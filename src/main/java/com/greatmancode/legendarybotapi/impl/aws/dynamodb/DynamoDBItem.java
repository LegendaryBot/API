package com.greatmancode.legendarybotapi.impl.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.greatmancode.legendarybotapi.item.Item;

@DynamoDBTable(tableName = "LegendaryBot_item")
public class DynamoDBItem implements Item {

    private int id;
    private String json;

    @DynamoDBHashKey(attributeName = "id")
    public int getid() {return id;}
    public void setid(int id) {this.id = id;}

    @DynamoDBAttribute(attributeName = "json")
    public String getJson() {return json;}
    public void setJson(String json) {this.json = json;}

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", json='" + json + '\'' +
                '}';
    }
}
