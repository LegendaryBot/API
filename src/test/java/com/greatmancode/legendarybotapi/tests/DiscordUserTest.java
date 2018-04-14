package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class DiscordUserTest {

    @Test
    public void testDiscordUser() {
        JSONObject object = new JSONObject();
        JSONArray characters = new JSONArray();

        JSONObject character = new JSONObject();
        character.put("region", "us");
        character.put("realm", "arthas");
        character.put("name", "Kugruon");
        JSONArray mainCharacterGuild = new JSONArray();
        mainCharacterGuild.put(1234L);
        mainCharacterGuild.put(4567L);
        character.put("mainCharacterForGuild", mainCharacterGuild);
        characters.put(character);

        character = new JSONObject();
        character.put("region", "eu");
        character.put("realm", "moon-guard");
        character.put("name", "Kogorof");
        characters.put(character);
        object.put("characters", characters);
        DiscordUser discordUser = new DiscordUserImpl();
        discordUser.setid(1111L);
        discordUser.setJson(object.toString());

        JSONObject mainCharacter = DiscordUserHelper.getGuildMainCharacter(discordUser, 1234L);
        assertEquals(mainCharacter.length(), 3);
        assertEquals(mainCharacter.getString("region"), "us");
        assertEquals(mainCharacter.getString("realm"), "arthas");
        assertEquals(mainCharacter.getString("name"), "Kugruon");

        mainCharacter = DiscordUserHelper.getGuildMainCharacter(discordUser, 4567L);
        assertEquals(mainCharacter.length(), 3);
        assertEquals(mainCharacter.getString("region"), "us");
        assertEquals(mainCharacter.getString("realm"), "arthas");
        assertEquals(mainCharacter.getString("name"), "Kugruon");

        mainCharacter = DiscordUserHelper.getGuildMainCharacter(discordUser, 3L);
        assertEquals(mainCharacter.length(), 0);

        DiscordUserHelper.setGuildMainCharacter(discordUser, 1234L, "eu","moon-guard", "Kogorof");
        JSONObject resultSet = new JSONObject(discordUser.getJson());
        JSONArray characterArray = resultSet.getJSONArray("characters");
        JSONObject characterKugruon = characterArray.getJSONObject(0);
        characterKugruon.getJSONArray("mainCharacterForGuild").forEach(value -> assertNotEquals(value, 1234L));

        JSONObject characterKogorof = characterArray.getJSONObject(1);
        characterKogorof.getJSONArray("mainCharacterForGuild").forEach(value -> assertEquals(value, 1234L));
    }
}
