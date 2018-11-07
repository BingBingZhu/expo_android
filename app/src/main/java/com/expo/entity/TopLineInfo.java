package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "topline")
public class TopLineInfo implements Parcelable {
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    public int id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    public String captionEn;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "end_time")
    @SerializedName("endtime")
    public String endTime;
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    public String idx;
    @DatabaseField(columnName = "is_date")
    @SerializedName("isdate")
    public int isDate;
    @DatabaseField(columnName = "enable")
    @SerializedName("isenable")
    public int enable;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    public String linkH5Url;
    @DatabaseField(columnName = "link_h5_url_en")
    @SerializedName("linkh5urlen")
    public String linkH5UrlEn;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    public String picUrl;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    public String remarkEn;
    @DatabaseField(columnName = "start_time")
    @SerializedName("startTime")
    public String starttime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;

    protected TopLineInfo() {

    }

    protected TopLineInfo(Parcel in) {
        id = in.readInt();
        caption = in.readString();
        captionEn = in.readString();
        createTime = in.readString();
        endTime = in.readString();
        idx = in.readString();
        isDate = in.readInt();
        enable = in.readInt();
        linkH5Url = in.readString();
        linkH5UrlEn = in.readString();
        picUrl = in.readString();
        remark = in.readString();
        remarkEn = in.readString();
        starttime = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<TopLineInfo> CREATOR = new Creator<TopLineInfo>() {
        @Override
        public TopLineInfo createFromParcel(Parcel in) {
            return new TopLineInfo(in);
        }

        @Override
        public TopLineInfo[] newArray(int size) {
            return new TopLineInfo[size];
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
        dest.writeString(createTime);
        dest.writeString(endTime);
        dest.writeString(idx);
        dest.writeInt(isDate);
        dest.writeInt(enable);
        dest.writeString(linkH5Url);
        dest.writeString(linkH5UrlEn);
        dest.writeString(picUrl);
        dest.writeString(remark);
        dest.writeString(remarkEn);
        dest.writeString(starttime);
        dest.writeString(updateTime);
    }
}
