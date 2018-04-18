package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

import java.util.HashMap;
import java.util.Map;

public class DiscordUserBackend {

    private static Map<Long, DiscordUser> memoryUserBackend = new HashMap<>();
    public static DiscordUser getDiscordUser(long id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getDiscordUser(id);
        } else {
            if (memoryUserBackend.containsKey(id)) {
                return memoryUserBackend.get(id);
            }
        }
        return null;
    }

    public static void saveDiscordUser(DiscordUser user) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            DynamoDBHelper.saveDiscordUser(user);
        } else {
            memoryUserBackend.put(user.getid(), user);
        }

    }
}
