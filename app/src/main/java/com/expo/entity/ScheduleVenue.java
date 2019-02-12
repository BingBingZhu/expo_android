package com.expo.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "schedule_venue")
public class ScheduleVenue {
    @DatabaseField(columnName = "_id", id = true)
    public Long iid;
    @DatabaseField(columnName = "id")
    @SerializedName("id")
    public Long id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    public String captionEn;
    @DatabaseField(columnName = "pic")
    @SerializedName("pic")
    public String pic;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    public String remarkEn;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    public String linkId;
    @DatabaseField(columnName = "gis_id")
    @SerializedName("gisid")
    public String gisId;
    @DatabaseField(columnName = "open_time")
    @SerializedName("opentime")
    public String openTime;
    @DatabaseField(columnName = "close_time")
    @SerializedName("closetime")
    public String closeTime;
    @DatabaseField(columnName = "before_day")
    @SerializedName("beforeday")
    public String beforeDay;
    @DatabaseField(columnName = "plan_id")
    @SerializedName("planid")
    public String planId;
    @DatabaseField(columnName = "online_state")
    @SerializedName("onlinestate")
    public String onlineState;
    @DatabaseField(columnName = "open_state")
    @SerializedName("openstate")
    public String openState;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;
    @DatabaseField(columnName = "date")
    public String date;
    public int percent;
}
