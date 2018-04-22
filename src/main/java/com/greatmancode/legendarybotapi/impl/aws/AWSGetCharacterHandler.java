package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.characters.CharacterHelper;
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.serverless.ApiGatewayResponse;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class AWSGetCharacterHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler.getHandler());
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        String serverName = pathParameters.get("realm");
        String region = pathParameters.get("region");
        String character = null;
        try {
            character = URLDecoder.decode(pathParameters.get("character"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject result = CharacterHelper.getCharacterStatsEmbed(region, serverName, character);
        if (result == null) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(404)
                    .build();
        }
        return ApiGatewayResponse.builder()
                .setStatusCode(result != null ? 200 : 404)
                .setRawBody(result.toString())
                .build();
    }
}
