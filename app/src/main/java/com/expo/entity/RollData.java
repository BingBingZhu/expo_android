package com.expo.entity;

public class RollData {

    private String url;
    private String name;
    private long count;

    public RollData() {
    }

    public RollData(String url, String name, long count) {
        this.url = url;
        this.name = name;
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
