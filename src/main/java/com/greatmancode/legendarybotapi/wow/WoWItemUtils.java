package com.greatmancode.legendarybotapi.wow;

import com.greatmancode.legendarybotapi.utils.HeroClass;
import com.greatmancode.legendarybotapi.utils.WoWArmorType;
import com.greatmancode.legendarybotapi.utils.WoWSlotType;
import com.greatmancode.legendarybotapi.utils.WoWUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.json.JSONObject;

public class WoWItemUtils {

    public static MessageEmbed buildEmbed(String character, HeroClass heroClass, String json) {
        JSONObject jsonObject = new JSONObject(json);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(jsonObject.getString("name"),"http://www.wowhead.com/item=" + jsonObject.getInt("id"));
        System.out.println("https://wow.zamimg.com/images/wow/icons/large/"+jsonObject.getString("icon") + ".jpg");
        builder.setThumbnail("https://wow.zamimg.com/images/wow/icons/large/"+jsonObject.getString("icon") + ".jpg");
        builder.setColor(WoWUtils.getClassColor(heroClass.name()));
        builder.setAuthor(character + " just looted the following legendary!", "http://www.wowhead.com/item=" + jsonObject.getInt("id"), WoWUtils.getClassIcon(heroClass.name()));
        StringBuilder stringBuilder = new StringBuilder();
        String slot = WoWSlotType.values()[jsonObject.getInt("inventoryType")].name().toLowerCase();
        slot = slot.substring(0,1).toUpperCase() + slot.substring(1);
        stringBuilder.append("**");
        stringBuilder.append(slot);
        if (jsonObject.getInt("itemSubClass") != 0) {
            String armorType = WoWArmorType.values()[jsonObject.getInt("itemSubClass")].name().toLowerCase();
            armorType = armorType.substring(0,1).toUpperCase() + armorType.substring(1);
            stringBuilder.append(" ");
            stringBuilder.append(armorType);
        }
        stringBuilder.append("**\n\n");

        if (jsonObject.has("itemSpells")) {
            if (jsonObject.getJSONObject("itemSpells").has("spell") && !jsonObject.getJSONObject("itemSpells").getJSONObject("spell").getString("description").equals("") ) {
                stringBuilder.append("**Equip:** ");
                stringBuilder.append(jsonObject.getJSONObject("itemSpells").getJSONObject("spell").getString("description"));
                stringBuilder.append("\n\n");
            }
        }

        //Hardcoded stuff because blizz does crap in his API
        switch (jsonObject.getInt("id")) {
            case 151801:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Finishing moves extend the duration of Tiger's Fury by 0.5 sec per combo point spent.");
                stringBuilder.append("\n\n");
                break;
            case 151823:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Ravager increases your movement speed by 10% and your damage done by 2% for 6 sec, increasing periodically and stacking up to 6 times\n" +
                        "\n" +
                        "Bladestorm increases your movement speed by 10% and your damage done by 2% for 6 sec, increasing periodically and stacking up to 6 times.");
                stringBuilder.append("\n\n");
                break;
            case 151787:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Prayer of Mending has a 15% chance to grant you Apotheosis for 8 sec.");
                stringBuilder.append("\n\n");
                break;
            case 151809:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Casting 30 Fireballs or Pyroblasts calls down a Meteor at your target.");
                stringBuilder.append("\n\n");
                break;
            case 151782:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Light of Dawn has a 10% chance to grant you Avenging Wrath for 8 sec.");
                stringBuilder.append("\n\n");
                break;
            case 151813:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Every 2 sec, gain 6% increased damage to your next Divine Storm, stacking up to 30 times.");
                stringBuilder.append("\n\n");
                break;
            case 137227:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Dire Beast reduces the remaining cooldown on Kill Command by 3 sec.");
                stringBuilder.append("\n\n");
                break;
            case 151644:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Gain one of the following talents based on your specialization:\n" +
                        "\n" +
                        "Holy: Divine Purpose\n" +
                        "Protection: Holy Shield\n" +
                        "Retribution: Divine Purpose");
                stringBuilder.append("\n\n");
                break;
            case 150936:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Gain the Vigor talent.");
                stringBuilder.append("\n\n");
                break;
            case 151819:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("You have a \n" +
                        "\n" +
                        "Enhancement\n" +
                        "0.12%\n" +
                        "\n" +
                        "Elemental\n" +
                        "0.10%\n" +
                        "\n" +
                        "chance per Maelstrom spent to gain Ascendance for 10 sec.");
                stringBuilder.append("\n\n");
                break;
            case 151812:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Eye of Tyr deals 300% increased damage and has 25% reduced cooldown.");
                stringBuilder.append("\n\n");
                break;
            case 151822:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Shield Block and Spell Reflection gain 1 additional charge.");
                stringBuilder.append("\n\n");
                break;
            case 151807:
                stringBuilder.append("**Equip:** ");
                stringBuilder.append("Gain 10% increased critical strike chance against enemies burning from your Explosive Trap.");
                stringBuilder.append("\n\n");
                break;
        }
        stringBuilder.append(jsonObject.getString("description"));
        builder.setDescription(stringBuilder);
        return builder.build();
    }
}
