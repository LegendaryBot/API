package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.greatmancode.legendarybotapi.legendarycheck.LegendaryCheckHandler;

import java.io.IOException;

public class AWSHandleLegendaryCheck implements RequestHandler<SNSEvent,Void> {
    @Override
    public Void handleRequest(SNSEvent input, Context context) {
        String message = input.getRecords().get(0).getSNS().getMessage();
        System.out.println("SNS MESSAGE:" + message);
        try {
            LegendaryCheckHandler.handleGuildLegendaryCheck(Long.parseLong(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
