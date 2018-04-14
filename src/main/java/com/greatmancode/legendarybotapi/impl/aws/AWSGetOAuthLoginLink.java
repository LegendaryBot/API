package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.oauth.OAuthHelper;
import com.serverless.ApiGatewayResponse;

import java.util.HashMap;
import java.util.Map;

public class AWSGetOAuthLoginLink implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        String region = pathParameters.get("region");
        long id = Long.parseLong(pathParameters.get("id"));
        Map<String,String> headers = new HashMap<>();
        headers.put("Location", OAuthHelper.generateLoginURL(region,id));
        return ApiGatewayResponse.builder()
                .setStatusCode(302)
                .setHeaders(headers)
                .build();
    }
}
