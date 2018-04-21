package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import com.serverless.ApiGatewayResponse;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.Map;

public class AWSGetGuildLatestLog implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        long guildId = Long.parseLong(pathParameters.get("guildId"));
        DiscordGuildHelper guildHelper = new DiscordGuildHelper();
        MessageEmbed latestLog = guildHelper.getLatestGuildLogs(guildHelper.getDiscordGuild(guildId));
        if (latestLog == null) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(404)
                    .build();
        } else {
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setRawBody(latestLog.toJSONObject().toString())
                    .build();
        }
    }
}
