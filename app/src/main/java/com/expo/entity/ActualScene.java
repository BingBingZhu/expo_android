package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.network.Http;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "actual_scene")
public class ActualScene implements Parcelable {
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    private int isEnable;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;
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


    public ActualScene() {
    }

    protected ActualScene(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        caption = in.readString();
        type = in.readString();
        remark = in.readString();
        isEnable = in.readInt();
        picUrl = in.readString();
        linkH5Url = in.readString();
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
    }

    public static final Creator<ActualScene> CREATOR = new Creator<ActualScene>() {
        @Override
        public ActualScene createFromParcel(Parcel in) {
            return new ActualScene( in );
        }

        @Override
        public ActualScene[] newArray(int size) {
            return new ActualScene[size];
        }
    };

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
            dest.writeLong( id );
        }
        dest.writeString( caption );
        dest.writeString( type );
        dest.writeString( remark );
        dest.writeInt( isEnable );
        dest.writeString( picUrl );
        dest.writeString( linkH5Url );
        dest.writeString( createTime );
        dest.writeString( updateTime );
        dest.writeString( lng );
        dest.writeString( lat );
        dest.writeString( electronicFenceList );
        if (isRecommended == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( isRecommended );
        }
        dest.writeString( recommendedIdx );
    }

    public ArrayList<double[]> getElectronicFenceList() {
        return Http.getGsonInstance().fromJson( electronicFenceList, new TypeToken<double[]>() {
        }.getType() );
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

    public String getType() {
        return type;
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

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
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
}
