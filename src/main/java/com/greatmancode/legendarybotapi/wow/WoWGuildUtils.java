package com.greatmancode.legendarybotapi.wow;

import com.greatmancode.legendarybotapi.utils.UncaughtExceptionHandler;
import com.greatmancode.legendarybotapi.utils.WoWUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class WoWGuildUtils {


    private static final OkHttpClient client = new OkHttpClient.Builder()
            .build();


    public static MessageEmbed getLastestWarcraftLog(String region, String realm, String guild) {
        EmbedBuilder eb = new EmbedBuilder();
        HttpUrl url = new HttpUrl.Builder().scheme("https")
                .host("www.warcraftlogs.com")
                .addPathSegments("v1/reports/guild/"+guild+"/"+ realm + "/" + region)
                .addQueryParameter("api_key",System.getenv("WARCRAFTLOGS_KEY"))
                .build();
        Request webRequest = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(webRequest).execute();
            if (response.code() != 200) {
                return null;
            }
            JSONArray jsonArray = new JSONArray(response.body().string());
            if (jsonArray.length() == 0) {
                return null;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1);
            Calendar calendar = Calendar.getInstance();
            String timezone = WoWUtils.getRealmTimezone(region, realm);
            calendar.setTimeZone(timezone != null ? TimeZone.getTimeZone(timezone) : TimeZone.getTimeZone("America/Montreal"));
            calendar.setTimeInMillis(jsonObject.getLong("start"));
            eb.setTitle(jsonObject.getString("title"), "https://www.warcraftlogs.com/reports/" + jsonObject.getString("id"));
            eb.setTimestamp(calendar.toInstant());
            eb.setThumbnail("https://www.warcraftlogs.com/img/icons/warcraft/zone-" + jsonObject.getInt("zone") + "-small.jpg");
            eb.addField("Created by", jsonObject.getString("owner"), true);

            url = new HttpUrl.Builder().scheme("https")
                    .host("www.warcraftlogs.com")
                    .addPathSegments("v1/zones")
                    .addQueryParameter("api_key",System.getenv("WARCRAFTLOGS_KEY"))
                    .build();
            webRequest = new Request.Builder().url(url).build();
            response = client.newCall(webRequest).execute();
            if (response.code() == 200) {
                JSONArray zonesArray = new JSONArray(response.body().string());
                for (Object zoneEntry : zonesArray) {
                    JSONObject zone = (JSONObject) zoneEntry;
                    if (zone.getInt("id") == jsonObject.getInt("zone")) {
                        eb.addField("Zone", zone.getString("name"), true);
                        break;
                    }
                }
            }
            eb.setColor(new Color(40,103,40));
            return eb.build();
        } catch (IOException e) {
            e.printStackTrace();
            UncaughtExceptionHandler.getHandler().sendException(e,"region:" + region, "realm:" + realm, "guild:" + guild);
        }
        return null;
    }

    public static MessageEmbed getRaiderIORank(String region, String realmName, String guild) {
        //https://raider.io/api/v1/guilds/profile
        HttpUrl url = new HttpUrl.Builder()
                .host("raider.io")
                .scheme("https")
                .addPathSegments("api/v1/guilds/profile")
                .addQueryParameter("region", region)
                .addQueryParameter("realm", realmName)
                .addQueryParameter("name", guild)
                .addQueryParameter("fields", "raid_rankings")
                .build();
        Request request = new Request.Builder().url(url).build();
        String result;
        try {
            result = client.newCall(request).execute().body().string();
            if ("null".equals(result)) {
                return null;
            }
            JSONObject obj = new JSONObject(result);

            if (obj.has("error")) {
                return null;
            }
            String realm = obj.getString("realm");
            JSONObject raidRankings = obj.getJSONObject("raid_rankings");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(guild + "-" + realm + " Raid Rankings");
            JSONObject theNighthold = raidRankings.getJSONObject("the-nighthold");
            JSONObject theEmeraldNightmare = raidRankings.getJSONObject("the-emerald-nightmare");
            JSONObject trialOfValor = raidRankings.getJSONObject("trial-of-valor");
            JSONObject tombOfSargeras = raidRankings.getJSONObject("tomb-of-sargeras");
            JSONObject antorusTheBurningThrone = raidRankings.getJSONObject("antorus-the-burning-throne");
            eb.addField("Antorus The Burning Throne", formatRanking(antorusTheBurningThrone), true);
            eb.addField("Tomb of Sargeras", formatRanking(tombOfSargeras), true);
            eb.addField("The Nighthold", formatRanking(theNighthold), true);
            eb.addField("Trial of Valor", formatRanking(trialOfValor), true);
            eb.addField("The Emerald Nightmare", formatRanking(theEmeraldNightmare), true);
            return eb.build();

        } catch (IOException e) {
            e.printStackTrace();
            UncaughtExceptionHandler.getHandler().sendException(e, "region:" + region, "realm:" + realmName, "guild:" + guild);
        }
        return null;
    }

    private static String formatRanking(JSONObject json) {
        JSONObject normal = json.getJSONObject("normal");
        JSONObject heroic = json.getJSONObject("heroic");
        JSONObject mythic = json.getJSONObject("mythic");
        StringBuilder builder = new StringBuilder();
        if (normal.getInt("world") != 0 && heroic.getInt("world") == 0 && mythic.getInt("world") == 0) {
            builder.append("**Normal**\n");
            subFormatRanking(normal, builder);
        } else if (heroic.getInt("world") != 0 && mythic.getInt("world") == 0) {
            builder.append("\n**Heroic**\n");
            subFormatRanking(heroic,builder);
        } else if (mythic.getInt("world") != 0){
            builder.append("\n**Mythic**\n");
            subFormatRanking(mythic, builder);
        }






        return builder.toString();
    }

    private static void subFormatRanking(JSONObject difficulty, StringBuilder builder) {
        if (difficulty.getInt("world") != 0) {
            builder.append("World: **" + difficulty.getInt("world") + "**\n");
            builder.append("Region: **" + difficulty.getInt("region") + "**\n");
            builder.append("Realm: **" + difficulty.getInt("realm") + "**\n");
        } else {
            builder.append("**Not started**\n");
        }

    }
}
