package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "subject")
public class Subject implements Parcelable {
    @DatabaseField(columnName = "ar_list")
    @SerializedName("arlist")
    private String arList;
    @DatabaseField(columnName = "ar_list_count")
    @SerializedName("arlistcount")
    private Integer arlistcount;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "color")
    @SerializedName("color")
    private String color;
    @DatabaseField(columnName = "_id",id = true)
    @SerializedName("id")
    private Integer id;
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    private String index;
    @DatabaseField(columnName = "enabled")
    @SerializedName("isenable")
    private Integer enable;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;
    @DatabaseField(columnName = "park_id")
    @SerializedName("parkid")
    private String parkId;
    @DatabaseField(columnName = "park_name")
    @SerializedName("parkname")
    private String parkName;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;

    public Subject() {
    }


    protected Subject(Parcel in) {
        arList = in.readString();
        if (in.readByte() == 0) {
            arlistcount = null;
        } else {
            arlistcount = in.readInt();
        }
        caption = in.readString();
        color = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        index = in.readString();
        if (in.readByte() == 0) {
            enable = null;
        } else {
            enable = in.readInt();
        }
        linkH5Url = in.readString();
        parkId = in.readString();
        parkName = in.readString();
        picUrl = in.readString();
        remark = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject( in );
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( arList );
        if (arlistcount == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( arlistcount );
        }
        dest.writeString( caption );
        dest.writeString( color );
        if (id == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( id );
        }
        dest.writeString( index );
        if (enable == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( enable );
        }
        dest.writeString( linkH5Url );
        dest.writeString( parkId );
        dest.writeString( parkName );
        dest.writeString( picUrl );
        dest.writeString( remark );
    }

    public String getArList() {
        return arList;
    }

    public void setArList(String arList) {
        this.arList = arList;
    }

    public Integer getArlistcount() {
        return arlistcount;
    }

    public void setArlistcount(Integer arlistcount) {
        this.arlistcount = arlistcount;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getLinkH5Url() {
        return linkH5Url;
    }

    public void setLinkH5Url(String linkH5Url) {
        this.linkH5Url = linkH5Url;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
