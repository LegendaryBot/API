package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.stats.StatsHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class StatsTest {

    @Test
    public void testStat() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("graph", "TestGraph");
        jsonObject.put("value", 30.0);
        jsonArray.put(jsonObject);
        StatsHelper.addPoint(jsonArray.toString());
    }
}
