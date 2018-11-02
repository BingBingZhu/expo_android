package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "encyclopedias")
public class Encyclopedias implements Parcelable {
    @DatabaseField(columnName = "area")
    @SerializedName("area")
    private String area;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "collection_count")
    @SerializedName("collectioncount")
    private Integer collectionCount;
    @DatabaseField(columnName = "content")
    @SerializedName("content")
    private String content;
    @DatabaseField(columnName = "country")
    @SerializedName("country")
    private String country;
    @DatabaseField(columnName = "h5_pic_url")
    @SerializedName("h5picurl")
    private String h5PicUrl;
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "info_type")
    @SerializedName("infotype")
    private String infoType;
    @DatabaseField(columnName = "enable")
    @SerializedName("isenable")
    private Integer enable;
    @DatabaseField(columnName = "recommend")
    @SerializedName("isrecommended")
    private Integer recommend;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;
    @DatabaseField(columnName = "link_info_url")
    @SerializedName("linkinfourl")
    private String linkInfoUrl;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;
    @DatabaseField(columnName = "py")
    @SerializedName("py")
    private String py;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "type_id")
    @SerializedName("typeid")
    private String typeId;
    @DatabaseField(columnName = "type_name")
    @SerializedName("typename")
    private String typeName;

    public Encyclopedias() {
    }

    protected Encyclopedias(Parcel in) {
        area = in.readString();
        caption = in.readString();
        if (in.readByte() == 0) {
            collectionCount = null;
        } else {
            collectionCount = in.readInt();
        }
        content = in.readString();
        country = in.readString();
        h5PicUrl = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        infoType = in.readString();
        if (in.readByte() == 0) {
            enable = null;
        } else {
            enable = in.readInt();
        }
        linkH5Url = in.readString();
        linkInfoUrl = in.readString();
        picUrl = in.readString();
        py = in.readString();
        remark = in.readString();
        typeId = in.readString();
        typeName = in.readString();
    }

    public static final Creator<Encyclopedias> CREATOR = new Creator<Encyclopedias>() {
        @Override
        public Encyclopedias createFromParcel(Parcel in) {
            return new Encyclopedias( in );
        }

        @Override
        public Encyclopedias[] newArray(int size) {
            return new Encyclopedias[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( area );
        dest.writeString( caption );
        if (collectionCount == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( collectionCount );
        }
        dest.writeString( content );
        dest.writeString( country );
        dest.writeString( h5PicUrl );
        if (id == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeLong( id );
        }
        dest.writeString( infoType );
        if (enable == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( enable );
        }
        dest.writeString( linkH5Url );
        dest.writeString( linkInfoUrl );
        dest.writeString( picUrl );
        dest.writeString( py );
        dest.writeString( remark );
        dest.writeString( typeId );
        dest.writeString( typeName );
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(Integer collectionCount) {
        this.collectionCount = collectionCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getH5PicUrl() {
        return h5PicUrl;
    }

    public void setH5PicUrl(String h5PicUrl) {
        this.h5PicUrl = h5PicUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
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

    public String getLinkInfoUrl() {
        return linkInfoUrl;
    }

    public void setLinkInfoUrl(String linkInfoUrl) {
        this.linkInfoUrl = linkInfoUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }
}
