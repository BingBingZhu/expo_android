package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "schedule_order")
public class Schedule implements Parcelable {

    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private int id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;
    @DatabaseField(columnName = "pic")
    @SerializedName("pic")
    private String pic;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    private String remarkEn;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    private String linkId;
    @DatabaseField(columnName = "gis_id")
    @SerializedName("gisid")
    private String gisId;
    @DatabaseField(columnName = "open_time")
    @SerializedName("opentime")
    private String openTime;
    @DatabaseField(columnName = "close_time")
    @SerializedName("closetime")
    private String closeTime;
    @DatabaseField(columnName = "before_day")
    @SerializedName("beforeday")
    private int beforeDay;
    @DatabaseField(columnName = "plan_id")
    @SerializedName("planid")
    private String planId;
    @DatabaseField(columnName = "online_state")
    @SerializedName("onlinestate")
    private String onlineState;
    @DatabaseField(columnName = "open_state")
    @SerializedName("openstate")
    private String openState;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;

    protected Schedule() {

    }

    protected Schedule(Parcel in) {
        id = in.readInt();
        caption = in.readString();
        captionEn = in.readString();
        pic = in.readString();
        remark = in.readString();
        remarkEn = in.readString();
        linkId = in.readString();
        gisId = in.readString();
        openTime = in.readString();
        closeTime = in.readString();
        beforeDay = in.readInt();
        planId = in.readString();
        onlineState = in.readString();
        openState = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(caption);
        dest.writeString(captionEn);
        dest.writeString(pic);
        dest.writeString(remark);
        dest.writeString(remarkEn);
        dest.writeString(linkId);
        dest.writeString(gisId);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeInt(beforeDay);
        dest.writeString(planId);
        dest.writeString(onlineState);
        dest.writeString(openState);
        dest.writeString(createTime);
        dest.writeString(updateTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkEn() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getGisId() {
        return gisId;
    }

    public void setGisId(String gisId) {
        this.gisId = gisId;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getBeforeDay() {
        return beforeDay;
    }

    public void setBeforeDay(int beforeDay) {
        this.beforeDay = beforeDay;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(String onlineState) {
        this.onlineState = onlineState;
    }

    public String getOpenState() {
        return openState;
    }

    public void setOpenState(String openState) {
        this.openState = openState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
