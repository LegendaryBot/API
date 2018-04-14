package com.greatmancode.legendarybotapi.item;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;
import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBItem;

public class ItemBackend {


    public static Item getItem(int id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getItem(id);
        }
        return null;
    }

    public static void saveItem(Item item) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            if (!(item instanceof DynamoDBItem)) {
                DynamoDBItem dynamoDBItem = new DynamoDBItem();
                dynamoDBItem.setid(item.getid());
                dynamoDBItem.setJson(item.getJson());
                DynamoDBHelper.saveItem(dynamoDBItem);
            } else {
                DynamoDBHelper.saveItem((DynamoDBItem) item);
            }
        }
    }
}
