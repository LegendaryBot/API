package com.greatmancode.legendarybotapi.impl.aws.SNS;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.Topic;

import java.util.concurrent.atomic.AtomicReference;

public class SNSHelper {

    private static AmazonSNS client = AmazonSNSClientBuilder.standard().withRegion(System.getenv("AWS_REGION") != null ? System.getenv("AWS_REGION") : "us-east-1").build();
    public static void sendMessage(String topicName, String message) {
        AtomicReference<Topic> topic = new AtomicReference<>();
        client.listTopics().getTopics().forEach(topicEntry -> {
            if (topicEntry.getTopicArn().contains(topicName)) {
                topic.set(topicEntry);
            }
        });
        if (topic.get() != null) {
            PublishRequest request = new PublishRequest(topic.get().getTopicArn(), message);
            client.publish(request);
        }
    }
}