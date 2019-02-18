package com.expo.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "data_type")
public class DataType implements Parcelable {
    @DatabaseField(columnName = "_id", generatedId = true, allowGeneratedIdInsert = true)
    @Expose(deserialize = false, serialize = false)
    private Integer _id;
    @DatabaseField(columnName = "type_id")
    @SerializedName("id")
    private Integer typeId;
    @DatabaseField(columnName = "bg_vr_card")
    @SerializedName(value = "typename", alternate = {"caption"})
    private String name;
    @DatabaseField(columnName = "name_en")
    @SerializedName(value = "typenameen", alternate = {"captionen"})
    private String enName;
    @DatabaseField(columnName = "pic_url")
    @SerializedName("picurl")
    private String picUrl;
    @DatabaseField(columnName = "pic_lst_url")
    @SerializedName("piclsturl")
    private String picListUrl;
    @DatabaseField(columnName = "pic_mark_url")
    @SerializedName("picmarkurl")
    private String picMarkUrl;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "enabled")
    @SerializedName("isenable")
    private int enabled;
    @DatabaseField(columnName = "sort")
    @SerializedName("idx")
    private Integer sort;
    @DatabaseField(columnName = "kind")
    @Expose(deserialize = false, serialize = false)
    private Integer kind;//1:消息类型;2:实体景观类型;3:百科类型;4:攻略类型;5:平台反馈；6:ar反馈;
    public Bitmap bitmap;

    public DataType() {
    }

    protected DataType(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readInt();
        }
        if (in.readByte() == 0) {
            typeId = null;
        } else {
            typeId = in.readInt();
        }
        name = in.readString();
        picUrl = in.readString();
        linkH5Url = in.readString();
        remark = in.readString();
        enabled = in.readInt();
        sort = in.readInt();
        if (in.readByte() == 0) {
            kind = null;
        } else {
            kind = in.readInt();
        }
    }

    public static final Creator<DataType> CREATOR = new Creator<DataType>() {
        @Override
        public DataType createFromParcel(Parcel in) {
            return new DataType( in );
        }

        @Override
        public DataType[] newArray(int size) {
            return new DataType[size];
        }
    };

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicListUrl() {
        return picListUrl;
    }

    public void setPicListUrl(String picListUrl) {
        this.picListUrl = picListUrl;
    }

    public String getPicMarkUrl() {
        return picMarkUrl;
    }

    public void setPicMarkUrl(String picMarkUrl) {
        this.picMarkUrl = picMarkUrl;
    }

    public String getLinkH5Url() {
        return linkH5Url;
    }

    public void setLinkH5Url(String linkH5Url) {
        this.linkH5Url = linkH5Url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (_id == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( _id );
        }
        if (typeId == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( typeId );
        }
        dest.writeString( name );
        dest.writeString( picUrl );
        dest.writeString( linkH5Url );
        dest.writeString( remark );
        dest.writeInt( enabled );
        dest.writeInt( sort );
        if (kind == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeInt( kind );
        }
    }
}
