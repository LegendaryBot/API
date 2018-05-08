package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.wow.WowRegionUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WowRegionTest {

    @Test
    public void testToken() {
        assertNotNull(WowRegionUtils.getTokenPrice("us"));
        assertNotNull(WowRegionUtils.getTokenPrice("eu"));
        assertNull(WowRegionUtils.getTokenPrice("sdfiugh"));
    }
}
