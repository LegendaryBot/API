package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.legendarycheck.LegendaryCheckHandler;

public class AWSScheduleLegendaryCheck implements RequestHandler<Void,Void> {

    @Override
    public Void handleRequest(Void input, Context context) {
        LegendaryCheckHandler.handleLegendaryCheck();
        return null;
    }
}
