package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.module.download.DownloadManager;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "tourist_type")
public class TouristType implements Parcelable {
    @DatabaseField(columnName = "id",id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "age")
    @SerializedName("age")
    private String age;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEN;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "idx")
    @SerializedName("idx")
    private Integer idx;
    @DatabaseField(columnName = "is_enable")
    @SerializedName("isenable")
    private String isEnable;
    @DatabaseField(columnName = "model_file")
    @SerializedName("modelfile")
    private String modelFile;
    @DatabaseField(columnName = "model_file_size")
    @SerializedName("modelfilesize")
    private Integer modelFileSize;
    @DatabaseField(columnName = "packed_name")
    @SerializedName("packedname")
    private String packedName;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    private String remarkEN;
    @DatabaseField(columnName = "sex")
    @SerializedName("sex")
    private String sex;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;

    @DatabaseField(columnName = "used")
    private boolean used;
    @DatabaseField(columnName = "down_state")
    private Integer downState;
    @DatabaseField(columnName = "curr_position")
    private long currPosition;
    @DatabaseField(columnName = "local_path")
    private String localPath;


    public TouristType(){}

    protected TouristType(Parcel in) {
        age = in.readString();
        caption = in.readString();
        captionEN = in.readString();
        createTime = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            idx = null;
        } else {
            idx = in.readInt();
        }
        isEnable = in.readString();
        modelFile = in.readString();
        if (in.readByte() == 0) {
            modelFileSize = null;
        } else {
            modelFileSize = in.readInt();
        }
        packedName = in.readString();
        picUrl = in.readString();
        remark = in.readString();
        remarkEN = in.readString();
        sex = in.readString();
        updateTime = in.readString();
    }

    public static final Creator<TouristType> CREATOR = new Creator<TouristType>() {
        @Override
        public TouristType createFromParcel(Parcel in) {
            return new TouristType(in);
        }

        @Override
        public TouristType[] newArray(int size) {
            return new TouristType[size];
        }
    };

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaptionEN() {
        return captionEN;
    }

    public void setCaptionEN(String captionEN) {
        this.captionEN = captionEN;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getModelFile() {
        return modelFile;
    }

    public void setModelFile(String modelFile) {
        this.modelFile = modelFile;
    }

    public Integer getModelFileSize() {
        return modelFileSize;
    }

    public void setModelFileSize(Integer modelFileSize) {
        this.modelFileSize = modelFileSize;
    }

    public String getPackedName() {
        return packedName;
    }

    public void setPackedName(String packedName) {
        this.packedName = packedName;
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

    public String getRemarkEN() {
        return remarkEN;
    }

    public void setRemarkEN(String remarkEN) {
        this.remarkEN = remarkEN;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDownState() {
        if (null == downState){
            return DownloadManager.DOWNLOAD_IDLE;
        }
        return downState;
    }

    public void setDownState(Integer downState) {
        this.downState = downState;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public long getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(long currPosition) {
        this.currPosition = currPosition;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(age);
        dest.writeString(caption);
        dest.writeString(captionEN);
        dest.writeString(createTime);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (idx == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(idx);
        }
        dest.writeString(isEnable);
        dest.writeString(modelFile);
        if (modelFileSize == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(modelFileSize);
        }
        dest.writeString(packedName);
        dest.writeString(picUrl);
        dest.writeString(remark);
        dest.writeString(remarkEN);
        dest.writeString(sex);
        dest.writeString(updateTime);
    }
}
