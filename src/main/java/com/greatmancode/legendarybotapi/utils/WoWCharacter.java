package com.greatmancode.legendarybotapi.utils;

import java.util.List;
import java.util.Objects;

public class WoWCharacter {

    private String region;
    private String realm;
    private String name;
    private String guild;
    private List<Long> mainCharacterForGuild;

    public WoWCharacter(String region, String realm, String name, String guild, List<Long> mainCharacterForGuild) {
        this.region = region;
        this.realm = realm;
        this.name = name;
        this.guild = guild;
        this.mainCharacterForGuild = mainCharacterForGuild;
    }

    public String getRegion() {
        return region;
    }

    public String getRealm() {
        return realm;
    }

    public String getName() {
        return name;
    }

    public String getGuild() {
        return guild;
    }

    public List<Long> getMainCharacterForGuild() {
        return mainCharacterForGuild;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WoWCharacter that = (WoWCharacter) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(realm, that.realm) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, realm, name);
    }

    @Override
    public String toString() {
        return "WoWCharacter{" +
                "region='" + region + '\'' +
                ", realm='" + realm + '\'' +
                ", name='" + name + '\'' +
                ", guild='" + guild + '\'' +
                '}';
    }
}
