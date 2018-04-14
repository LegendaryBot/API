package com.greatmancode.legendarybotapi.oauth;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserBackend;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.greatmancode.legendarybotapi.utils.OAuthBattleNetApi;
import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OAuthHelper {

    public static String generateLoginURL(String region, int userId) {
        OAuth20Service service = new ServiceBuilder(System.getenv(region.toUpperCase() + "_KEY"))
                .scope("wow.profile")
                .callback("https://legendarybotapi.greatmancode.com/api/oauth/battlenetcallback")
                .state(region + ":" + userId)
                .build(new OAuthBattleNetApi(region));
        return service.getAuthorizationUrl();
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
    public static void handleBattleNetCallback(String code, String state) {
        String region = state.split(":")[0];
        int userId = Integer.parseInt(state.split(":")[1]);
        OAuth20Service service = new ServiceBuilder(System.getenv(region.toUpperCase() + "_KEY"))
                .apiSecret(System.getenv(region.toUpperCase() + "_SECRET"))
                .scope("wow.profile")
                .callback("https://legendarybotapi.greatmancode.com/api/oauth/battlenetcallback")
                .build(new OAuthBattleNetApi(region));
        try {
            OAuth2AccessToken token = service.getAccessToken(code);
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://"+region+".api.battle.net/wow/user/characters");
            service.signRequest(token, request);
            Response response = service.execute(request);
            JSONObject userJSON = new JSONObject(response.getBody());
            JSONArray characterArray = userJSON.getJSONArray("characters");

            //We load the current user.
            DiscordUser discordUser = DiscordUserHelper.getDiscordUser(userId);
            JSONObject discordUserJSON = new JSONObject(discordUser.getJson());
            List<WoWCharacter> existingCharacters = new ArrayList<>();
            if (discordUserJSON.length() > 0) {
                if (discordUserJSON.has("characters")) {
                    discordUserJSON.getJSONArray("characters").forEach(characterEntry -> {
                        JSONObject character = (JSONObject) characterEntry;
                        existingCharacters.add(new WoWCharacter(character.getString("region"),character.getString("realm"),character.getString("name"), character.has("guild") ? character.getString("guild") : null));
                    });
                }
            }

            //We now add/remove accordingly
            List<WoWCharacter> characterToUpdate = new ArrayList<>();
            characterArray.forEach(characterEntry -> {
                JSONObject character = (JSONObject) characterEntry;
                WoWCharacter wowCharacter = new WoWCharacter(region, character.getString("realm").toLowerCase(), character.getString("name"), character.has("guild") ? character.getString("guild") : null);
                if (existingCharacters.contains(wowCharacter)) {
                    characterToUpdate.add(wowCharacter);
                    existingCharacters.remove(wowCharacter);
                }
            });

            if (existingCharacters.size() > 0) {
                //We remove old characters no longer existing.
                Iterator<Object> characterIterator = characterArray.iterator();
                while (characterIterator.hasNext()) {
                    JSONObject character = (JSONObject) characterIterator.next();
                    WoWCharacter arrayCharactor = new WoWCharacter(character.getString("region"),character.getString("realm"),character.getString("name"), character.has("guild") ? character.getString("guild") : null);
                    if (existingCharacters.contains(arrayCharactor)) {
                        characterIterator.remove();
                        existingCharacters.remove(arrayCharactor);
                    }
                }
            }

            if (characterToUpdate.size() > 0) {
                characterToUpdate.forEach(characterEntry -> {
                    JSONObject character = new JSONObject();
                    character.put("region", region);
                    character.put("realm", characterEntry.getRealm());
                    character.put("name", characterEntry.getName());
                    character.put("guild", characterEntry.getGuild());
                    characterArray.put(character);
                });
            }
            discordUser.setJson(discordUserJSON.toString());
            DiscordUserBackend.saveDiscordUser(discordUser);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
