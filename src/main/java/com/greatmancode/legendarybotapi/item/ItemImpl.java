package com.greatmancode.legendarybotapi.item;

public class ItemImpl implements Item {

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
}
