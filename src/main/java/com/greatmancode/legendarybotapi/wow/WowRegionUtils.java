package com.greatmancode.legendarybotapi.wow;

import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class WowRegionUtils {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .build();

    public static MessageEmbed getTokenPrice(String region) {
        String regionName = region.toUpperCase();
        Request webRequest = new Request.Builder().url("https://data.wowtoken.info/snapshot.json").build();


        try {
            String request = client.newCall(webRequest).execute().body().string();
            if (request == null) {
                return null;
            }
            JSONObject object = new JSONObject(request);
            if ("US".equalsIgnoreCase(regionName)) {
                regionName = "NA";
            }
            if (!object.has(regionName)) {
                return null;
            }
            JSONObject regionJSON = (JSONObject) object.get(regionName);
            JSONObject prices = (JSONObject) regionJSON.get("formatted");
            String price = (String) prices.get("buy");
            String minPrice = (String) prices.get("24min");
            String maxPrice = (String) prices.get("24max");
            double pctPrice = Double.parseDouble(prices.get("24pct").toString());
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Price for 1 WoW Token in the " + regionName + " region");
            eb.setThumbnail("http://wow.zamimg.com/images/wow/icons/large/wow_token01.jpg");
            eb.setColor(new Color(255,215,0));
            eb.addField("Current Price", price, true);
            eb.addField("Minimum 24H", minPrice, true);
            eb.addField("Maximum 24H", maxPrice, true);
            eb.addField("Percentage 24H range", pctPrice + "", true);
            eb.setFooter("Information taken from https://wowtoken.info/", "http://wow.zamimg.com/images/wow/icons/large/wow_token01.jpg");
            return eb.build();
        } catch (IOException e) {
            e.printStackTrace();
            UncaughtExceptionHandler.getHandler().sendException(e, "region:" + regionName);
        }
        return null;
    }
}
