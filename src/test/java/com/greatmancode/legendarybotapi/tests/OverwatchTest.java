package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.overwatch.OverwatchHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OverwatchTest {

    @Test
    public void testOverwatch() {
        assertNull(OverwatchHelper.getOverwatchStats("us", "Greatman-1189"));
        assertNotNull(OverwatchHelper.getOverwatchStats("us","Rachityk-1116"));
    }
}

