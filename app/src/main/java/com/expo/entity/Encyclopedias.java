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
    public String area;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    public String captionEn;
    @DatabaseField(columnName = "collection_count")
    @SerializedName("collectioncount")
    public Integer collectionCount;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public Integer createTime;
    @DatabaseField(columnName = "content")
    @SerializedName("content")
    public String content;
    @DatabaseField(columnName = "country")
    @SerializedName("country")
    public String country;
    @DatabaseField(columnName = "h5_pic_url")
    @SerializedName("h5picurl")
    public String h5PicUrl;
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    public Long id;
    @DatabaseField(columnName = "info_type")
    @SerializedName("infotype")
    public String infoType;
    @DatabaseField(columnName = "enable")
    @SerializedName("isenable")
    public Integer enable;
    @DatabaseField(columnName = "recommend")
    @SerializedName("isrecommended")
    public Integer recommend;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    public String linkH5Url;
    @DatabaseField(columnName = "link_h5_url_en")
    @SerializedName("linkh5urlen")
    public String linkH5UrlEn;
    @DatabaseField(columnName = "link_info_url")
    @SerializedName("linkinfourl")
    public String linkInfoUrl;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    public String picUrl;
    @DatabaseField(columnName = "py")
    @SerializedName("py")
    public String py;
    @DatabaseField(columnName = "recommended_idx")
    @SerializedName("recommendedidx")
    public String recommendedIdx;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    public String remarkEn;
    @DatabaseField(columnName = "type_id")
    @SerializedName("typeid")
    public String typeId;
    @DatabaseField(columnName = "type_name")
    @SerializedName("typename")
    public String typeName;
    @DatabaseField(columnName = "type_name_en")
    @SerializedName("typenameen")
    public String typeNameEn;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;
    @DatabaseField(columnName = "voiceUrl")
    @SerializedName("voiceurl")
    public String voiceUrl;
    @DatabaseField(columnName = "voiceEurl_en")
    @SerializedName("voiceurlen")
    public String voiceUrlEn;

    public Encyclopedias() {
    }

    protected Encyclopedias(Parcel in) {
        area = in.readString();
        caption = in.readString();
        captionEn = in.readString();
        if (in.readByte() == 0) {
            collectionCount = null;
        } else {
            collectionCount = in.readInt();
        }
        if (in.readByte() == 0) {
            createTime = null;
        } else {
            createTime = in.readInt();
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
        if (in.readByte() == 0) {
            recommend = null;
        } else {
            recommend = in.readInt();
        }
        linkH5Url = in.readString();
        linkH5UrlEn = in.readString();
        linkInfoUrl = in.readString();
        picUrl = in.readString();
        py = in.readString();
        recommendedIdx = in.readString();
        remark = in.readString();
        remarkEn = in.readString();
        typeId = in.readString();
        typeName = in.readString();
        typeNameEn = in.readString();
        updateTime = in.readString();
        voiceUrl = in.readString();
        voiceUrlEn = in.readString();
    }

    public static final Creator<Encyclopedias> CREATOR = new Creator<Encyclopedias>() {
        @Override
        public Encyclopedias createFromParcel(Parcel in) {
            return new Encyclopedias(in);
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
        dest.writeString(area);
        dest.writeString(caption);
        dest.writeString(captionEn);
        if (collectionCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(collectionCount);
        }
        if (createTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(createTime);
        }
        dest.writeString(content);
        dest.writeString(country);
        dest.writeString(h5PicUrl);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(infoType);
        if (enable == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(enable);
        }
        if (recommend == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(recommend);
        }
        dest.writeString(linkH5Url);
        dest.writeString(linkH5UrlEn);
        dest.writeString(linkInfoUrl);
        dest.writeString(picUrl);
        dest.writeString(py);
        dest.writeString(recommendedIdx);
        dest.writeString(remark);
        dest.writeString(remarkEn);
        dest.writeString(typeId);
        dest.writeString(typeName);
        dest.writeString(typeNameEn);
        dest.writeString(updateTime);
        dest.writeString(voiceUrl);
        dest.writeString(voiceUrlEn);
    }

}
