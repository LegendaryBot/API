package com.greatmancode.legendarybotapi.impl.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserImpl;
import com.greatmancode.legendarybotapi.item.Item;
import com.greatmancode.legendarybotapi.item.ItemImpl;


public class DynamoDBHelper {

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(System.getenv("AWS_REGION")).build();
    private static DynamoDB dynamoDB = new DynamoDB(client);

    public static Item getItem(long id) {
        Item item = null;
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_ITEM"));
        com.amazonaws.services.dynamodbv2.document.Item entry = table.getItem("id", id);
        if (entry != null) {
            item = new ItemImpl();
            item.setid(id);
            item.setJson(entry.getJSON("json"));
        }

        return item;
    }

    public static void saveItem(Item item) {
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_ITEM"));
        table.putItem(new com.amazonaws.services.dynamodbv2.document.Item().withPrimaryKey("id", item.getid()).withJSON("json", item.getJson()));
    }

    public static DiscordUser getDiscordUser(long id) {
        DiscordUser user = null;
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_DISCORD_USER"));
        com.amazonaws.services.dynamodbv2.document.Item entry = table.getItem("id", id);
        if (entry != null) {
            user = new DiscordUserImpl();
            user.setid(id);
            user.setJson(entry.getJSON("json"));
        }
        return user;
    }

    public static void saveDiscordUser(DiscordUser discordUser) {
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_DISCORD_USER"));
        table.putItem(new com.amazonaws.services.dynamodbv2.document.Item().withPrimaryKey("id", discordUser.getid()).withJSON("json", discordUser.getJson()));
    }
}
