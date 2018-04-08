package com.greatmancode.legendarybotapi.item;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;
import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBItem;

public class ItemBackend {


    public static Item getItem(int id) {
        DynamoDBHelper.createTable(DynamoDBItem.class);
        return DynamoDBHelper.getItem(id);
    }

    public static void saveItem(Item item) {
        if (!(item instanceof DynamoDBItem)) {
            DynamoDBItem dynamoDBItem = new DynamoDBItem();
            dynamoDBItem.setid(item.getid());
            dynamoDBItem.setJson(item.getJson());
            DynamoDBHelper.saveItem(dynamoDBItem);
        }
    }
}
