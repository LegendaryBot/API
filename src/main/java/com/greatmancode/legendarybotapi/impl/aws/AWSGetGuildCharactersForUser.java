package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.serverless.ApiGatewayResponse;
import org.json.JSONArray;

import java.util.Map;

public class AWSGetGuildCharactersForUser implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        long userId = Long.parseLong(pathParameters.get("userId"));
        String guildName = pathParameters.get("guildName");
        DiscordUserHelper userHelper = new DiscordUserHelper();
        JSONArray characters = userHelper.getGuildCharactersForUser(userHelper.getDiscordUser(userId), guildName);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setRawBody(characters.toString())
                .build();
    }
}
