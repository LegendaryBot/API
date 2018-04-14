package com.greatmancode.legendarybotapi.characters;

import com.greatmancode.legendarybotapi.utils.BattleNetAPIInterceptor;
import com.greatmancode.legendarybotapi.utils.HeroClass;
import com.greatmancode.legendarybotapi.utils.HeroRace;
import com.greatmancode.legendarybotapi.utils.WoWUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class CharacterHelper {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .build();

    private static final OkHttpClient clientBattleNet = new OkHttpClient.Builder()
            .addInterceptor(new BattleNetAPIInterceptor())
            .connectionPool(new ConnectionPool(300, 1, TimeUnit.SECONDS))
            .build();

    public static JSONObject getCharacterStatsEmbed(String region, String realm, String character) {
        JSONObject responseBody = null;
        try {
            String serverSlug = realm; //TODO real slug support

            HttpUrl url = new HttpUrl.Builder().scheme("https")
                    .host("raider.io")
                    .addPathSegments("api/v1/characters/profile")
                    .addQueryParameter("region", region)
                    .addQueryParameter("realm", serverSlug)
                    .addQueryParameter("name", character)
                    .addQueryParameter("fields", "gear,raid_progression,mythic_plus_scores,previous_mythic_plus_scores,mythic_plus_best_runs")
                    .build();
            Request request = new Request.Builder().url(url).build();
            String result = null;
            try {
                result = client.newCall(request).execute().body().string();
            } catch (SocketTimeoutException e)  {

            }
            if (result != null) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    jsonObject = new JSONObject();
                    jsonObject.put("error","error");
                }

                if (!jsonObject.has("error")) {

                    EmbedBuilder eb = new EmbedBuilder();
                    if (jsonObject.getString("name").equals("Pepyte") && jsonObject.getString("realm").equals("Arthas")) {
                        eb.setThumbnail("https://lumiere-a.akamaihd.net/v1/images/b5e11dc889c5696799a6bd3ec5d819c1f7dfe8b4.jpeg");
                    } else if (jsonObject.getString("name").equals("Xdntgivitoya") && jsonObject.getString("realm").equals("Arthas")) {
                        eb.setThumbnail("https://cdn.discordapp.com/attachments/239729214004133889/389452254823841792/20171210_112359.jpg");
                    } else {
                        eb.setThumbnail(jsonObject.getString("thumbnail_url"));
                    }

                    String className = jsonObject.getString("class").toLowerCase();
                    eb.setColor(WoWUtils.getClassColor(className));

                    StringBuilder titleBuilder = new StringBuilder();
                    titleBuilder.append(jsonObject.get("name"));
                    titleBuilder.append(" ");
                    titleBuilder.append(jsonObject.get("realm"));
                    titleBuilder.append(" - ");
                    titleBuilder.append(((String) jsonObject.get("region")).toUpperCase());
                    titleBuilder.append(" | ");
                    titleBuilder.append(jsonObject.get("race"));
                    titleBuilder.append(" ");
                    titleBuilder.append(jsonObject.get("active_spec_name"));
                    titleBuilder.append(" ");
                    titleBuilder.append(jsonObject.get("class"));
                    String wowLink = null;
                    if (((String) jsonObject.get("region")).equalsIgnoreCase("us")) {
                        wowLink = "https://worldofwarcraft.com/en-us/character/" + serverSlug + "/" + jsonObject.get("name");
                    } else {
                        wowLink = "https://worldofwarcraft.com/en-gb/character/" + serverSlug + "/" + jsonObject.get("name");
                    }
                    eb.setAuthor(titleBuilder.toString(), wowLink, WoWUtils.getClassIcon(className));


                    StringBuilder progressionBuilder = new StringBuilder();
                    JSONObject raidProgression = jsonObject.getJSONObject("raid_progression");
                    JSONObject emeraldNightmare = raidProgression.getJSONObject("the-emerald-nightmare");
                    JSONObject trialOfValor = raidProgression.getJSONObject("trial-of-valor");
                    JSONObject theNighthold = raidProgression.getJSONObject("the-nighthold");
                    JSONObject tombOfSargeras = raidProgression.getJSONObject("tomb-of-sargeras");
                    JSONObject antorus = raidProgression.getJSONObject("antorus-the-burning-throne");
                    progressionBuilder.append("**EN**: ");
                    progressionBuilder.append(emeraldNightmare.get("summary"));
                    progressionBuilder.append(" - ");
                    progressionBuilder.append("**ToV**:");
                    progressionBuilder.append(trialOfValor.get("summary"));
                    progressionBuilder.append(" - ");
                    progressionBuilder.append("**NH**: ");
                    progressionBuilder.append(theNighthold.get("summary"));
                    progressionBuilder.append(" - ");
                    progressionBuilder.append("**ToS**: ");
                    progressionBuilder.append(tombOfSargeras.get("summary"));
                    progressionBuilder.append(" - ");
                    progressionBuilder.append("**ABT**: ");
                    progressionBuilder.append(antorus.get("summary"));
                    eb.addField("Progression", progressionBuilder.toString(), false);


                    //We fetch the Battle.net achivement record
                    HttpUrl battleneturl = new HttpUrl.Builder().scheme("https")
                            .host(region + ".api.battle.net")
                            .addPathSegments("wow/character/"+serverSlug+"/" +character)
                            .addQueryParameter("fields", "achievements,stats")
                            .build();
                    Request battlenetRequest = new Request.Builder().url(battleneturl).build();
                    String battlenetResult = clientBattleNet.newCall(battlenetRequest).execute().body().string();
                    String apAmount = getAP(battlenetResult);

                    JSONObject gear = jsonObject.getJSONObject("gear");
                    eb.addField("iLVL", gear.get("item_level_equipped") + "/" + gear.get("item_level_total"), true);

                    if (apAmount != null) {
                        eb.addField("Artifact Power", gear.get("artifact_traits").toString() + " / " + apAmount + " " + "AP Gathered", true);
                    } else {
                        eb.addField("Artifact Power", gear.get("artifact_traits").toString(), true);
                    }




                    JSONObject mplusRank = jsonObject.getJSONObject("mythic_plus_scores");
                    eb.addField("Mythic+ Score", mplusRank.get("all").toString(), true);
                    JSONObject lastMplusRank = jsonObject.getJSONObject("previous_mythic_plus_scores");
                    double lastSeasonScore = lastMplusRank.getDouble("all");
                    double currentSeasonScore = mplusRank.getDouble("all");
                    if (lastSeasonScore > currentSeasonScore || lastSeasonScore == 0) {
                        eb.addField("Last Season M+ Score", lastMplusRank.get("all").toString(), true);
                    } else {
                        //Field Unused. Let's put something else.
                        JSONObject battleNetJSON = new JSONObject(battlenetResult);
                        JSONObject statsJSON = battleNetJSON.getJSONObject("stats");
                        //TODO Translate this
                        StringBuilder statsBuilder = new StringBuilder();
                        long str = statsJSON.getLong("str");
                        long agi = statsJSON.getLong("agi");
                        long intel = statsJSON.getLong("int");
                        if (str > agi && str > intel) {
                            statsBuilder.append("**STR**: ");
                            statsBuilder.append(str);
                        } else if (agi > str && agi > intel) {
                            statsBuilder.append("**AGI**: ");
                            statsBuilder.append(agi);
                        } else {
                            statsBuilder.append("**INT**: ");
                            statsBuilder.append(intel);
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("00.##");
                        statsBuilder.append(" - ");
                        statsBuilder.append("**Crit**: ");
                        statsBuilder.append(decimalFormat.format(statsJSON.get("crit")));
                        statsBuilder.append("%");
                        statsBuilder.append(" - ");
                        statsBuilder.append("**Haste**: ");
                        statsBuilder.append(decimalFormat.format(statsJSON.get("haste")));
                        statsBuilder.append("%");
                        statsBuilder.append("\n");
                        statsBuilder.append("**Mastery**: ");
                        statsBuilder.append(decimalFormat.format(statsJSON.get("mastery")));
                        statsBuilder.append("%");
                        statsBuilder.append(" - ");
                        statsBuilder.append("**Vers**: ");
                        statsBuilder.append(decimalFormat.format(statsJSON.get("versatilityDamageDoneBonus")));
                        statsBuilder.append("%");

                        eb.addField("Stats", statsBuilder.toString(), true);
                    }



                    StringBuilder runsBuilder = new StringBuilder();
                    JSONArray bestRuns = jsonObject.getJSONArray("mythic_plus_best_runs");
                    for (Object runObject : bestRuns) {
                        JSONObject run = (JSONObject) runObject;
                        runsBuilder.append("[");
                        runsBuilder.append(run.get("dungeon"));
                        runsBuilder.append(" **+");
                        runsBuilder.append(run.get("mythic_level"));
                        runsBuilder.append("**](");
                        runsBuilder.append(run.get("url"));
                        runsBuilder.append(")\n");
                        long time = run.getLong("clear_time_ms");
                        long hours = TimeUnit.MILLISECONDS.toHours(time);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time));
                        runsBuilder.append("    ");
                        if (hours >= 1) {
                            runsBuilder.append(hours + " Hour(s) " +  minutes + " Minute(s) " +  seconds + " Second(s)");
                        } else {
                            runsBuilder.append(minutes + " Minute(s) " +  seconds + " Second(s)");
                        }
                        runsBuilder.append(" | ");
                        runsBuilder.append(run.get("num_keystone_upgrades").toString() + " ");
                        runsBuilder.append("Chest(s)");
                        runsBuilder.append("\n");
                    }
                    eb.addField("Best Mythic+ Runs", runsBuilder.toString(),    true);
                    long m5 = getM5(battlenetResult);
                    long m10 = getM10(battlenetResult);
                    long m15 = getM15(battlenetResult);
                    StringBuilder completedBuilder = new StringBuilder();
                    completedBuilder.append("**M+5**: " + m5 + "\n");
                    completedBuilder.append("**M+10**: " + m10 + "\n");
                    completedBuilder.append("**M+15**: " + m15 + "\n");
                    eb.addField("Mythic+ Completed", completedBuilder.toString(), true);
                    eb.addField("WoWProgress", "[Click Here](https://www.wowprogress.com/character/"+region.toLowerCase()+"/"+serverSlug+"/"+jsonObject.get("name") + ")", true);
                    eb.addField("Raider.IO", "[Click Here](https://raider.io/characters/"+region.toLowerCase()+"/"+serverSlug+"/"+jsonObject.get("name") + ")", true);
                    eb.addField("WarcraftLogs","[Click Here](https://www.warcraftlogs.com/character/"+region.toLowerCase()+"/"+serverSlug+"/"+jsonObject.get("name") + ")", true);
                    eb.setFooter("Information taken from Raider.IO",null);

                    responseBody = eb.build().toJSONObject();
                } else {

                    //We got an error from raider.io, maybe he was never added to the site. Let's try through battle.net
                    HttpUrl battleneturl = new HttpUrl.Builder().scheme("https")
                            .host(region + ".api.battle.net")
                            .addPathSegments("wow/character/"+serverSlug+"/" +character)
                            .addQueryParameter("fields", "progression,items,achievements")
                            .build();
                    Request battlenetRequest = new Request.Builder().url(battleneturl).build();
                    String battlenetResult = clientBattleNet.newCall(battlenetRequest).execute().body().string();

                    JSONObject battleNetObject = new JSONObject(battlenetResult);
                    if (battleNetObject.has("status")) {
                        //Error
                        return null;
                    }

                    StringBuilder titleBuilder = new StringBuilder();
                    titleBuilder.append(battleNetObject.get("name"));
                    titleBuilder.append(" ");
                    titleBuilder.append(battleNetObject.get("realm"));
                    titleBuilder.append(" - ");
                    titleBuilder.append(region.toUpperCase());
                    titleBuilder.append(" | ");
                    url = new HttpUrl.Builder().scheme("https")
                            .host(region + ".api.battle.net")
                            .addPathSegments("wow/data/character/races")
                            .build();
                    battlenetRequest = new Request.Builder().url(url).build();
                    String racesResult = clientBattleNet.newCall(battlenetRequest).execute().body().string();
                    JSONObject raceObject = new JSONObject(racesResult);
                    JSONArray raceArray = raceObject.getJSONArray("races");
                    for (int i = 0; i < raceArray.length(); i++) {
                        JSONObject raceEntry = raceArray.getJSONObject(i);
                        if (raceEntry.getInt("id") == battleNetObject.getInt("race")) {
                            titleBuilder.append(raceEntry.getString("name"));
                            titleBuilder.append(" ");
                        }
                    }

                    titleBuilder.append(HeroClass.values()[battleNetObject.getInt("class")]);
                    titleBuilder.append(" ");

                    String wowLink = null;

                    if (region.equalsIgnoreCase("us")) {
                        wowLink = "https://worldofwarcraft.com/en-us/character/" + serverSlug + "/" + battleNetObject.get("name");
                    } else {
                        wowLink = "https://worldofwarcraft.com/en-gb/character/" + serverSlug + "/" + battleNetObject.get("name");
                    }

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(titleBuilder.toString(), wowLink);
                    eb.setThumbnail("http://render-" + region.toLowerCase() + ".worldofwarcraft.com/character/" + battleNetObject.get("thumbnail"));
                    String apAmount = getAP(battlenetResult);
                    JSONObject gear = battleNetObject.getJSONObject("items");
                    eb.addField("iLVL", gear.get("averageItemLevelEquipped") + "/" + gear.get("averageItemLevel"), true);

                    if (apAmount != null) {
                        eb.addField("Artifact Power", apAmount + " AP Gathered", true);
                    }
                    long m5 = getM5(battlenetResult);
                    long m10 = getM10(battlenetResult);
                    long m15 = getM15(battlenetResult);
                    StringBuilder completedBuilder = new StringBuilder();
                    completedBuilder.append("**M+5**: " + m5 + "\n");
                    completedBuilder.append("**M+10**: " + m10 + "\n");
                    completedBuilder.append("**M+15**: " + m15 + "\n");
                    eb.addField("Mythic+ Completed", completedBuilder.toString(), false);
                    eb.addField("WoWProgress", "[Click Here](https://www.wowprogress.com/character/"+region.toLowerCase()+"/"+serverSlug+"/"+battleNetObject.get("name") + ")", true);
                    eb.addField("Raider.IO", "[Click Here](https://raider.io/characters/"+region.toLowerCase()+"/"+serverSlug+"/"+battleNetObject.get("name") + ")", true);
                    eb.addField("WarcraftLogs","[Click Here](https://www.warcraftlogs.com/character/"+region.toLowerCase()+"/"+serverSlug+"/"+battleNetObject.get("name") + ")", true);
                    eb.addField("Information", "Character not found on Raider.IO", false);
                    eb.setFooter("Information taken from Battle.Net",null);

                    responseBody = eb.build().toJSONObject();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO error handling
        }
        return responseBody;
    }

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "millions");
        suffixes.put(1_000_000_000L, "billions");
        suffixes.put(1_000_000_000_000L, "trillions");
        suffixes.put(1_000_000_000_000_000L, "quadrillions");
        suffixes.put(1_000_000_000_000_000_000L, "quintillions");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format( -value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + " " +  suffix : (truncated / 10) + " " + suffix;
    }

    public static String getAP(String json){
        long apAmount = -1;
        if (json != null) {
            JSONObject battleNetCharacter = new JSONObject(json);
            JSONObject achivements = battleNetCharacter.getJSONObject("achievements");
            JSONArray criteriaObject = achivements.getJSONArray("criteria");
            int criteriaNumber = -1;
            for (int i = 0; i < criteriaObject.length(); i++) {
                if (criteriaObject.getInt(i) == 30103) {
                    criteriaNumber = i;
                }
            }

            if (criteriaNumber != -1) {
                apAmount = (long) achivements.getJSONArray("criteriaQuantity").get(criteriaNumber);
            }
        }
        String result = null;
        if (apAmount != -1) {
            result = format(apAmount);
        }
        return result;

    }

    public static long getM5(String json){
        long m5 = 0;
        if (json != null) {
            JSONObject battleNetCharacter = new JSONObject(json);
            JSONObject achivements = battleNetCharacter.getJSONObject("achievements");
            JSONArray criteriaObject = achivements.getJSONArray("criteria");
            int criteriaNumber = -1;
            for (int i = 0; i < criteriaObject.length(); i++) {
                if (criteriaObject.getInt(i) == 33097) {
                    criteriaNumber = i;
                }
            }

            if (criteriaNumber != -1) {
                m5 = achivements.getJSONArray("criteriaQuantity").getLong(criteriaNumber);
                if (m5 >= 1) {
                    m5 += 1;
                }
            }
        }
        return m5;
    }

    public static long getM10(String json) {
        long m10 = 0;
        if (json != null) {
            JSONObject battleNetCharacter = new JSONObject(json);
            JSONObject achivements = battleNetCharacter.getJSONObject("achievements");
            JSONArray criteriaObject = achivements.getJSONArray("criteria");
            int criteriaNumber = -1;
            for (int i = 0; i < criteriaObject.length(); i++) {
                if (criteriaObject.getInt(i) == 33098) {
                    criteriaNumber = i;
                }
            }

            if (criteriaNumber != -1) {
                m10 = achivements.getJSONArray("criteriaQuantity").getLong(criteriaNumber);
                if (m10 >= 1) {
                    m10 += 1;
                }
            }
        }
        return m10;
    }

    public static long getM15(String json) {
        long m15 = 0;
        if (json != null) {
            JSONObject battleNetCharacter = new JSONObject(json);
            JSONObject achivements = battleNetCharacter.getJSONObject("achievements");
            JSONArray criteriaObject = achivements.getJSONArray("criteria");
            int criteriaNumber = -1;
            for (int i = 0; i < criteriaObject.length(); i++) {
                if (criteriaObject.getInt(i) == 32028 ) {
                    criteriaNumber = i;
                }
            }

            if (criteriaNumber != -1) {
                m15 = achivements.getJSONArray("criteriaQuantity").getLong(criteriaNumber);
                if (m15 >= 1) {
                    m15 += 1;
                }
            }
        }
        return m15;
    }
}
