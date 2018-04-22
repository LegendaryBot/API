package com.greatmancode.legendarybotapi.utils;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;

public class WoWUtils {

    private static final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .build();
    /**
     * Retrieve the {@link Color} of a World of Warcraft class.
     * @param className The class name to get the color from
     * @return A instance of {@link Color} representing the class color. Returns white if not found
     */
    public static Color getClassColor(String className) {
        Color color = null;
        String classNameLower = className.toLowerCase();
        switch (classNameLower) {
            case "death knight":
                color = new Color(196,30,59);
                break;
            case "demon hunter":
                color = new Color(163,48,201);
                break;
            case "druid":
                color = new Color(255,125,10);
                break;
            case "hunter":
                color = new Color(171,212,115);
                break;
            case "mage":
                color = new Color(105,204,240);
                break;
            case "monk":
                color = new Color(0,255,150);
                break;
            case "paladin":
                color = new Color(245,140,186);
                break;
            case "priest":
                color = new Color(255,255,255);
                break;
            case "rogue":
                color = new Color(255,245,105);
                break;
            case "shaman":
                color = new Color(0,112,222);
                break;
            case "warlock":
                color = new Color(148,130,201);
                break;
            case "warrior":
                color = new Color(199,156,110);
                break;
            default:
                color = Color.WHITE;
        }
        return color;
    }

    public static String getClassIcon(String className) {
        String url;
        String classNameLower = className.toLowerCase();
        switch (classNameLower) {
            case "death knight":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/e/e5/Ui-charactercreate-classes_deathknight.png";
                break;
            case "demon hunter":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/c/c9/Ui-charactercreate-classes_demonhunter.png";
                break;
            case "druid":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/6/6f/Ui-charactercreate-classes_druid.png";
                break;
            case "hunter":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/4/4e/Ui-charactercreate-classes_hunter.png";
                break;
            case "mage":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/5/56/Ui-charactercreate-classes_mage.png";
                break;
            case "monk":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/2/24/Ui-charactercreate-classes_monk.png";
                break;
            case "paladin":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/8/80/Ui-charactercreate-classes_paladin.png";
                break;
            case "priest":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/0/0f/Ui-charactercreate-classes_priest.png";
                break;
            case "rogue":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/b/b1/Ui-charactercreate-classes_rogue.png";
                break;
            case "shaman":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/3/3e/Ui-charactercreate-classes_shaman.png";
                break;
            case "warlock":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/c/cf/Ui-charactercreate-classes_warlock.png";
                break;
            case "warrior":
                url = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/3/37/Ui-charactercreate-classes_warrior.png";
                break;
            default:
                url = null;
                break;
        }
        return url;
    }

    public static String getRealmTimezone(String region, String realm) {
        HttpUrl url = new HttpUrl.Builder().scheme("https")
                .host(region + ".api.battle.net")
                .addPathSegments("/wow/realm/status")
                .addQueryParameter("realms", realm)
                .build();
        Request request = new Request.Builder().url(url).build();
        try {
            JSONObject jsonObject = new JSONObject(clientBattleNet.newCall(request).execute().body().string());
            if (jsonObject.has("code")) {
                return null;
            }
            JSONArray realmArray = jsonObject.getJSONArray("realms");
            return realmArray.getJSONObject(0).getString("timezone");
        } catch (IOException e) {
            e.printStackTrace();
            UncaughtExceptionHandler.getHandler().sendException(e,"region:" + region, "realm:" + realm);
        }
        return null;
    }
}
