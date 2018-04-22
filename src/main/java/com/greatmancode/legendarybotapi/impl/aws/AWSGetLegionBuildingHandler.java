package com.greatmancode.legendarybotapi.impl.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.greatmancode.legendarybotapi.general.LegionBuildingHelper;
import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.serverless.ApiGatewayResponse;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.Map;

public class AWSGetLegionBuildingHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler.getHandler());
        Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
        String region = pathParameters.get("region");
        MessageEmbed legionBuilding = LegionBuildingHelper.getLegionBuildingStatus(region);
        if (legionBuilding == null) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(404)
                    .build();
        } else {
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setRawBody(legionBuilding.toJSONObject().toString())
                    .build();
        }
    }
}
