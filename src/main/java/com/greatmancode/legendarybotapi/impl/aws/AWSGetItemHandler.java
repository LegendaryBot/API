package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.item.Item;
import com.greatmancode.legendarybotapi.item.ItemHelper;
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.serverless.ApiGatewayResponse;

import java.util.Arrays;
import java.util.Map;

public class AWSGetItemHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler.getHandler());
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        Arrays.toString(pathParameters.entrySet().toArray());
        String region = pathParameters.get("region");
        String id = pathParameters.get("id");
        Item item = ItemHelper.getItem(region,Long.parseLong(id));
        if (item == null) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(404)
                    .build();
        } else {
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setRawBody(item.getJson())
                    .build();
        }
    }
}
