package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "routes")
public class RouteInfo implements Parcelable {
    @DatabaseField(columnName = "id", id = true)
    @SerializedName("id")
    public int id;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    public String caption;
    @DatabaseField(columnName = "captionen")
    @SerializedName("captionen")
    public String captionen;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    public String createTime;
    @DatabaseField(columnName = "hot_count")
    @SerializedName("hotcount")
    public int hotCount;
    @DatabaseField(columnName = "ids_list")
    @SerializedName("idslist")
    public String idsList;
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    public int isEnable;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    public String lat;
    @DatabaseField(columnName = "lines_list")
    @SerializedName("lineslist")
    public String linesList;
    @DatabaseField(columnName = "link_url")
    @SerializedName("linkh5url")
    public String linkurl;
    @DatabaseField(columnName = "lon")
    @SerializedName("lon")
    public String lon;
    @DatabaseField(columnName = "par_id")
    @SerializedName("parkid")
    public String parkId;
    @DatabaseField(columnName = "parkname")
    @SerializedName("parkname")
    public String parkname;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    public String picUrl;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    public String remark;
    @DatabaseField(columnName = "remarken")
    @SerializedName("remarken")
    public String remarken;
    @DatabaseField(columnName = "type_id")
    @SerializedName("typeid")
    public String typeId;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    public String updateTime;

    public RouteInfo() {

    }

    public RouteInfo(Parcel in) {
        id = in.readInt();
        caption = in.readString();
        captionen = in.readString();
        createTime = in.readString();
        hotCount = in.readInt();
        idsList = in.readString();
        isEnable = in.readInt();
        lat = in.readString();
        linesList = in.readString();
        linkurl = in.readString();
        lon = in.readString();
        parkId = in.readString();
        parkname = in.readString();
        picUrl = in.readString();
        remark = in.readString();
        remarken = in.readString();
        typeId = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<RouteInfo> CREATOR = new Creator<RouteInfo>() {
        @Override
        public RouteInfo createFromParcel(Parcel in) {
            return new RouteInfo(in);
        }

        @Override
        public RouteInfo[] newArray(int size) {
            return new RouteInfo[size];
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
        dest.writeString(captionen);
        dest.writeString(createTime);
        dest.writeInt(hotCount);
        dest.writeString(idsList);
        dest.writeInt(isEnable);
        dest.writeString(lat);
        dest.writeString(linesList);
        dest.writeString(linkurl);
        dest.writeString(lon);
        dest.writeString(parkId);
        dest.writeString(parkname);
        dest.writeString(picUrl);
        dest.writeString(remark);
        dest.writeString(remarken);
        dest.writeString(typeId);
        dest.writeString(updateTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaptionen() {
        return captionen;
    }

    public void setCaptionen(String captionen) {
        this.captionen = captionen;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getHotCount() {
        return hotCount;
    }

    public void setHotCount(int hotCount) {
        this.hotCount = hotCount;
    }

    public String getIdsList() {
        return idsList;
    }

    public void setIdsList(String idsList) {
        this.idsList = idsList;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLinesList() {
        return linesList;
    }

    public void setLinesList(String linesList) {
        this.linesList = linesList;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkname() {
        return parkname;
    }

    public void setParkname(String parkname) {
        this.parkname = parkname;
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

    public String getRemarken() {
        return remarken;
    }

    public void setRemarken(String remarken) {
        this.remarken = remarken;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
