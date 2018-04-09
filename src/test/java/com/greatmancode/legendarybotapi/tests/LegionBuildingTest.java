package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.general.LegionBuildingHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LegionBuildingTest {

    @Test
    public void testLegionBuilding() {
        assertNotNull(LegionBuildingHelper.getLegionBuildingStatus("us"));
        assertNotNull(LegionBuildingHelper.getLegionBuildingStatus("eu"));
    }
}
