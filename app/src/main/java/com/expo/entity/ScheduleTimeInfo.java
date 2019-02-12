package com.expo.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "schedule_time_info")
public class ScheduleTimeInfo {
    @DatabaseField(columnName = "_id", id = true)
    public Long iid;
    @DatabaseField(columnName = "id")
    @SerializedName("id")
    public int id;
    @DatabaseField(columnName = "start_time")
    @SerializedName("starttime")
    public String startTime;
    @DatabaseField(columnName = "end_time")
    @SerializedName("endtime")
    public String endTime;
    @DatabaseField(columnName = "personal_count")
    @SerializedName("personalcount")
    public int personalCount;
    @DatabaseField(columnName = "personal_used_count")
    @SerializedName("personalusedcount")
    public int personalUsedCount;
    @DatabaseField(columnName = "personal_entryed_count")
    @SerializedName("personalentryedcount")
    public int personalEntryedCount;
    @DatabaseField(columnName = "personal_leaveed_count")
    @SerializedName("personalleaveedcount")
    public int personalLeaveedCount;
    @DatabaseField(columnName = "team_count")
    @SerializedName("teamcount")
    public int teamCount;
    @DatabaseField(columnName = "team_used_count")
    @SerializedName("teamusedcount")
    public int teamUsedCount;
    @DatabaseField(columnName = "team_entryed_count")
    @SerializedName("teamentryedcount")
    public int teamEntryedCount;
    @DatabaseField(columnName = "team_leaved_count")
    @SerializedName("teamleavedcount")
    public int teamLeavedCount;
    @DatabaseField(columnName = "plan_id")
    @SerializedName("planid")
    public String planId;
    @DatabaseField(columnName = "is_defalult")
    @SerializedName("isdefalult")
    public String isDefalult;
    @DatabaseField(columnName = "state")
    @SerializedName("state")
    public String state;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;

    @DatabaseField(columnName = "venue_id")
    public String venueId;
    @DatabaseField(columnName = "date")
    public String date;
}
