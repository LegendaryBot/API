package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiscordUserHelper {

    public DiscordUser getDiscordUser(long id) {
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

    public JSONArray getGuildCharactersForUser(DiscordUser user, String guildName) {
        JSONArray result = new JSONArray();
        List<WoWCharacter> characterList = user.getCharacters();
        characterList.forEach(characterEntry -> {
            if (guildName.equalsIgnoreCase(characterEntry.getGuild())) {
                result.put(characterEntry.getName());
            }
        });
        return result;
    }

    public JSONObject getGuildMainCharacter(DiscordUser user, long id) {
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

    public boolean setGuildMainCharacter(DiscordUser user, long guildId, String region, String realm, String name) {
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

}
