package com.greatmancode.legendarybotapi.legendarycheck;

import com.greatmancode.legendarybotapi.discordguild.DiscordGuild;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildBackend;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;
import com.greatmancode.legendarybotapi.item.ItemHelper;
import com.greatmancode.legendarybotapi.messages.MessageHelper;
import com.greatmancode.legendarybotapi.queue.QueueBackend;
import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import com.greatmancode.legendarybotapi.utils.HeroClass;
import com.greatmancode.legendarybotapi.utils.WoWUtils;
import com.greatmancode.legendarybotapi.wow.WoWItemUtils;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class LegendaryCheckHandler {

    /**
     * ItemIDs to ignore, those are not Legendaries that we want to announce.
     */
    private static final long[] itemIDIgnore = {147451,151462, 152626, 154880};

    public static void handleLegendaryCheck() {
        DiscordGuildBackend.getDiscordGuilds().forEach(guild -> {
            if (isValidLegendaryCheck(guild)) {
                System.out.println("Running legendary check for  + " + guild.getid());
                MessageHelper.sendMessage(System.getenv("SNS_LEGENDARYCHECK"), guild.getid() + "");
            }
        });
    }

    public static void handleGuildLegendaryCheck(long id) throws IOException {
        DiscordGuildHelper helper = new DiscordGuildHelper();
        DiscordGuild guild = helper.getDiscordGuild(id);
        if (isValidLegendaryCheck(guild)) {
            System.out.println("Starting Legendary check for server " + id);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BattleNetAPIInterceptor())
                    .build();
            String regionName = helper.getSetting(guild, "WOW_REGION_NAME");
            String serverName = helper.getSetting(guild, "WOW_SERVER_NAME");
            String guildName = helper.getSetting(guild,"GUILD_NAME");
            String channelName = helper.getSetting(guild, "legendary_check");
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(regionName.toLowerCase() + ".api.battle.net")
                    .addPathSegments("/wow/guild/" + serverName + "/" + guildName)
                    .addQueryParameter("fields", "members,news")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Map<String, String> characterRealmMap = new HashMap<>();
            Response guildResponse = client.newCall(request).execute();
            String result = guildResponse.body().string();
            guildResponse.close();
            JSONObject guildJSON = new JSONObject(result);
            if (guildJSON.has("status")) {
                return;
            }

            //We load the guild character list
            JSONArray members = guildJSON.getJSONArray("members");
            for (Object memberRaw : members) {
                JSONObject member = (JSONObject) memberRaw;
                JSONObject character = member.getJSONObject("character");
                long level = (Long) character.get("level");
                if (level != 110) {
                    continue;
                }
                JSONObject realmInfo = WoWUtils.getRealmInformation(regionName,(String)character.get("realm"));
                if (realmInfo != null) {
                    characterRealmMap.put((String)character.get("name"), (String) realmInfo.get("slug"));
                }
            }

            //We do the check to see if a character is active so we query it's last actions.
            JSONArray news = (JSONArray) guildJSON.get("news");
            List<String> doneCharacter = new ArrayList<>();
            for (Object newsEntryRaw : news) {
                JSONObject newsEntry = (JSONObject) newsEntryRaw;
                String character = (String) newsEntry.get("character");
                long newsTimestamp = (long) newsEntry.get("timestamp");

                if (doneCharacter.contains(character)) {
                    continue;
                }

                long currentNewsTimestamp = getPlayerNewsDate(regionName,serverName,character);
                if (newsTimestamp > currentNewsTimestamp) {
                    setPlayerNewsDate(regionName,serverName,character,newsTimestamp);
                }
                doneCharacter.add(character);
            }

            LocalDateTime date = LocalDateTime.now().minusDays(7);
            long timeMinus7Days = date.toInstant(ZoneOffset.UTC).toEpochMilli();
            characterRealmMap.forEach((character,realm) -> {
                realm = realm.toLowerCase();
                if (getPlayerNewsDate(regionName,realm,character) > timeMinus7Days) {
                    //The character is active, let's do the legendary check.
                    HttpUrl characterUrl = new HttpUrl.Builder()
                            .scheme("https")
                            .host(regionName.toLowerCase() + ".api.battle.net")
                            .addPathSegments("/wow/character/" + realm + "/" + character)
                            .addQueryParameter("fields", "feed")
                            .build();
                    Request characterRequest = new Request.Builder().url(characterUrl).build();
                    boolean notOk = true;
                    Response characterResponse = null;
                    while (notOk) {
                        try {
                            characterResponse = client.newCall(characterRequest).execute();
                            notOk = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            notOk = false;
                        }
                    }
                    if (characterResponse != null) {
                        try {
                            String characterJSONRaw = characterResponse.body().string();
                            characterResponse.close();
                            JSONObject characterJSON = new JSONObject(characterJSONRaw);
                            if (!characterJSON.has("status")) {
                                long memberLastModified = (Long) characterJSON.get("lastModified");
                                long dbLastModified = getPlayerInventoryDate(regionName, realm, character);
                                if (memberLastModified > dbLastModified) {
                                    setPlayerInventoryDate(regionName, realm, character, memberLastModified);
                                    //We check the items
                                    JSONArray feedArray = characterJSON.getJSONArray("feed");
                                    for (Object feedObject : feedArray) {
                                        JSONObject feed = (JSONObject) feedObject;
                                        if (feed.getString("type").equals("LOOT")) {
                                            long itemID = feed.getLong("itemId");
                                            long timestamp = feed.getLong("timestamp");
                                            if (timestamp <= dbLastModified) {
                                                continue;
                                            }
                                            if (LongStream.of(itemIDIgnore).anyMatch(x -> x == itemID)) {
                                                continue;
                                            }
                                            String itemJson = ItemHelper.getItem(regionName, itemID).getJson();
                                            if (isItemLegendary(itemJson)) {
                                                System.out.println(character + " just looted a legendary");
                                                //We got a legendary!
                                                Map<String, String> metadata = new HashMap<>();
                                                metadata.put("name", character);
                                                metadata.put("server", id + "");
                                                metadata.put("channel", channelName);
                                                QueueBackend.sendMessage(System.getenv("SQS_QUEUE_NAME"),WoWItemUtils.buildEmbed(character, HeroClass.values()[((Long) characterJSON.get("class")).intValue()] ,itemJson).toJSONObject().toString(), metadata);
                                            }
                                        }
                                    }
                                }

                            } else {
                                System.out.println("Guild " + id + ") Member " + character + " with realm " + realm + " not found for WoW guild " + guildName + "-" + regionName);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


        }
    }

    private static long getPlayerInventoryDate(String regionName, String serverName, String character) {
        return DynamoDBHelper.getCharacterInventoryDate(regionName, serverName, character);
    }

    private static void setPlayerInventoryDate(String regionName, String serverName, String character, long inventoryDate) {
        DynamoDBHelper.setCharacterInventoryDate(regionName, serverName, character, inventoryDate);
    }

    private static long getPlayerNewsDate(String regionName, String serverName, String character) {
        return DynamoDBHelper.getCharacterNewsDate(regionName,serverName,character);
    }

    private static void setPlayerNewsDate(String regionName, String serverName, String character, long newsTimestamp) {
        DynamoDBHelper.setCharacterNewsDate(regionName,serverName,character,newsTimestamp);
    }

    /**
     * Check if an item is a legendary. Checks in the ES cache if we have the item. If not, we retrieve the information from Battle.Net API and cache it.
     * @return True if the item is a legendary. Else false.
     */
    public static boolean isItemLegendary(String json) {
        return (json != null) && new JSONObject(json).getInt("quality") == 5;
    }

    private static boolean isValidLegendaryCheck(DiscordGuild guild) {
        DiscordGuildHelper helper = new DiscordGuildHelper();
        String channelName = helper.getSetting(guild, "legendary_check");
        String regionName = helper.getSetting(guild, "WOW_REGION_NAME");
        String serverName = helper.getSetting(guild, "WOW_SERVER_NAME");
        String guildName = helper.getSetting(guild,"GUILD_NAME");
        return channelName != null && regionName != null && serverName != null && guildName != null;
    }

}
