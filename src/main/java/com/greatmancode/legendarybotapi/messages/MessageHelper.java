package com.greatmancode.legendarybotapi.messages;

import com.greatmancode.legendarybotapi.impl.aws.SNS.SNSHelper;

public class MessageHelper {

    public static void sendMessage(String name, String message) {
        SNSHelper.sendMessage(name,message);
    }
}
