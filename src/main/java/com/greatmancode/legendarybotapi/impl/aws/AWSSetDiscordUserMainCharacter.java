package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.serverless.ApiGatewayResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class AWSSetDiscordUserMainCharacter implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        int userId = Integer.parseInt(pathParameters.get("userId"));
        int guildId = Integer.parseInt(pathParameters.get("guildId"));
        String region = pathParameters.get("region");
        String realm = pathParameters.get("realm");
        try {
            String character = character = URLDecoder.decode(pathParameters.get("character"), "UTF-8");
            boolean result = DiscordUserHelper.setGuildMainCharacter(DiscordUserHelper.getDiscordUser(userId), guildId,region,realm,character);
            return ApiGatewayResponse.builder()
                    .setStatusCode(result ? 200 : 404)
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ApiGatewayResponse.builder()
                    .setStatusCode(500).build();
        }

    }
}
