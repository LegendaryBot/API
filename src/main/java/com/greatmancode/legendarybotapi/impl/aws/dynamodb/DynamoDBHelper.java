package com.greatmancode.legendarybotapi.impl.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.item.Item;

public class DynamoDBHelper {

    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(System.getenv("AWS_REGION")).build();
    private static DynamoDBMapper mapper = new DynamoDBMapper(client);
    public static void createTable(Class clazz) {
        if (clazz.isAnnotationPresent(DynamoDBTable.class)) {
            DynamoDBTable annotation = (DynamoDBTable) clazz.getAnnotation(DynamoDBTable.class);
            if (!client.listTables().getTableNames().contains(annotation.tableName())) {
                CreateTableRequest request = mapper.generateCreateTableRequest(clazz);
                request.setProvisionedThroughput(new ProvisionedThroughput(5L,5L));
                client.createTable(request);
            }
        }

    }

    public static Item getItem(int id) {
        return mapper.load(DynamoDBItem.class, id);
    }

    public static void saveItem(DynamoDBItem item) {
        mapper.save(item);
    }

    public static DiscordUser getDiscordUser(int id) {
        return mapper.load(DynamoDBDiscordUser.class, id);
    }

    public static void saveDiscordUser(DynamoDBDiscordUser discordUser) {
        mapper.save(discordUser);
    }
}
