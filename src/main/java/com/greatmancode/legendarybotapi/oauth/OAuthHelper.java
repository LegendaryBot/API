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
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OAuthHelper {

    public static String generateLoginURL(String region, long userId) {
        OAuth20Service service = new ServiceBuilder(System.getenv(region.toUpperCase() + "_KEY"))
                .scope("wow.profile")
                .callback("https://"+System.getenv("API_URL")+"/api/oauth/battlenetcallback")
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
        long userId = Long.parseLong(state.split(":")[1]);
        OAuth20Service service = new ServiceBuilder(System.getenv(region.toUpperCase() + "_KEY"))
                .apiSecret(System.getenv(region.toUpperCase() + "_SECRET"))
                .scope("wow.profile")
                .callback("https://"+System.getenv("API_URL")+"/api/oauth/battlenetcallback")
                .build(new OAuthBattleNetApi(region));
        try {
            OAuth2AccessToken token = service.getAccessToken(code);
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://"+region+".api.battle.net/wow/user/characters");
            service.signRequest(token, request);
            Response response = service.execute(request);
            DiscordUserHelper userHelper = new DiscordUserHelper();
            handleCharacterUpdate(region, userHelper.getDiscordUser(userId), response.getBody());

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            UncaughtExceptionHandler.getHandler().sendException(e);
        }
    }

    public static DiscordUser handleCharacterUpdate(String region, DiscordUser discordUser, String json) {
        JSONObject userJSON = new JSONObject(json);
        JSONArray blizzardCharacterArray = userJSON.getJSONArray("characters");

        //We load the current user.
        List<WoWCharacter> currentCharacters = discordUser.getCharacters();

        //We load blizzard characters
        List<WoWCharacter> blizzardCharacters = new ArrayList<>();
        blizzardCharacterArray.forEach(characterEntry -> {
            JSONObject character = (JSONObject) characterEntry;
            blizzardCharacters.add(new WoWCharacter(region, character.getString("realm").toLowerCase(), character.getString("name"), character.has("guild") ? character.getString("guild") : null, new ArrayList<>()));
        });

        //We remove from the user all characters that don't exist.
        currentCharacters.removeIf(currentCharacter -> !blizzardCharacters.contains(currentCharacter));

        //We now load the user with the characters
        blizzardCharacters.forEach(blizzardCharacter -> {
            int index = currentCharacters.indexOf(blizzardCharacter);
            if (index != -1) {
                WoWCharacter character = currentCharacters.get(index);
                character.setGuild(blizzardCharacter.getGuild());
            } else {
                currentCharacters.add(blizzardCharacter);
            }

        });
        discordUser.updateCharacters(currentCharacters);
        DiscordUserBackend.saveDiscordUser(discordUser);
        return discordUser;
    }
}
