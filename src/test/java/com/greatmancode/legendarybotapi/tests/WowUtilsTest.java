package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.utils.WoWUtils;
import org.junit.jupiter.api.Test;

public class WowUtilsTest {

    @Test
    public void testGetRealmInformation() {
        System.out.println(WoWUtils.getRealmInformation("us", "aerie-peak"));
    }
}
