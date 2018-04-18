package com.greatmancode.legendarybotapi.tests;

import com.greatmancode.legendarybotapi.discordguild.DiscordGuild;
import com.greatmancode.legendarybotapi.discordguild.DiscordGuildHelper;
import com.greatmancode.legendarybotapi.discorduser.DiscordUser;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserBackend;
import com.greatmancode.legendarybotapi.discorduser.DiscordUserHelper;
import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class DiscordGuildTest {


    @Test
    public void testDiscordGuild() {
        DiscordGuildHelper guildHelper = new DiscordGuildHelper();
        DiscordGuild guild = guildHelper.getDiscordGuild(12345L);
        assertNotNull(guild);
        assertNull(guildHelper.getSetting(guild,"testsetting"));
        guildHelper.setSetting(guild, "testsetting", "teststring");
        assertEquals("teststring", guildHelper.getSetting(guild, "testsetting"));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1","value1");
        jsonObject.put("key2", "value2");
        guildHelper.setSetting(guild, "testjson", jsonObject);
        assertEquals("teststring", guildHelper.getSetting(guild, "testsetting"));
        assertTrue(jsonObject.similar(guildHelper.getSetting(guild, "testjson")));
        guildHelper.unsetSetting(guild, "testjson");
        assertNull(guildHelper.getSetting(guild, "testjson"));

    }

    @Test
    public void testRankSync() {
        DiscordUserHelper userHelper = new DiscordUserHelper();
        DiscordGuildHelper guildHelper = new DiscordGuildHelper();
        DiscordGuild guild = guildHelper.getDiscordGuild(1);
        guildHelper.setSetting(guild, "GUILD_NAME", "Legendary");
        guildHelper.setSetting(guild, "WOW_REALM_NAME", "arthas");
        guildHelper.setSetting(guild, "WOW_REGION_NAME", "us");

        JSONObject rankSettings = new JSONObject();
        rankSettings.put("1","Raider");
        rankSettings.put("2", "Member");
        rankSettings.put("0", "GuildMaster");
        guildHelper.setSetting(guild, "wowranks", rankSettings);

        List<Long> mainGuildID = new ArrayList<>();
        mainGuildID.add(1L);
        DiscordUser user = userHelper.getDiscordUser(1);
        DiscordUserBackend.saveDiscordUser(user);

        user = userHelper.getDiscordUser(2);
        List<WoWCharacter> characters = user.getCharacters();
        characters.add(new WoWCharacter("us","arthas", "memberReal", "Legendary", mainGuildID));
        user.updateCharacters(characters);
        DiscordUserBackend.saveDiscordUser(user);

        user = userHelper.getDiscordUser(3);
        characters = user.getCharacters();
        characters.add(new WoWCharacter("us","arthas", "raiderFake", "Legendary", mainGuildID));
        user.updateCharacters(characters);
        DiscordUserBackend.saveDiscordUser(user);

        user = userHelper.getDiscordUser(4);
        characters = user.getCharacters();
        characters.add(new WoWCharacter("us","arthas", "gmReal", "Legendary", mainGuildID));
        user.updateCharacters(characters);
        DiscordUserBackend.saveDiscordUser(user);

        user = userHelper.getDiscordUser(5);
        characters = user.getCharacters();
        characters.add(new WoWCharacter("us","arthas", "gmFake", "Legendary", mainGuildID));
        user.updateCharacters(characters);
        DiscordUserBackend.saveDiscordUser(user);

        user = userHelper.getDiscordUser(6);
        DiscordUserBackend.saveDiscordUser(user);

        user = userHelper.getDiscordUser(7);
        characters = user.getCharacters();
        characters.add(new WoWCharacter("us","arthas", "memberReal", "Legendary", mainGuildID));
        user.updateCharacters(characters);
        DiscordUserBackend.saveDiscordUser(user);

        JSONObject guildRankInput = new JSONObject();
        JSONObject guildJSON = new JSONObject();
        JSONArray ranks = new JSONArray();

        JSONObject rank = new JSONObject();
        rank.put("name", "Raider");
        rank.put("id", 12345);
        rank.put("position", 2);
        rank.put("managerole", false);
        ranks.put(rank);

        rank = new JSONObject();
        rank.put("name", "Member");
        rank.put("id", 5678);
        rank.put("position", 1);
        rank.put("managerole", false);
        ranks.put(rank);

        rank = new JSONObject();
        rank.put("name", "Guild Master");
        rank.put("id", 999);
        rank.put("position", 4);
        rank.put("managerole", true);
        ranks.put(rank);

        rank = new JSONObject();
        rank.put("name", "LegendaryBot");
        rank.put("id", 111);
        rank.put("position", 3);
        rank.put("managerole", true);
        ranks.put(rank);
        guildJSON.put("ranks", ranks);

        JSONArray botRank = new JSONArray();
        botRank.put("LegendaryBot");
        guildJSON.put("botranks", botRank);
        guildRankInput.put("guild", guildJSON);

        JSONArray userJSONArray = new JSONArray();

        //This user have no rank set and no character, so do nothing.
        JSONObject userJSON = new JSONObject();
        userJSON.put("discordId",1);
        userJSON.put("ranks", new JSONArray());
        userJSONArray.put(userJSON);

        //This user is a Member and should stay that way.
        userJSON = new JSONObject();
        userJSON.put("discordId",2);
        JSONArray userRanks = new JSONArray();
        userRanks.put("Member");
        userJSON.put("ranks", userRanks);
        userJSONArray.put(userJSON);

        //This user is a Raider but should be a Member instead
        userJSON = new JSONObject();
        userJSON.put("discordId", 3);
        userRanks = new JSONArray();
        userRanks.put("Raider");
        userJSON.put("ranks", userRanks);
        userJSONArray.put(userJSON);

        //This user is a Guild Master and should stay that way.
        userJSON = new JSONObject();
        userJSON.put("discordId", 4);
        userRanks = new JSONArray();
        userRanks.put("Guild Master");
        userJSON.put("ranks", userRanks);
        userJSONArray.put(userJSON);

        //This user is a Raider but have the GM rank. The bot should still do nothing since he can't modify the rank.
        userJSON = new JSONObject();
        userJSON.put("discordId", 5);
        userRanks = new JSONArray();
        userRanks.put("Guild Master");
        userJSON.put("ranks", userRanks);
        userJSONArray.put(userJSON);

        //This user have everything but he is nothing, It should remove everything except GM & LegendaryBot (Weird case)
        userJSON = new JSONObject();
        userJSON.put("discordId", 6);
        userRanks = new JSONArray();
        userRanks.put("Guild Master");
        userRanks.put("Member");
        userRanks.put("Raider");
        userRanks.put("LegendaryBot");
        userJSON.put("ranks", userRanks);
        userJSONArray.put(userJSON);

        //This user have a character but no rank set yet.
        userJSON = new JSONObject();
        userJSON.put("discordId", 7);
        userJSON.put("ranks", new JSONArray());
        userJSONArray.put(userJSON);

        guildRankInput.put("users", userJSONArray);

        DiscordGuildHelper mocking = spy(DiscordGuildHelper.class);
        Map<String, Integer> memberList = new HashMap<>();
        memberList.put("raiderFake", 2);
        memberList.put("memberReal", 2);
        memberList.put("gmReal", 0);
        memberList.put("gmFake", 1);
        when(mocking.getBattleNetMemberRank(guild)).thenReturn(memberList);
        JSONObject rankUpdateResult = mocking.processRankUpdate(guild, guildRankInput.toString());

        AtomicBoolean tested3 = new AtomicBoolean(false);
        AtomicBoolean tested5 = new AtomicBoolean(false);
        AtomicBoolean tested6 = new AtomicBoolean(false);
        AtomicBoolean tested7 = new AtomicBoolean(false);
        rankUpdateResult.getJSONArray("roleChange").forEach(roleChangeEntry -> {
            JSONObject roleChange = (JSONObject) roleChangeEntry;
            int discordId = roleChange.getInt("discordId");
            assertTrue(discordId == 3 || discordId == 5 || discordId == 6 || discordId == 7);
            if (discordId == 3) {
                tested3.set(true);
                assertEquals(1, roleChange.getJSONArray("ranksToRemove").length());
                assertEquals("Raider", roleChange.getJSONArray("ranksToRemove").get(0));
                assertEquals("Member", roleChange.getString("rankToAdd"));
            } else if (discordId == 5) {
                tested5.set(true);
                assertEquals("Raider", roleChange.getString("rankToAdd"));
                assertFalse(roleChange.has("ranksToRemove"));
            } else if (discordId == 6) {
                tested6.set(true);
                assertEquals(2, roleChange.getJSONArray("ranksToRemove").length());
                roleChange.getJSONArray("ranksToRemove").forEach(entry -> {
                    assertTrue(entry.equals("Raider") || entry.equals("Member"));
                });
            } else if (discordId == 7) {
                tested7.set(true);
                assertFalse(roleChange.has("ranksToRemove"));
                assertEquals("Member", roleChange.getString("rankToAdd"));
            }
        });
        assertTrue(tested3.get());
        assertTrue(tested5.get());
        assertTrue(tested6.get());
        assertTrue(tested7.get());
    }
}
