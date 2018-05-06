package com.greatmancode.legendarybotapi.stats;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class StatsHelper {

    final static AmazonCloudWatch cw =
            AmazonCloudWatchClientBuilder.standard().withRegion(System.getenv("AWS_REGION") != null ? System.getenv("AWS_REGION") : "us-east-1").build();
    public static void addPoint(String json) {
        JSONArray jsonArray = new JSONArray(json);
        jsonArray.forEach(entry -> {
            JSONObject jsonObject = (JSONObject) entry;
            Dimension dimension = new Dimension()
                    .withName("Environment")
                    .withValue(System.getenv("STATS_DIMENSION") != null ? System.getenv("STATS_DIMENSION") : "dev");
            MetricDatum datum = new MetricDatum()
                    .withMetricName(jsonObject.getString("name"))
                    .withUnit(StandardUnit.Count)
                    .withValue(jsonObject.getDouble("value"))
                    .withDimensions(dimension);

            PutMetricDataRequest request = new PutMetricDataRequest()
                    .withNamespace("LegendaryBot")
                    .withMetricData(datum);

            cw.putMetricData(request);
        });

    }
}
