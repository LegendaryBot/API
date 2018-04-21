package com.greatmancode.legendarybotapi.wow;

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


    private static OkHttpClient client = new OkHttpClient();


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
        }
        return null;
    }
}
