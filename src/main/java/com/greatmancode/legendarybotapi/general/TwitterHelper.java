package com.greatmancode.legendarybotapi.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TwitterHelper {

    /**
     * Instance of the HTTP Client
     */
    private static OkHttpClient client = new OkHttpClient();

    /**
     * Retrieve the latest tweet of a username that is not a mention
     * @param region The region to fetch the blizardcs tweet from.
     * @return The latest tweet of the user.
     */
    public static MessageEmbed getLastTweet(String region) {
        if (region == null) {
            return null;
        }
        String username;
        if (region.equalsIgnoreCase("eu")) {
            username = "blizzardcseu_en";
        } else {
            username = "blizzardcs";
        }
        MessageEmbed result = null;
        byte[] key = Base64.getEncoder().encode((System.getenv("TWITTER_KEY") + ":" + System.getenv("TWITTER_SECRET")).getBytes());
        Request request = new Request.Builder()
                .url("https://api.twitter.com/oauth2/token")
                .post(RequestBody.create(MediaType.parse( "application/x-www-form-urlencoded;charset=UTF-8"), "grant_type=client_credentials"))
                .addHeader("Authorization","Basic " + new String(key))
                .build();

        try {
            String auth = client.newCall(request).execute().body().string(); //TODO use the oauth package?
            JSONObject authObject = new JSONObject(auth);
            if (authObject.has("token_type")) {
                String bearer = (String) authObject.get("access_token");
                HttpUrl url = new HttpUrl.Builder().scheme("https")
                        .host("api.twitter.com")
                        .addPathSegments("1.1/statuses/user_timeline.json")
                        .addQueryParameter("screen_name", username)
                        .addQueryParameter("exclude_replies", "1")
                        .addQueryParameter("count", "200")
                        .addQueryParameter("tweet_mode", "extended")
                        .build();
                request = new Request.Builder().url(url).addHeader("Authorization","Bearer " + bearer).build();
                String twitterTimeline = client.newCall(request).execute().body().string();
                JSONArray twitterObject = new JSONArray(twitterTimeline);
                JSONObject messageObject = twitterObject.getJSONObject(0);
                Date date = getTwitterDate((String)messageObject.get("created_at"));
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.setTimeZone(TimeZone.getTimeZone("America/Montreal"));
                EmbedBuilder builder = new EmbedBuilder();
                builder.setThumbnail(messageObject.getJSONObject("user").getString("profile_image_url_https"));
                if (messageObject.getJSONObject("entities").has("media")) {
                    builder.setImage(messageObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url_https"));
                }
                builder.setAuthor(messageObject.getJSONObject("user").getString("screen_name"), "https://twitter.com/"+username);
                builder.setTimestamp(cal.toInstant());
                builder.setDescription(messageObject.getString("full_text"));
                result = builder.build();
            }
        } catch (JSONException | java.text.ParseException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Convert a Twitter formatted date to a Java format
     * @param date The twitter date
     * @return a {@link Date} instance of the twitter date
     * @throws java.text.ParseException If we can't parse the date
     */
    public static Date getTwitterDate(String date) throws java.text.ParseException {

        final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return sf.parse(date);
    }
}
