package com.greatmancode.legendarybotapi.discorduser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class DiscordUserHelper {

    public static DiscordUser getDiscordUser(int id) {
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
        JSONObject userJSON = new JSONObject(user.getJson());
        if (userJSON.has("characters")) {
            userJSON.getJSONArray("characters").forEach(characterEntry -> {
                JSONObject character = (JSONObject) characterEntry;
                if (character.has("guild") && character.getString("guild").equalsIgnoreCase("guildName")) {
                    result.put(character);
                }
            });
        }
        return result;
    }

    public static JSONObject getGuildMainCharacter(DiscordUser user, int id) {
        final JSONObject result = new JSONObject();
        if (user != null) {
            JSONObject jsonObject = new JSONObject(user.getJson());
            if (jsonObject.has("characters")) {
                jsonObject.getJSONArray("characters").forEach(characterEntry -> {
                    JSONObject character = (JSONObject) characterEntry;
                    if (character.has("mainCharacterForGuild")) {
                        if (character.getJSONArray("mainCharacterForGuild").toList().stream().filter(value -> (int)value == id).count() == 1) {
                            result.put("region", character.getString("region"));
                            result.put("realm", character.getString("realm"));
                            result.put("name", character.getString("name"));
                        }
                    }
                });
            }

        }
        return result;
    }

    public static boolean setGuildMainCharacter(DiscordUser user, int guildId, String region, String realm, String name) {
        boolean found = false;
        if (user != null) {
            JSONObject jsonObject = new JSONObject(user.getJson());
            if (jsonObject.has("characters")) {
                JSONArray jsonArray = jsonObject.getJSONArray("characters");
                int i;

                for (i = 0; i < jsonArray.length(); i++) {
                    JSONObject character = jsonArray.getJSONObject(i);
                    if (character.getString("region").equalsIgnoreCase(region) && character.getString("realm").equalsIgnoreCase(realm) && character.getString("name").equalsIgnoreCase(name)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    //old character cleanup
                    for (int ii = 0; ii < jsonArray.length(); ii++) {
                        JSONObject character = jsonArray.getJSONObject(ii);
                        if (character.has("mainCharacterForGuild")) {
                            if (character.getJSONArray("mainCharacterForGuild").toList().stream().filter(value -> (int)value == guildId).count() == 1) {
                                Iterator<Object> iterator = character.getJSONArray("mainCharacterForGuild").iterator();
                                while (iterator.hasNext()) {
                                    int value = (int)iterator.next();
                                    if (value == guildId) {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                    }
                    JSONObject character = jsonArray.getJSONObject(i);
                    if (character.has("mainCharacterForGuild")) {
                        character.getJSONArray("mainCharacterForGuild").put(guildId);
                    } else {
                        JSONArray tempArray = new JSONArray();
                        tempArray.put(guildId);
                        character.put("mainCharacterForGuild",tempArray);
                    }
                    user.setJson(jsonObject.toString());
                    DiscordUserBackend.saveDiscordUser(user);
                }
            }
        }
        return found;
    }
}