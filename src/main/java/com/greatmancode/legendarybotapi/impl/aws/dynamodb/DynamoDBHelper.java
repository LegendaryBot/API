package com.greatmancode.legendarybotapi.impl.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuild;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildImpl;
import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserImpl;
import com.greatmancode.legendarybotapi.item.Item;
import com.greatmancode.legendarybotapi.item.ItemImpl;

import java.util.ArrayList;
import java.util.List;


public class DynamoDBHelper {

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(System.getenv("AWS_REGION") != null ? System.getenv("AWS_REGION") : "us-east-1").build();
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

    public static DiscordGuild getDiscordGuild(long id) {
        DiscordGuild guild = null;
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_DISCORD_GUILD"));
        com.amazonaws.services.dynamodbv2.document.Item entry = table.getItem("id", id);
        if (entry != null) {
            guild = new DiscordGuildImpl();
            guild.setid(id);
            guild.setJson(entry.getJSON("json"));
        }
        return guild;
    }

    public static void saveDiscordGuild(DiscordGuild discordGuild) {
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_DISCORD_GUILD"));
        table.putItem(new com.amazonaws.services.dynamodbv2.document.Item().withPrimaryKey("id", discordGuild.getid()).withJSON("json", discordGuild.getJson()));
    }

    public static List<DiscordGuild> getGuilds() {
        List<DiscordGuild> guildList = new ArrayList<>();
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_DISCORD_GUILD"));
        ItemCollection<ScanOutcome> scan = table.scan();
        scan.pages().forEach(pages -> {
            pages.forEach(page -> {
                DiscordGuild guild = new DiscordGuildImpl();
                guild.setid(page.getLong("id"));
                guild.setJson(page.getJSON("json"));
                guildList.add(guild);
            });
        });
        return guildList;
    }

    public static long getCharacterInventoryDate(String region, String realm, String characterName) {
        long time = -1;
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_LEGENDARYCHECK"));
        com.amazonaws.services.dynamodbv2.document.Item item = table.getItem("id", String.join("-",region,realm,characterName));
        if (item != null && item.hasAttribute("inventoryDate")) {
            time = item.getLong("inventoryDate");
        }
        return time;
    }

    public static long getCharacterNewsDate(String region, String realm, String characterName) {
        long time = -1;
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_LEGENDARYCHECK"));
        com.amazonaws.services.dynamodbv2.document.Item item = table.getItem("id", String.join("-",region,realm,characterName));
        if (item != null && item.hasAttribute("newsDate")) {
            time = item.getLong("newsDate");
        }
        return time;
    }

    public static void setCharacterInventoryDate(String region, String realm, String characterName, long inventoryDate) {
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_LEGENDARYCHECK"));
        table.updateItem(new UpdateItemSpec().withPrimaryKey("id",String.join("-",region,realm,characterName)).addAttributeUpdate(new AttributeUpdate("inventoryDate").addNumeric(inventoryDate)));
    }

    public static void setCharacterNewsDate(String region, String realm, String characterName, long newsDate) {
        Table table = dynamoDB.getTable(System.getenv("DYNAMODB_TABLE_LEGENDARYCHECK"));
        table.updateItem(new UpdateItemSpec().withPrimaryKey("id",String.join("-",region,realm,characterName)).addAttributeUpdate(new AttributeUpdate("newsDate").addNumeric(newsDate)));
    }
}
