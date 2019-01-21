package com.expo.entity;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vr_lable_info")
public class VrLableInfo {

    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    public String captionEn;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "_id", generatedId = true, allowGeneratedIdInsert = true)
    public int id;
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    public int idx;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    public int linkId;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;
}
