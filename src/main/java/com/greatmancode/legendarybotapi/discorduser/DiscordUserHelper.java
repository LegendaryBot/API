package com.greatmancode.legendarybotapi.discorduser;

import org.json.JSONObject;

public class DiscordUserHelper {

    public static DiscordUser getDiscordUser(int id) {
        return DiscordUserBackend.getDiscordUser(id);
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
    public static JSONObject getGuildMainCharacter(DiscordUser user, int id) {
        final JSONObject result = new JSONObject();
        if (user != null) {
            JSONObject jsonObject = new JSONObject(user.getJson());
            if (jsonObject.has("characters")) {
                jsonObject.getJSONArray("characters").forEach(characterEntry -> {
                    JSONObject character = (JSONObject) characterEntry;
                    if (character.has("mainCharacterForGuild")) {
                        character.getJSONArray("mainCharacterForGuild").forEach(guildId -> {
                            if ((int)guildId == id) {
                                result.put("region", character.getString("region"));
                                result.put("realm", character.getString("realm"));
                                result.put("name", character.getString("name"));
                            }
                        });
                    }
                });
            }

        }
        return result;
    }
}
