package com.greatmancode.legendarybotapi.overwatch;

import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONObject;

import java.io.IOException;

public class OverwatchHelper {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .build();

    public static MessageEmbed getOverwatchStats(String region, String battletag) {
        String regionName = region.toLowerCase();
        String user = battletag.substring(0, 1).toUpperCase() + battletag.substring(1);
        System.out.println("https://owapi.net/api/v3/u/"+user+"/stats");
        Request webRequest = new Request.Builder().url("https://owapi.net/api/v3/u/"+user+"/stats").build();

        try {
            String output = client.newCall(webRequest).execute().body().string();
            if (output == null) {
                return null;
            }
            JSONObject json = new JSONObject(output);
            if (json.has("error")) {
                return null;
            }
            if (!json.has(regionName)) {
                return null;
            }
            if (json.getJSONObject(regionName).getJSONObject("stats").isNull("competitive")) {
                return null;
            }

            JSONObject competitive = json.getJSONObject(regionName).getJSONObject("stats").getJSONObject("competitive");

            JSONObject competitiveStats = (JSONObject) competitive.get("overall_stats");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Player " + user + " Overwatch " + region.toUpperCase() + " Stats");
            eb.addField("Rank", competitiveStats.get("tier").toString() + "(" + competitiveStats.get("comprank").toString() +")", true);
            eb.addField("Wins", competitiveStats.get("wins").toString(), true);
            eb.addField("Losses", competitiveStats.get("losses").toString(), true);
            eb.addField("Ties", competitiveStats.get("ties").toString(), true);
            eb.addField("Win Rate", competitiveStats.get("win_rate") + "%", true);
            eb.setThumbnail(competitiveStats.get("avatar").toString());
            return eb.build();
        } catch (IOException |  NullPointerException e) {
            e.printStackTrace();
            UncaughtExceptionHandler.getHandler().sendException(e,"region:" + region, "battletag:" + battletag);
        }
        return null;
    }
}
