package com.expo.entity;

import com.blankj.utilcode.util.TimeUtils;
import com.expo.base.ExpoApp;
import com.expo.network.response.BaseResponse;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "footPrint")
public class FootPrint extends BaseResponse {
    @DatabaseField(columnName = "id", id = true)
    public String id;
    @DatabaseField(columnName = "lat")
    public Double lat;
    @DatabaseField(columnName = "lon")
    public Double lon;
    @DatabaseField(columnName = "time")
    public long time;
    @DatabaseField(columnName = "uid")
    public String uid;
    @DatabaseField(columnName = "times")
    public int times;

    public FootPrint() {
        time = TimeUtils.getNowMills();
        uid = ExpoApp.getApplication().getUser().getUid();
        times = 0;
    }

    public FootPrint(double lat, double lon) {
        this();
        this.lat = lat;
        this.lon = lon;
    }

}
