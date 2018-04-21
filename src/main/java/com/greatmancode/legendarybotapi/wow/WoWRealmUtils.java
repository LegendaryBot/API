package com.greatmancode.legendarybotapi.wow;

import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class WoWRealmUtils {

    private static final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .build();

    public static MessageEmbed getRealmStatus(String region, String serverName) {
        HttpUrl url = new HttpUrl.Builder().scheme("https")
                .host(region + ".api.battle.net")
                .addPathSegments("/wow/realm/status")
                .addQueryParameter("realms", serverName)
                .build();
        Request request = new Request.Builder().url(url).build();
        try {
            String result = clientBattleNet.newCall(request).execute().body().string();
            JSONObject object = new JSONObject(result);
            JSONArray realms = object.getJSONArray("realms");
            EmbedBuilder eb = new EmbedBuilder();
            if (realms.length() == 1) {
                JSONObject realm = realms.getJSONObject(0);
                if (realm.getBoolean("status")) {
                    eb.setColor(Color.GREEN);
                } else {
                    eb.setColor(Color.RED);
                }

                eb.setTitle(realm.getString("name") + " - " + region.toUpperCase());
                eb.addField("Status", realm.getBoolean("status") ? "Online" : "Offline", true);
                eb.addField("Population", realm.getString("population"), true);
                eb.addField("Currently a Queue?", realm.getBoolean("queue") ? "Yes" : "No", true);
                return eb.build();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
