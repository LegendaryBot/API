package com.greatmancode.legendarybotapi.discordguild;

import org.json.JSONObject;

public class DiscordGuildHelper {


    public static DiscordGuild getDiscordGuild(long guildId) {
        DiscordGuild guild = DiscordGuildBackend.getDiscordGuild(guildId);
        if (guild == null) {
            guild = new DiscordGuildImpl();
            guild.setid(guildId);
            guild.setJson(new JSONObject().toString());
            DiscordGuildBackend.saveDiscordGuild(guild);
        }
        return guild;
    }

    public static void setSetting(DiscordGuild guild, String key, Object value) {
        JSONObject guildJSON = new JSONObject(guild.getJson());
        if (!guildJSON.has("settings")) {
            guildJSON.put("settings", new JSONObject());
        }
        guildJSON.getJSONObject("settings").put(key,value);
        guild.setJson(guildJSON.toString());
        DiscordGuildBackend.saveDiscordGuild(guild);
    }

    public static <T extends Object> T getSetting(DiscordGuild guild, String key) {
        T value = null;
        JSONObject guildJSON = new JSONObject(guild.getJson());
        if (guildJSON.has("settings") && guildJSON.getJSONObject("settings").has(key)) {
            value = (T) guildJSON.getJSONObject("settings").get(key);
        }
        return value;
    }

    public static void unsetSetting(DiscordGuild guild, String key) {
        JSONObject guildJSON = new JSONObject(guild.getJson());
        if (guildJSON.has("settings") && guildJSON.getJSONObject("settings").has(key)) {
            guildJSON.getJSONObject("settings").remove(key);
        }
        guild.setJson(guildJSON.toString());
        DiscordGuildBackend.saveDiscordGuild(guild);
    }
}
