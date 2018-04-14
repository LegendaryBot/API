package com.greatmancode.legendarybotapi.utils;

import java.util.Objects;

public class WoWCharacter {

    private final String region;
    private final String realm;
    private final String name;
    private final String guild;

    public WoWCharacter(String region, String realm, String name, String guild) {
        this.region = region;
        this.realm = realm;
        this.name = name;
        this.guild = guild;
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
