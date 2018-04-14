package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

public class DiscordUserBackend {

    public static DiscordUser getDiscordUser(long id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getDiscordUser(id);
        }
        return null;
    }

    public static void saveDiscordUser(DiscordUser user) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            DynamoDBHelper.saveDiscordUser(user);
        }

    }
}
