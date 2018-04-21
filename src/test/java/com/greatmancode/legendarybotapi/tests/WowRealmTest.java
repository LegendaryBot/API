package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.wow.WoWRealmUtils;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WowRealmTest {

    @Test
    public void testRealmStatus() {
        MessageEmbed embed = WoWRealmUtils.getRealmStatus("us", "arthas");
        assertFalse(embed.isEmpty());
        embed = WoWRealmUtils.getRealmStatus("us", "dkdkdkd");
        assertNull(embed);
    }
}
