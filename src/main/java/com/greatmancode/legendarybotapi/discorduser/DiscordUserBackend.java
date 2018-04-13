package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBDiscordUser;
import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

public class DiscordUserBackend {

    public static DiscordUser getDiscordUser(int id) {
        return DynamoDBHelper.getDiscordUser(id);
    }

    public static void saveDiscordUser(DiscordUser user) {
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
