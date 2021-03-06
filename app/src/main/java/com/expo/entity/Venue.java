package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.amap.api.maps.model.LatLng;
import com.expo.network.Http;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "venue")
public class Venue implements Parcelable, Comparable<Venue> {
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String enCaption;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;
    @DatabaseField(columnName = "type_name")
    @SerializedName("typename")
    private String typeName ;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    private String enRemark;
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    private int isEnable;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;
    @DatabaseField(columnName = "link_h5_url_en")
    @SerializedName("linkh5urlen")
    private String linkH5UrlEn;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;
    @DatabaseField(columnName = "lng")
    @SerializedName("lon")
    private String lng;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    private String lat;
    @DatabaseField(columnName = "electronic_fence_list")
    @SerializedName("electronicfencelist")
    private String electronicFenceList;
    @DatabaseField(columnName = "is_recommended")
    @SerializedName("isrecommended")
    private Integer isRecommended;
    @DatabaseField(columnName = "recommended_idx")
    @SerializedName("recommendedidx")
    private String recommendedIdx;
    @DatabaseField(columnName = "wiki_id")
    @SerializedName("wikiid")
    private String wikiId;
    @DatabaseField(columnName = "score")
    @SerializedName("score")
    private int score;
    @DatabaseField(columnName = "voice_url")
    @SerializedName("voiceurl")
    private String voiceUrl;           // 场馆音频介绍 ,
    @DatabaseField(columnName = "voice_url_en")
    @SerializedName("voiceurlen")
    private String voiceUrlEn;          //场馆音频介绍英文，如果没有可以使用中文介绍的链接
    @DatabaseField(columnName = "gis_id")
    @SerializedName("gisid")
    public String gisId;
    @DatabaseField(columnName = "gis_type_id")
    @SerializedName("gistypeid")
    public String gisTypeId;
    @DatabaseField(columnName = "park_id")
    @SerializedName("parkid")
    public String parkId;
    @DatabaseField(columnName = "park_name")
    @SerializedName("parkname")
    public String parkName;
    @DatabaseField(columnName = "pic_url_en")
    @SerializedName("picurlen")
    public String picUrlEn;
    @DatabaseField(columnName = "is_selected")
    private boolean isSelected;
    @DatabaseField(columnName = "route_index")
    private long routeIndex;

    public Venue() {
    }


    protected Venue(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        caption = in.readString();
        enCaption = in.readString();
        type = in.readString();
        remark = in.readString();
        enRemark = in.readString();
        isEnable = in.readInt();
        picUrl = in.readString();
        linkH5Url = in.readString();
        linkH5UrlEn = in.readString();
        createTime = in.readString();
        updateTime = in.readString();
        lng = in.readString();
        lat = in.readString();
        electronicFenceList = in.readString();
        if (in.readByte() == 0) {
            isRecommended = null;
        } else {
            isRecommended = in.readInt();
        }
        recommendedIdx = in.readString();
        wikiId = in.readString();
        score = in.readInt();
        voiceUrl = in.readString();
        voiceUrlEn = in.readString();
        gisId = in.readString();
        gisTypeId = in.readString();
        parkId = in.readString();
        parkName = in.readString();
        picUrlEn = in.readString();
        typeName = in.readString();
    }

    public static final Creator<Venue> CREATOR = new Creator<Venue>() {
        @Override
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        @Override
        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };

    public ArrayList<double[]> getElectronicFenceList() {
        return Http.getGsonInstance().fromJson(electronicFenceList, new TypeToken<ArrayList<double[]>>() {
        }.getType());
    }


    public Long getType() {
        try {
            return Long.parseLong(type);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    public Double getLng() {
        try {
            return Double.parseDouble(lng);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Double getLat() {
        try {
            return Double.parseDouble(lat);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEnCaption() {
        return enCaption;
    }

    public void setEnCaption(String enCaption) {
        this.enCaption = enCaption;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnRemark() {
        return enRemark;
    }

    public void setEnRemark(String enRemark) {
        this.enRemark = enRemark;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setElectronicFenceList(String electronicFenceList) {
        this.electronicFenceList = electronicFenceList;
    }

    public Integer getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Integer isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getRecommendedIdx() {
        return recommendedIdx;
    }

    public void setRecommendedIdx(String recommendedIdx) {
        this.recommendedIdx = recommendedIdx;
    }

    public String getWikiId() {
        return wikiId;
    }

    public void setWikiId(String wikiId) {
        this.wikiId = wikiId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public String getGisId() {
        return gisId;
    }

    public void setGisId(String gisId) {
        this.gisId = gisId;
    }

    public String getGisTypeId() {
        return gisTypeId;
    }

    public void setGisTypeId(String gisTypeId) {
        this.gisTypeId = gisTypeId;
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

    public String getPicUrlEn() {
        return picUrlEn;
    }

    public void setPicUrlEn(String picUrlEn) {
        this.picUrlEn = picUrlEn;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getRouteIndex() {
        return routeIndex;
    }

    public void setRouteIndex(long routeIndex) {
        this.routeIndex = routeIndex;
    }

    public LatLng getLatLng() {
        return new LatLng(this.getLat(), this.getLng());
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
            dest.writeLong(id);
        }
        dest.writeString(caption);
        dest.writeString(enCaption);
        dest.writeString(type);
        dest.writeString(remark);
        dest.writeString(enRemark);
        dest.writeInt(isEnable);
        dest.writeString(picUrl);
        dest.writeString(linkH5Url);
        dest.writeString(linkH5UrlEn);
        dest.writeString(createTime);
        dest.writeString(updateTime);
        dest.writeString(lng);
        dest.writeString(lat);
        dest.writeString(electronicFenceList);
        if (isRecommended == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isRecommended);
        }
        dest.writeString(recommendedIdx);
        dest.writeString(wikiId);
        dest.writeInt(score);
        dest.writeString(voiceUrl);
        dest.writeString(voiceUrlEn);
        dest.writeString(gisId);
        dest.writeString(gisTypeId);
        dest.writeString(parkId);
        dest.writeString(parkName);
        dest.writeString(picUrlEn);
        dest.writeString(typeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Venue venue = (Venue) o;

        return id.equals(venue.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(@NonNull Venue o) {
        return (int) (this.getRouteIndex() - o.getRouteIndex());
    }
}
