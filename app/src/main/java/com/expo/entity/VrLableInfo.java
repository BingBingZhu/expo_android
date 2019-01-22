package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "vr_lable_info")
public class VrLableInfo implements Parcelable {

    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "_id", generatedId = true, allowGeneratedIdInsert = true)
    private Long id;
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    private int idx;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    private int linkId;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;

    public VrLableInfo() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaptionEn() {
        return captionEn;
    }

    public void setCaptionEn(String captionEn) {
        this.captionEn = captionEn;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    protected VrLableInfo(Parcel in) {
        caption = in.readString();
        captionEn = in.readString();
        createTime = in.readString();
        id = in.readLong();
        idx = in.readInt();
        linkId = in.readInt();
        updateTime = in.readString();
    }

    public static final Creator<VrLableInfo> CREATOR = new Creator<VrLableInfo>() {
        @Override
        public VrLableInfo createFromParcel(Parcel in) {
            return new VrLableInfo(in);
        }

        @Override
        public VrLableInfo[] newArray(int size) {
            return new VrLableInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(captionEn);
        dest.writeString(createTime);
        dest.writeLong(id);
        dest.writeInt(idx);
        dest.writeInt(linkId);
        dest.writeString(updateTime);
    }
}
