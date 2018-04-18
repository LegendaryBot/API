package com.greatmancode.legendarybotapi.discordguild;

import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DiscordGuildHelper {

    private final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .connectionPool(new ConnectionPool(300, 1, TimeUnit.SECONDS))
            .build();

    public DiscordGuild getDiscordGuild(long guildId) {
        DiscordGuild guild = DiscordGuildBackend.getDiscordGuild(guildId);
        if (guild == null) {
            guild = new DiscordGuildImpl();
            guild.setid(guildId);
            guild.setJson(new JSONObject().toString());
            DiscordGuildBackend.saveDiscordGuild(guild);
        }
        return guild;
    }

    public void setSetting(DiscordGuild guild, String key, Object value) {
        JSONObject guildJSON = new JSONObject(guild.getJson());
        if (!guildJSON.has("settings")) {
            guildJSON.put("settings", new JSONObject());
        }
        guildJSON.getJSONObject("settings").put(key,value);
        guild.setJson(guildJSON.toString());
        DiscordGuildBackend.saveDiscordGuild(guild);
    }

    public <T extends Object> T getSetting(DiscordGuild guild, String key) {
        T value = null;
        JSONObject guildJSON = new JSONObject(guild.getJson());
        if (guildJSON.has("settings") && guildJSON.getJSONObject("settings").has(key)) {
            value = (T) guildJSON.getJSONObject("settings").get(key);
        }
        return value;
    }

    public void unsetSetting(DiscordGuild guild, String key) {
        JSONObject guildJSON = new JSONObject(guild.getJson());
        if (guildJSON.has("settings") && guildJSON.getJSONObject("settings").has(key)) {
            guildJSON.getJSONObject("settings").remove(key);
        }
        guild.setJson(guildJSON.toString());
        DiscordGuildBackend.saveDiscordGuild(guild);
    }


    /*
        {
            "guild": {
                "discordId": 19579275975
                ranks: [
                    {
                        "name": "Raider",
                        "id": 98579347597, #if exist
                        "position": 3,
                        "managerole": true,
                    },
                    {
                        "name": "Member",
                        "id": 025735345,
                        "position": "4",
                        "managerole": false
                    }
                    ....
                ]
                "botranks": [
                    "SuperAdmin",
                    "LegendaryBot",
                ]
            }
            "users": [
                {
                    "discordId": 124623856285
                    "ranks": [
                        "Raider",
                        "Member",
                        "Admin",
                    ]
                }
                ....
            ]
        }

     */
    /*
        RESULT JSON:
        {
            "roleChange": [
                {
                    "discordId": 387957345,
                    "ranksToAdd": "Raider",
                    "ranksToRemove": ["Member"]
                }
            ]
        }
     */
    public JSONObject processRankUpdate(DiscordGuild discordGuild, String json) {
        JSONObject guildJSON = new JSONObject(json);
        JSONObject guildInformation = guildJSON.getJSONObject("guild");
        Map<String, JSONObject> guildRanks = new HashMap<>();
        guildInformation.getJSONArray("ranks").forEach(rankEntry -> {
            JSONObject rank = (JSONObject) rankEntry;
            guildRanks.put(rank.getString("name"), rank);
        });

        //We get the rank position of the bot so we know which roles it can set.
        final int[] botRoleRank = {-999};
        guildInformation.getJSONArray("botranks").forEach(botRankEntry -> {
            JSONObject role = guildRanks.get(botRankEntry);
            if (role.getBoolean("managerole")) {
                int rankPosition = role.getInt("position");
                if (rankPosition > botRoleRank[0]) {
                    botRoleRank[0] = rankPosition;
                }
            }

        });

        //We retrieve the ranks in the configuration
        JSONObject discordGuildJSON = getSetting(discordGuild, "wowranks");
        Map<Integer,String> discordRankMapping = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            if (discordGuildJSON.has(i + "")) {
                discordRankMapping.put(i,discordGuildJSON.getString(i + ""));
            }
        }

        //We collect the information from battle.net
        Map<String, Integer> guildMembers = getBattleNetMemberRank(discordGuild);
        if (guildMembers != null) {
            JSONObject resultJSON = new JSONObject();
            JSONArray roleChange = new JSONArray();


            JSONArray userArray = guildJSON.getJSONArray("users");
            DiscordUserHelper userHelper = new DiscordUserHelper();
            userArray.forEach(userEntry -> {
                JSONObject user = (JSONObject) userEntry;
                DiscordUser discordUser = userHelper.getDiscordUser(user.getLong("discordId"));

                //We prepare the JSON of the user
                JSONObject userJSON = new JSONObject();
                userJSON.put("discordId", user.getLong("discordId"));

                String discordRank = null;
                WoWCharacter mainCharacter = discordUser.getCharacters().stream()
                        .filter(u -> u.getMainCharacterForGuild().contains(discordGuild.getid()))
                        .findFirst().orElse(null);
                if (mainCharacter != null && guildMembers.containsKey(mainCharacter.getName())) {
                    //We found a character. Let's get his rank and apply it to him.

                    //We get the rank of the player in the guild.
                    int rankID = guildMembers.get(mainCharacter.getName());
                    //If we have the rank in our mapping, add it to the possible to add
                    if (discordRankMapping.containsKey(rankID)) {
                        discordRank = discordRankMapping.get(rankID);
                        //Does the discord rank exist?
                        if (guildRanks.containsKey(discordRank)) {
                            //Does the user not have the rank?
                            if (!user.getJSONArray("ranks").toList().contains(discordRank)) {
                                //The user does not have the rank, let's add it to him.
                                JSONObject discordRankJSON = guildRanks.get(discordRank);
                                if (botRoleRank[0] > discordRankJSON.getInt("position")) {
                                    //the bot rank is higher than the given rank, so we can set it.
                                    userJSON.put("rankToAdd", discordRank);
                                }
                            }
                        }
                    }

                }
                JSONArray ranksToRemove = new JSONArray();
                //We remove all the other ranks that we can from the user.
                user.getJSONArray("ranks").forEach(rankEntry -> {
                    if (discordRankMapping.values().contains(rankEntry)) {
                        JSONObject discordRankJSON = guildRanks.get(rankEntry);
                        if (botRoleRank[0] > discordRankJSON.getInt("position")) {
                            ranksToRemove.put(rankEntry);
                        }
                    }
                });
                ranksToRemove.remove(ranksToRemove.toList().indexOf(discordRank));
                if (ranksToRemove.length() != 0) {
                    userJSON.put("ranksToRemove", ranksToRemove);
                }
                //If we only have the discordId in the json, no changes are needed so don't send them.
                if (userJSON.length() > 1) {
                    roleChange.put(userJSON);
                }
            });

            resultJSON.put("roleChange",roleChange);
            return resultJSON;
        }

        return new JSONObject();
    }

    public Map<String, Integer> getBattleNetMemberRank(DiscordGuild discordGuild) {
        Map<String, Integer> guildMembers = new HashMap<>();
        String serverName = getSetting(discordGuild, "WOW_SERVER_NAME");
        String region = getSetting(discordGuild, "WOW_REGION_NAME");
        String guildName = getSetting(discordGuild, "GUILD_NAME");
        if (serverName != null && region != null && guildName != null) {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host(region.toLowerCase() + ".api.battle.net")
                    .addPathSegments("wow/guild/" + serverName + "/" + guildName)
                    .addQueryParameter("fields", "members")
                    .build();
            Request request = new Request.Builder().url(url).build();
            try {
                String battlenetResult = clientBattleNet.newCall(request).execute().body().string();
                JSONObject battleNetJSON = new JSONObject(battlenetResult);
                if (!battleNetJSON.has("status")) {
                    battleNetJSON.getJSONArray("members").forEach(memberEntry -> {
                        JSONObject member = (JSONObject) memberEntry;
                        guildMembers.put(member.getJSONObject("characters").getString("name"), member.getJSONObject("characters").getInt("rank"));
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (guildMembers.size() != 0) ? guildMembers : null;
    }
}
