package com.greatmancode.legendarybotapi.discorduser;

import com.greatmancode.legendarybotapi.utils.WoWCharacter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiscordUserImpl implements DiscordUser {

    private long id;
    private String json;

    public long getid() {return id;}
    public void setid(long id) {this.id = id;}

    public String getJson() {return json;}
    public void setJson(String json) {this.json = json;}

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", json='" + json + '\'' +
                '}';
    }

    @Override
    public List<WoWCharacter> getCharacters() {
        List<WoWCharacter> characterList = new ArrayList<>();
        JSONObject userJSON = new JSONObject(json);
        if (userJSON.has("characters")) {
            userJSON.getJSONArray("characters").forEach(characterEntry -> {
                JSONObject character = (JSONObject) characterEntry;
                List<Long> inGuild = new ArrayList<>();
                for (int i = 0; i < character.getJSONArray("mainCharacterForGuild").length(); i++) {
                    inGuild.add(character.getJSONArray("mainCharacterForGuild").getLong(i));
                }
                characterList.add(new WoWCharacter(character.getString("region"),character.getString("realm"),character.getString("name"), character.has("guild") ? character.getString("guild") : null, inGuild));
            });
        }
        return characterList;
    }

    @Override
    public void updateCharacters(List<WoWCharacter> characters) {
        JSONObject userJSON = new JSONObject(json);
        JSONArray characterArray = new JSONArray();
        characters.forEach(characterEntry -> {
            JSONObject character = new JSONObject();
            character.put("region", characterEntry.getRegion());
            character.put("realm", characterEntry.getRealm());
            character.put("name", characterEntry.getName());
            character.put("guild", characterEntry.getGuild());
            JSONArray inGuild = new JSONArray();
            characterEntry.getMainCharacterForGuild().forEach(inGuild::put);
            character.put("mainCharacterForGuild", inGuild);
            characterArray.put(character);
        });
        userJSON.put("characters", characterArray);
        json = userJSON.toString();
    }
}
