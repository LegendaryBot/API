package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import com.serverless.ApiGatewayResponse;

import java.util.Map;

public class AWSSetDiscordGuildSetting implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        long guildId = Long.parseLong(pathParameters.get("guildId"));
        String key = pathParameters.get("key");
        DiscordGuildHelper guildHelper = new DiscordGuildHelper();
        guildHelper.setSetting(guildHelper.getDiscordGuild(guildId),key, input.get("body"));
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .build();
    }
}
