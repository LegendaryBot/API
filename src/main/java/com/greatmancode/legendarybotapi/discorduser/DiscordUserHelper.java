package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.discordguild.DiscordGuild;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DiscordUserHelper {

    private static final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .connectionPool(new ConnectionPool(300, 1, TimeUnit.SECONDS))
            .build();

    public static DiscordUser getDiscordUser(long id) {
        DiscordUser user = DiscordUserBackend.getDiscordUser(id);
        if (user == null) {
            user = new DiscordUserImpl();
            user.setid(id);
            user.setJson(new JSONObject().toString());
            DiscordUserBackend.saveDiscordUser(user);
        }
        return user;
    }
    /*
        {
            "characters":[
                {
                    "region": "us",
                    "realm": "arthas",
                    "name": "Kugruon",
                    "mainCharacterForGuild": [
                        12326423682352,
                        65826583465834,
                        23957257352921
                    ]

                },
            ]
        }

     */

    public static JSONArray getGuildCharactersForUser(DiscordUser user, String guildName) {
        JSONArray result = new JSONArray();
        List<WoWCharacter> characterList = user.getCharacters();
        characterList.forEach(characterEntry -> {
            if (guildName.equalsIgnoreCase(characterEntry.getGuild())) {
                result.put(characterEntry.getName());
            }
        });
        return result;
    }

    public static JSONObject getGuildMainCharacter(DiscordUser user, long id) {
        final JSONObject result = new JSONObject();
        if (user != null) {
            List<WoWCharacter> characterList = user.getCharacters();
            characterList.forEach(characterEntry -> {
                if (characterEntry.getMainCharacterForGuild().contains(id)) {
                    result.put("region", characterEntry.getRegion());
                    result.put("realm", characterEntry.getRealm());
                    result.put("name", characterEntry.getName());
                }
            });

        }
        return result;
    }

    public static boolean setGuildMainCharacter(DiscordUser user, long guildId, String region, String realm, String name) {
        boolean found = false;
        if (user != null) {
            WoWCharacter characterToSet = new WoWCharacter(region, realm, name, null, new ArrayList<>());
            List<WoWCharacter> userCharacters = user.getCharacters();
            if (userCharacters.contains(characterToSet)) {
                found = true;
                //We check if any characters are already set as main.
                userCharacters.forEach(characterEntry -> {
                    if (characterEntry.getMainCharacterForGuild().contains(guildId)) {
                        characterEntry.getMainCharacterForGuild().remove(guildId);
                    }
                });
                userCharacters.get(userCharacters.indexOf(characterToSet)).getMainCharacterForGuild().add(guildId);
                user.updateCharacters(userCharacters);
                DiscordUserBackend.saveDiscordUser(user);
            }
        }
        return found;
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
                    "ranksToAdd": ["Raider"],
                    "ranksToRemove": ["Member"]
                }
            ]
        }
     */
    public static JSONObject processRankUpdate(long guildId, String json) {
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


        //We collect the information from battle.net
        DiscordGuild discordGuild = DiscordGuildHelper.getDiscordGuild(guildId);
        String serverName = DiscordGuildHelper.getSetting(discordGuild, "WOW_SERVER_NAME");
        String region = DiscordGuildHelper.getSetting(discordGuild, "WOW_REGION_NAME");
        String guildName = DiscordGuildHelper.getSetting(discordGuild, "GUILD_NAME");
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
                    Map<String, Integer> guildMembers = new HashMap<>();
                    battleNetJSON.getJSONArray("members").forEach(memberEntry -> {
                        JSONObject member = (JSONObject) memberEntry;
                        guildMembers.put(member.getJSONObject("characters").getString("name"),member.getJSONObject("characters").getInt("rank"));
                    });

                    JSONObject resultJSON = new JSONObject();
                    JSONArray roleChange = new JSONArray();


                    JSONArray userArray = guildJSON.getJSONArray("users");
                    userArray.forEach(userEntry -> {
                        JSONObject user = (JSONObject) userEntry;
                        DiscordUser discordUser = DiscordUserHelper.getDiscordUser(user.getLong("discordId"));
                        List<WoWCharacter> userCharacters = discordUser.getCharacters();
                        if (userCharacters.size() > 0) {
                            WoWCharacter mainCharacter = userCharacters.stream()
                                    .filter(u -> u.getMainCharacterForGuild().contains(guildId))
                                    .findFirst().orElse(null);
                            if (mainCharacter != null && guildMembers.containsKey(mainCharacter.getName())) {
                                //We found a character. Let's get his rank and apply it to him.
                            }
                        }
                    });

                    resultJSON.put("roleChange",resultJSON);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



        }

        return new JSONObject();
    }
}
