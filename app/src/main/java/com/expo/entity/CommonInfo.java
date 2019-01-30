package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "common_information")
public class CommonInfo implements Parcelable {
    public static final String USER_GUIDE = "0";//用户指南
    public static final String USER_PROTOCOL = "1";//用户协议、法律条款与政策
    public static final String NOTICE_OF_BUY_TICKETS = "2";//购票须知
    public static final String NOTICE_OF_GARDEN = "3";//用户须知、游园须知
    public static final String BARRIER_FREE_SERVICE = "4";//无障碍服务
    public static final String VENUE_BESPEAK = "5";//场馆预约
    public static final String BUY_TICKETS = "6";//购票
    public static final String PANORAMA = "7";//全景、网上世园
    public static final String ORDER_MESSAGE_INFO = "8";//订单消息详情
    public static final String MY_BESPEAK = "9";//我的预约
    public static final String SCORE = "10";//积分
    public static final String PORTAL_WEBSITE_INTEGRATION = "11";//门户网站、微观世界
    public static final String VISITOR_SERVICE_DETAILS = "12";//游客服务详情
    public static final String ENCYCLOPEDIAS_DETAIL_URL = "13";// 百科详情
    public static final String EXPO_BRIEF_INTRODUCTION = "14";// 世园会简介
    public static final String TOURIST_SERVICE_RENTAL_ITEMS = "15";// 物品租赁
    public static final String TOURIST_SERVICE_LOST_AND_FOUND = "16";// 问询咨询
    public static final String TOURIST_SERVICE_MATERNAL_AND_CHILD = "17";// 母婴服务
    public static final String TOURIST_SERVICE_LEFT_LUGGAGE = "18";// 物品寄存
    public static final String EXPO_AR_DOWNLOAD_PAGE = "15";// 世园会简介

    @DatabaseField(columnName = "id", id = true)
    @SerializedName("id")
    private Integer id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    private String isEenable;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    private String linkId;
    @DatabaseField(columnName = "link_url")
    @SerializedName("linkurl")
    private String linkUrl;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;// 0 用户指南  1 用户协议  2 购票须知  3 用户须知  4 无障碍服务 5 预约  6 购票 7 全景地址 10 积分兑换
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;

    public CommonInfo() {
    }

    protected CommonInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        caption = in.readString();
        createTime = in.readString();
        isEenable = in.readString();
        linkId = in.readString();
        linkUrl = in.readString();
        remark = in.readString();
        type = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<CommonInfo> CREATOR = new Creator<CommonInfo>() {
        @Override
        public CommonInfo createFromParcel(Parcel in) {
            return new CommonInfo(in);
        }

        @Override
        public CommonInfo[] newArray(int size) {
            return new CommonInfo[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsEenable() {
        return isEenable;
    }

    public void setIsEenable(String isEenable) {
        this.isEenable = isEenable;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCaptionEn() {
        return captionEn;
    }

    public void setCaptionEn(String captionEn) {
        this.captionEn = captionEn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(caption);
        dest.writeString(createTime);
        dest.writeString(isEenable);
        dest.writeString(linkId);
        dest.writeString(linkUrl);
        dest.writeString(remark);
        dest.writeString(type);
        dest.writeString(updateTime);
    }

    @Override
    public String toString() {
        return "CommonInfo{" +
                "id=" + id +
                ", caption='" + caption + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isEenable='" + isEenable + '\'' +
                ", linkId='" + linkId + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", remark='" + remark + '\'' +
                ", type='" + type + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
