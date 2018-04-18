package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.serverless.ApiGatewayResponse;
import org.json.JSONObject;

import java.util.Map;

public class AWSGetDiscordUserMainCharacter implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        long userId = Long.parseLong(pathParameters.get("userId"));
        long guildId = Long.parseLong(pathParameters.get("guildId"));
        DiscordUserHelper userHelper = new DiscordUserHelper();
        JSONObject mainCharacter = userHelper.getGuildMainCharacter(userHelper.getDiscordUser(userId), guildId);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setRawBody(mainCharacter.toString())
                .build();
    }

}
