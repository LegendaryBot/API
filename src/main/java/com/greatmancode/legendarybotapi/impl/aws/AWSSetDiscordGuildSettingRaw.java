package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.serverless.ApiGatewayResponse;
import org.json.JSONObject;

import java.util.Map;

public class AWSSetDiscordGuildSettingRaw implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler.getHandler());
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        long guildId = Long.parseLong(pathParameters.get("guildId"));
        DiscordGuildHelper guildHelper = new DiscordGuildHelper();
        guildHelper.setRawSetting(guildHelper.getDiscordGuild(guildId), new JSONObject((String)input.get("body")));
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .build();
    }
}
