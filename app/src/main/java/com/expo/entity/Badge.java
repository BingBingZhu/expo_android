package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "badge")
public class Badge implements Parcelable {

    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;   // 徽章名称 ,
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;   // 徽章名称英文 ,
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;   // 创建时间 ,
    @DatabaseField(columnName = "icon")
    @SerializedName("icon")
    private String icon;   // icon ,
    @DatabaseField(columnName = "icon_small")
    @SerializedName("iconsmall")
    private String iconSmall;   // iconsmall ,
    @DatabaseField(columnName = "id")
    @SerializedName("id")
    private Long id;   //
    @DatabaseField(columnName = "level")
    @SerializedName("level")
    private Integer level;   // level ,
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;   // 备注信息 ,
    @DatabaseField(columnName = "score")
    @SerializedName("score")
    private Integer score;   // 所需积分 ,
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;   // 修改时间

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconSmall() {
        return iconSmall;
    }

    public void setIconSmall(String iconSmall) {
        this.iconSmall = iconSmall;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Badge() {
    }

    protected Badge(Parcel in) {
        caption = in.readString();
        captionEn = in.readString();
        createTime = in.readString();
        icon = in.readString();
        iconSmall = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            level = null;
        } else {
            level = in.readInt();
        }
        remark = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readInt();
        }
        updateTime = in.readString();
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel in) {
            return new Badge(in);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
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
        dest.writeString(createTime);
        dest.writeString(icon);
        dest.writeString(iconSmall);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (level == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(level);
        }
        dest.writeString(remark);
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(score);
        }
        dest.writeString(updateTime);
    }
}
