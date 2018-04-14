package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.item.ItemHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemTest {

    @Test
    public void testItem() {

       assertNotNull(ItemHelper.getItem("us", 132455L));
    }
}
