package com.greatmancode.legendarybotapi.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LegionBuildingHelper {

    public static MessageEmbed getLegionBuildingStatus(String region) {
        if (!region.equalsIgnoreCase("eu") && !region.equalsIgnoreCase("us")) {
            return null;
        }
        EmbedBuilder builder = new EmbedBuilder();
        List<String> buildingStatus = new ArrayList<>();
        List<String> buildingStatusString = new ArrayList<>();
        List<String> buildingImage = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://www.wowhead.com/").get();
            Element element;
            if ("eu".equalsIgnoreCase(region)) {
                element = document.getElementsByClass("tiw-region tiw-region-EU").first();
            } else {
                element = document.getElementsByClass("tiw-region tiw-region-US tiw-show").first();
            }
            element.getElementsByClass("tiw-group tiw-bs-building").stream().forEach(building -> {
                String buildingImageTemp = building.getElementsByClass("tiw-bs-status").first().attributes().get("style");
                buildingImageTemp = buildingImageTemp.substring(23);
                buildingImageTemp = buildingImageTemp.substring(0,buildingImageTemp.length() - 1);
                buildingImageTemp = "https://" + buildingImageTemp;
                buildingImage.add(buildingImageTemp);
                buildingStatusString.add(building.getElementsByClass("imitation-heading heading-size-5").first().ownText());
                buildingStatus.add(building.getElementsByClass("tiw-bs-status-progress").first().getElementsByTag("span").first().ownText());
            });
            builder.setAuthor("Broken Shore Building Status");
            builder.addField("Mage Tower",buildingStatusString.get(0) + " " + buildingStatus.get(0), true);
            builder.addField("Command Center", buildingStatusString.get(1) + " " + buildingStatus.get(1), true);
            builder.addField("Nether Disruptor", buildingStatusString.get(2) + " " + buildingStatus.get(2), false);
            if (buildingStatusString.get(0).equalsIgnoreCase("Completed")) {
                builder.setImage(buildingImage.get(0));
            } else if (buildingStatusString.get(1).equalsIgnoreCase("Completed")) {
                builder.setImage(buildingImage.get(1));
            } else if (buildingStatusString.get(2).equalsIgnoreCase("Completed")) {
                builder.setImage(buildingImage.get(2));
            } else if (buildingStatusString.get(0).equalsIgnoreCase("Under Attack")) {
                builder.setImage(buildingImage.get(0));
            } else if (buildingStatusString.get(1).equalsIgnoreCase("Under Attack")) {
                builder.setImage(buildingImage.get(1));
            } else if (buildingStatusString.get(2).equalsIgnoreCase("Under Attack")) {
                builder.setImage(buildingImage.get(2));
            } else {
                builder.setImage(buildingImage.get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.isEmpty() ? null : builder.build();
    }
}
