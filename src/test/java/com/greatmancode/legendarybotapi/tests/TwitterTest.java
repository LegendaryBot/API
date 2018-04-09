package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.general.TwitterHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TwitterTest {

    @Test
    public void testTwitter() {
        assertNotNull(TwitterHelper.getLastTweet("us"));
    }
}
