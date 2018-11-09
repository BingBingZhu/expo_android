package com.expo.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class VisitorService {

    public String codeid = "";
    public String coordinate_assist = "";
    public String counttrycode;
    public String createtime = "";
    public String dispose_type = "1";
    public String gps_latitude;
    public String gps_longitude;
    public int id = 0;
    public String img_url1 = "";
    public String img_url2 = "";
    public String img_url3 = "";
    public String lock_id = "";
    public String lock_name = "";
    public String lock_state = "";
    public String phone;
    public String platform = "1";
    public String servicetype;
    public String situation;
    public String state = "";
    public String updatetime = "";
    public String userid;
    public String username;
    public String complaintype = "";

    @Expose()
    private int times = 0;

    public synchronized void addTimes() {
        times++;
    }

    public int getTimes() {
        return times;
    }

    public Map toJson() {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        return gson.fromJson(gson.toJson(this), map.getClass());

    }
}
