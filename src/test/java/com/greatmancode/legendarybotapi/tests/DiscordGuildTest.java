package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.discordguild.DiscordGuild;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscordGuildTest {


    @Test
    public void testDiscordGuild() {
        DiscordGuild guild = DiscordGuildHelper.getDiscordGuild(12345L);
        assertNotNull(guild);
        assertNull(DiscordGuildHelper.getSetting(guild,"testsetting"));
        DiscordGuildHelper.setSetting(guild, "testsetting", "teststring");
        assertEquals("teststring", DiscordGuildHelper.getSetting(guild, "testsetting"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1","value1");
        jsonObject.put("key2", "value2");
        DiscordGuildHelper.setSetting(guild, "testjson", jsonObject);
        assertEquals("teststring", DiscordGuildHelper.getSetting(guild, "testsetting"));
        assertTrue(jsonObject.similar(DiscordGuildHelper.getSetting(guild, "testjson")));
        DiscordGuildHelper.unsetSetting(guild, "testjson");
        assertNull(DiscordGuildHelper.getSetting(guild, "testjson"));

    }
}
