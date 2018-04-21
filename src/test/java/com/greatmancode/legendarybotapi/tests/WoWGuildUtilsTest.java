package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.wow.WoWGuildUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WoWGuildUtilsTest {

    @Test
    public void testLogGuild() {
        MessageEmbed embed = WoWGuildUtils.getLastestWarcraftLog("us","arthas","Legendary");
        assertNotNull(embed);
        embed = WoWGuildUtils.getLastestWarcraftLog("us","arthas","Legendaryee");
        assertNull(embed);
    }
}
