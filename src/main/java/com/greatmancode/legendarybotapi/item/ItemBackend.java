package com.greatmancode.legendarybotapi.item;

import com.greatmancode.legendarybotapi.impl.aws.dynamodb.DynamoDBHelper;

public class ItemBackend {


    public static Item getItem(long id) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            return DynamoDBHelper.getItem(id);
        }
        return null;
    }

    public static void saveItem(Item item) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            DynamoDBHelper.saveItem(item);
        }
    }
}
