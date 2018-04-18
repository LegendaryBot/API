package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.characters.CharacterHelper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CharacterTest {

    @Test
    public void testCharacter() {
        JSONObject object = CharacterHelper.getCharacterStatsEmbed("us","arthas", "Kugruon");
        assertNotNull(object);
        //assertTrue(object.getJSONObject("author").getString("name").contains("Kugruon"));
        object = CharacterHelper.getCharacterStatsEmbed("us","arthas", "characterThatDoesntExist");
        assertNull(object);
        System.out.println(CharacterHelper.getCharacterStatsEmbed("us", "frostmourne", "Jácé"));
    }
}
