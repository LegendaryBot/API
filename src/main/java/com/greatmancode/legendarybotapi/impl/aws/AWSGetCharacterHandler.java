package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.characters.CharacterHelper;
import com.serverless.ApiGatewayResponse;
import org.json.JSONObject;

import java.util.Map;

public class AWSGetCharacterHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        String serverName = pathParameters.get("realm");
        String region = pathParameters.get("region");
        String character = pathParameters.get("character");

        JSONObject result = CharacterHelper.getCharacterStatsEmbed(region, serverName, character);
        return ApiGatewayResponse.builder()
                .setStatusCode(result != null ? 200 : 404)
                .setRawBody(result.toString())
                .build();
    }
}
