package com.greatmancode.legendarybotapi.queue;

import com.greatmancode.legendarybotapi.impl.aws.SQS.SQSHelper;

import java.util.Map;

public class QueueBackend {

    public static void sendMessage(String queue, String value, Map<String, String> metadata) {
        if (System.getenv("AWS_EXECUTION_ENV") != null) {
            SQSHelper.sendMessage(queue,value,metadata);
        }
    }
}
