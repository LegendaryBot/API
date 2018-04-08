package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.characters.CharacterHelper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    @Test
    void testCharacter() {
        JSONObject object = CharacterHelper.getCharacterStatsEmbed("us","arthas", "Kugruon");
        assertNotNull(object);
        assertTrue(object.getJSONObject("author").getString("name").contains("Kugruon"));
        object = CharacterHelper.getCharacterStatsEmbed("us","arthas", "characterThatDoesntExist");
        assertNull(object);
    }
}
