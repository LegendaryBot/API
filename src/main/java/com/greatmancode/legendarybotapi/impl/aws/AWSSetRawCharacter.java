package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.serverless.ApiGatewayResponse;

import java.util.Map;

public class AWSSetRawCharacter implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler.getHandler());
        DiscordUserHelper userHelper = new DiscordUserHelper();
        userHelper.rawSetMainCharacter((String)input.get("body"));
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .build();
    }
}
