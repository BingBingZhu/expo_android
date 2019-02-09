package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "portal_site")
public class PortalSite implements Parcelable {
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;//标题 ,
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;//标题英文 ,
    @DatabaseField(columnName = "id", id = true)
    @SerializedName("id")
    private Integer id;
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    private Integer idx;//排序索引值 ,
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    private String enable;//是否可用 0 否 1 是 ,
    @DatabaseField(columnName = "link_url")
    @SerializedName("linkurl")
    private String linkUrl;//链接页面 ,
    @DatabaseField(columnName = "link_url_en")
    @SerializedName("linkurlen")
    private String linkUrlEn;// 链接页面英文 ,
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;//背景图 ,
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;//备注内容 ,
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;//类别 1 植物学院 2 新闻动态 3 每周一园 4 董卿·草木卿园 5 读·植物的力量 6 听·花之声 7 百草园 8 我是园艺师 ,
    @DatabaseField(columnName = "type_id")
    @SerializedName("typeid")
    private String typeId;//类别id


    public PortalSite() {
    }

    protected PortalSite(Parcel in) {
        caption = in.readString();
        captionEn = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            idx = null;
        } else {
            idx = in.readInt();
        }
        enable = in.readString();
        linkUrl = in.readString();
        linkUrlEn = in.readString();
        picUrl = in.readString();
        remark = in.readString();
        type = in.readString();
        typeId = in.readString();
    }

    public static final Creator<PortalSite> CREATOR = new Creator<PortalSite>() {
        @Override
        public PortalSite createFromParcel(Parcel in) {
            return new PortalSite(in);
        }

        @Override
        public PortalSite[] newArray(int size) {
            return new PortalSite[size];
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
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (idx == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idx);
        }
        dest.writeString(enable);
        dest.writeString(linkUrl);
        dest.writeString(linkUrlEn);
        dest.writeString(picUrl);
        dest.writeString(remark);
        dest.writeString(type);
        dest.writeString(typeId);
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkUrlEn() {
        return linkUrlEn;
    }

    public void setLinkUrlEn(String linkUrlEn) {
        this.linkUrlEn = linkUrlEn;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
