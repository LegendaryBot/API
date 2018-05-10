package com.greatmancode.legendarybotapi.impl.aws.SQS;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;

public class SQSHelper {

    private static AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(System.getenv("AWS_REGION") != null ? System.getenv("AWS_REGION") : "us-east-1").build();

    public static void sendMessage(String queue, String value, Map<String,String> metadata) {
        Map<String, MessageAttributeValue> metadataAWS = new HashMap<>();
        metadata.forEach((k,v) -> metadataAWS.put(k, new MessageAttributeValue().withStringValue(v)));
        System.out.println(metadataAWS);
        String queueURL = sqs.getQueueUrl(queue).getQueueUrl();
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(value)
                .withMessageAttributes(metadataAWS);
        sqs.sendMessage(request);
    }
}
