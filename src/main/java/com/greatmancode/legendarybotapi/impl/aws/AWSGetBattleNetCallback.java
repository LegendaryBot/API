package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.oauth.OAuthHelper;
import com.serverless.ApiGatewayResponse;

import java.util.Map;

public class AWSGetBattleNetCallback implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String,String> pathParameters = (Map<String,String>)input.get("queryStringParameters");
        String code = pathParameters.get("code");
        String state = pathParameters.get("state");
        OAuthHelper.handleBattleNetCallback(code,state);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .build();
    }
}
