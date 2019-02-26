package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "encyclopedias")
public class Encyclopedias implements Parcelable, Comparable<Encyclopedias> {
    @DatabaseField(columnName = "area")
    @SerializedName("area")
    private String area;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    public String captionEn;
    @DatabaseField(columnName = "collection_count")
    @SerializedName("collectioncount")
    private Integer collectionCount;
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
    @DatabaseField(columnName = "model_url")
    @SerializedName("modelurl")
    public String modelUrl;
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
    public Long typeId;
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
    @DatabaseField(columnName = "is_share")
    @SerializedName("isshare")
    public String isShare;
    @DatabaseField(columnName = "estimated_tour_time")
    @SerializedName("playtime")
    public String estimatedTourTime;//预计游览时间
    @DatabaseField(columnName = "exhibition")
    @SerializedName("introduction")
    public String exhibition;//展项介绍
    @DatabaseField(columnName = "exhibition_en")
    @SerializedName("introductionen")
    public String exhibitionEn;//展项介绍 英文
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    public int idx;//排序
    @DatabaseField(columnName = "trait_label")
    @SerializedName("traitlabel")
    public String traitLabel;
    @DatabaseField(columnName = "recommend_lang")
    @SerializedName("recommendlang")
    public String recommendLang;
    @DatabaseField(columnName = "recommend_lang_en")
    @SerializedName("recommendlanguage")
    public String recommendLangEn;
    @DatabaseField(columnName = "model_pic_url")
    @SerializedName("modelpicurl")
    public String modelPicUrl;

    @SerializedName("distance")
    private String distance;
    @SerializedName("distance_float")
    private float distanceFloat;

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
        modelUrl = in.readString();
        linkInfoUrl = in.readString();
        picUrl = in.readString();
        py = in.readString();
        recommendedIdx = in.readString();
        remark = in.readString();
        remarkEn = in.readString();
        if (in.readByte() == 0) {
            typeId = null;
        } else {
            typeId = in.readLong();
        }
        typeName = in.readString();
        typeNameEn = in.readString();
        updateTime = in.readString();
        voiceUrl = in.readString();
        voiceUrlEn = in.readString();
        estimatedTourTime = in.readString();
        exhibition = in.readString();
        exhibitionEn = in.readString();
        idx = in.readInt();
        distance = in.readString();
        traitLabel = in.readString();
        recommendLang = in.readString();
        recommendLangEn = in.readString();
        modelPicUrl = in.readString();
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

    public String getCaptionEn() {
        return captionEn;
    }

    public void setCaptionEn(String captionEn) {
        this.captionEn = captionEn;
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

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getLinkH5Url() {
        return linkH5Url;
    }

    public void setLinkH5Url(String linkH5Url) {
        this.linkH5Url = linkH5Url;
    }

    public String getLinkH5UrlEn() {
        return linkH5UrlEn;
    }

    public void setLinkH5UrlEn(String linkH5UrlEn) {
        this.linkH5UrlEn = linkH5UrlEn;
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

    public String getRecommendedIdx() {
        return recommendedIdx;
    }

    public void setRecommendedIdx(String recommendedIdx) {
        this.recommendedIdx = recommendedIdx;
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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNameEn() {
        return typeNameEn;
    }

    public void setTypeNameEn(String typeNameEn) {
        this.typeNameEn = typeNameEn;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getVoiceUrlEn() {
        return voiceUrlEn;
    }

    public void setVoiceUrlEn(String voiceUrlEn) {
        this.voiceUrlEn = voiceUrlEn;
    }

    public String getEstimatedTourTime() {
        return estimatedTourTime;
    }

    public void setEstimatedTourTime(String estimatedTourTime) {
        this.estimatedTourTime = estimatedTourTime;
    }

    public String getExhibition() {
        return exhibition;
    }

    public void setExhibition(String exhibition) {
        this.exhibition = exhibition;
    }

    public String getExhibitionEn() {
        return exhibitionEn;
    }

    public void setExhibitionEn(String exhibitionEn) {
        this.exhibitionEn = exhibitionEn;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public float getDistanceFloat() {
        return distanceFloat;
    }

    public void setDistanceFloat(float distanceFloat) {
        this.distanceFloat = distanceFloat;
    }

    public void setDistance(float distance) {
        String distanceStr = "";
        if (distance > 1000) {
            distanceStr = (int) (distance / 1000) + "Km";
        } else {
            distanceStr = (int) distance + "m";
        }
        this.distance = "\u8ddd\u79bb" + distanceStr;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public String getTraitLabel() {
        return traitLabel;
    }

    public void setTraitLabel(String traitLabel) {
        this.traitLabel = traitLabel;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public String getModelPicUrl() {
        return modelPicUrl;
    }

    public void setModelPicUrl(String modelPicUrl) {
        this.modelPicUrl = modelPicUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRecommendLang() {
        return recommendLang;
    }

    public void setRecommendLang(String recommendLang) {
        this.recommendLang = recommendLang;
    }

    public String getRecommendLangEn() {
        return recommendLangEn;
    }

    public void setRecommendLangEn(String recommendLangEn) {
        this.recommendLangEn = recommendLangEn;
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
        dest.writeString(modelUrl);
        dest.writeString(linkInfoUrl);
        dest.writeString(picUrl);
        dest.writeString(py);
        dest.writeString(recommendedIdx);
        dest.writeString(remark);
        dest.writeString(remarkEn);
        if (typeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(typeId);
        }
        dest.writeString(typeName);
        dest.writeString(typeNameEn);
        dest.writeString(updateTime);
        dest.writeString(voiceUrl);
        dest.writeString(voiceUrlEn);
        dest.writeString(estimatedTourTime);
        dest.writeString(exhibition);
        dest.writeString(exhibitionEn);
        dest.writeInt(idx);
        dest.writeString(distance);
        dest.writeString(traitLabel);
        dest.writeString(recommendLang);
        dest.writeString(recommendLangEn);
        dest.writeString(modelPicUrl);
    }

    @Override
    public int compareTo(Encyclopedias encyclopedias) {
        return (int) ((this.getDistanceFloat() - encyclopedias.getDistanceFloat())*100);
    }
}
