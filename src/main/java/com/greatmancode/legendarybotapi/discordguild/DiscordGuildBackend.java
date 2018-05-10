package com.greatmancode.legendarybotapi.discordguild;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DiscordGuildBackend {

    private static Map<Long, DiscordGuild> memoryGuildBackend = new HashMap<>();
    public static DiscordGuild getDiscordGuild(long id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getDiscordGuild(id);
        } else {
            if (memoryGuildBackend.containsKey(id)) {
                return memoryGuildBackend.get(id);
            }
        }
        return null;
    }

    public static void saveDiscordGuild(DiscordGuild guild) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            DynamoDBHelper.saveDiscordGuild(guild);
        } else {
            memoryGuildBackend.put(guild.getid(), guild);
        }

    }

    public static Collection<DiscordGuild> getDiscordGuilds() {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getGuilds();
        } else {
            return memoryGuildBackend.values();
        }
    }
}
