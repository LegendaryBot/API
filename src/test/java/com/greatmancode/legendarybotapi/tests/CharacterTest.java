package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.characters.CharacterHelper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    @Test
    public void testCharacter() throws UnsupportedEncodingException {
        JSONObject object = CharacterHelper.getCharacterStatsEmbed("us","arthas", "Kugruon");
        assertNotNull(object);
        //assertTrue(object.getJSONObject("author").getString("name").contains("Kugruon"));
        object = CharacterHelper.getCharacterStatsEmbed("us","arthas", "characterThatDoesntExist");
        assertNull(object);
        System.out.println(CharacterHelper.getCharacterStatsEmbed("us", "frostmourne", "Jácé"));
    }
}
