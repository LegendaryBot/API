package com.greatmancode.legendarybotapi.discorduser;

public class DiscordUserImpl implements DiscordUser {

    private int id;
    private String json;

    public int getid() {return id;}
    public void setid(int id) {this.id = id;}

    public String getJson() {return json;}
    public void setJson(String json) {this.json = json;}

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", json='" + json + '\'' +
                '}';
    }

}
