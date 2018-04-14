package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBDiscordUser;
import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

public class DiscordUserBackend {

    public static DiscordUser getDiscordUser(int id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getDiscordUser(id);
        }
        return null;
    }

    public static void saveDiscordUser(DiscordUser user) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            if (!(user instanceof DynamoDBDiscordUser)) {
                DynamoDBDiscordUser dynamoDBDiscordUser = new DynamoDBDiscordUser();
                dynamoDBDiscordUser.setid(user.getid());
                dynamoDBDiscordUser.setJson(user.getJson());
                DynamoDBHelper.saveDiscordUser(dynamoDBDiscordUser);
            } else {
                DynamoDBHelper.saveDiscordUser((DynamoDBDiscordUser) user);
            }
        }

    }
}
