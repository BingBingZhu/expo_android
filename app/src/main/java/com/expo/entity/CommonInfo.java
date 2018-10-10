package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "common_information")
public class CommonInfo implements Parcelable {
    @DatabaseField(columnName = "id", id = true)
    @SerializedName("id")
    private Integer id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
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
    private String type;//1  用户协议  2 法律法规  3 帮助中心  4 关于我们 5 积分规则  6 ar广场 7 球幕玩法
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
            return new CommonInfo( in );
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( id );
        }
        dest.writeString( caption );
        dest.writeString( createTime );
        dest.writeString( isEenable );
        dest.writeString( linkId );
        dest.writeString( linkUrl );
        dest.writeString( remark );
        dest.writeString( type );
        dest.writeString( updateTime );
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
