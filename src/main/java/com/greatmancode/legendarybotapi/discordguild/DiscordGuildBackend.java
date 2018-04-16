package com.greatmancode.legendarybotapi.discordguild;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

public class DiscordGuildBackend {

    public static DiscordGuild getDiscordGuild(long id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getDiscordGuild(id);
        }
        return null;
    }

    public static void saveDiscordGuild(DiscordGuild guild) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            DynamoDBHelper.saveDiscordGuild(guild);
        }

    }
}
