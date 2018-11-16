package com.expo.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "venues_type")
public class VenuesType implements Parcelable {
    @DatabaseField(columnName = "id", id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;         //创建时间 ,
    @DatabaseField(columnName = "gis_type_id")
    @SerializedName("gistypeid")
    private String gisTypeId;           //gis系统的类型id ,
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    private String idx;                 //排序 ,
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    private Integer isEnable;           //设施是否可用 1 可用 0 禁用 ,
    @DatabaseField(columnName = "is_seach")
    @SerializedName("isseach")
    private String isSeach;             //是否要在搜索中列出来 ,
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;           //Link h5 链接的介绍页面 ,
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;              //图片 ,
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;              //说明 ,
    @DatabaseField(columnName = "type_name")
    @SerializedName("typename")
    private String typeName;            //类型名称 ,
    @DatabaseField(columnName = "type_name_en")
    @SerializedName("typenameen")
    private String typeNameEn;         //类型名称英文 ,
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;         //修改时间
    @DatabaseField(columnName = "pic_lst_url")
    @SerializedName("piclsturl")
    private String picLstUrl;       //列表图片 ,
    @DatabaseField(columnName = "pic_mark_url")
    @SerializedName("picmarkurl")
    private String picMarkUrl;          // 地图图标 ,

    private Bitmap lstBitmap;
    private Bitmap markBitmap;

    public VenuesType() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGisTypeId() {
        return gisTypeId;
    }

    public void setGisTypeId(String gisTypeId) {
        this.gisTypeId = gisTypeId;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public Integer getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Integer isEnable) {
        this.isEnable = isEnable;
    }

    public String getIsSeach() {
        return isSeach;
    }

    public void setIsSeach(String isSeach) {
        this.isSeach = isSeach;
    }

    public String getLinkH5Url() {
        return linkH5Url;
    }

    public void setLinkH5Url(String linkH5Url) {
        this.linkH5Url = linkH5Url;
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

    public String getPicLstUrl() {
        return picLstUrl;
    }

    public void setPicLstUrl(String picLstUrl) {
        this.picLstUrl = picLstUrl;
    }

    public String getPicMarkUrl() {
        return picMarkUrl;
    }

    public void setPicMarkUrl(String picMarkUrl) {
        this.picMarkUrl = picMarkUrl;
    }

    public Bitmap getLstBitmap() {
        return lstBitmap;
    }

    public void setLstBitmap(Bitmap lstBitmap) {
        this.lstBitmap = lstBitmap;
    }

    public Bitmap getMarkBitmap() {
        return markBitmap;
    }

    public void setMarkBitmap(Bitmap markBitmap) {
        this.markBitmap = markBitmap;
    }

    public boolean picIsFinished(){
        return null != lstBitmap && null != markBitmap;
    }

    protected VenuesType(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        createTime = in.readString();
        gisTypeId = in.readString();
        idx = in.readString();
        if (in.readByte() == 0) {
            isEnable = null;
        } else {
            isEnable = in.readInt();
        }
        isSeach = in.readString();
        linkH5Url = in.readString();
        picUrl = in.readString();
        remark = in.readString();
        typeName = in.readString();
        typeNameEn = in.readString();
        updateTime = in.readString();
        picLstUrl = in.readString();
        picMarkUrl = in.readString();
    }

    public static final Creator<VenuesType> CREATOR = new Creator<VenuesType>() {
        @Override
        public VenuesType createFromParcel(Parcel in) {
            return new VenuesType(in);
        }

        @Override
        public VenuesType[] newArray(int size) {
            return new VenuesType[size];
        }
    };

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
        dest.writeString(createTime);
        dest.writeString(gisTypeId);
        dest.writeString(idx);
        if (isEnable == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isEnable);
        }
        dest.writeString(isSeach);
        dest.writeString(linkH5Url);
        dest.writeString(picUrl);
        dest.writeString(remark);
        dest.writeString(typeName);
        dest.writeString(typeNameEn);
        dest.writeString(updateTime);
        dest.writeString(picLstUrl);
        dest.writeString(picMarkUrl);
    }
}
