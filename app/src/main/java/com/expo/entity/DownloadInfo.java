package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "download_info")
public class DownloadInfo implements Parcelable {
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;
    @DatabaseField(columnName = "app_ver")
    @SerializedName("appver")
    private String appVer;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "city")
    @SerializedName("city")
    private String city;
    @DatabaseField(columnName = "district")
    @SerializedName("district")
    private String district;
    @DatabaseField(columnName = "file_size")
    @SerializedName("filesize")
    private Long fileSize;
    @DatabaseField(columnName = "mission_id")
    @SerializedName("missionid")
    private String missionId;
    @DatabaseField(columnName = "park_id")
    @SerializedName("parkid")
    private String parkId;
    @DatabaseField(columnName = "park_name")
    @SerializedName("parkname")
    private String parkName;
    @DatabaseField(columnName = "province")
    @SerializedName("province")
    private String province;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "res_package")
    @SerializedName("respacked")
    private String resPackageVer;//资源包版本
    @DatabaseField(columnName = "res_url")
    @SerializedName("resurl")
    private String resUrl;
    @DatabaseField(columnName = "type")
    @SerializedName("type")
    private String type;//分类 id 1 全景 2 ar任务 3d  4 园区资源
    @DatabaseField(columnName = "curr_position")
    private long currPosition;
    @DatabaseField(columnName = "status")
    private int status;
    @DatabaseField(columnName = "local_path")
    private String localPath;

    public DownloadInfo() {
    }

    protected DownloadInfo(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        appVer = in.readString();
        caption = in.readString();
        city = in.readString();
        district = in.readString();
        if (in.readByte() == 0) {
            fileSize = null;
        } else {
            fileSize = in.readLong();
        }
        missionId = in.readString();
        parkId = in.readString();
        parkName = in.readString();
        province = in.readString();
        remark = in.readString();
        resPackageVer = in.readString();
        resUrl = in.readString();
        type = in.readString();
        currPosition = in.readLong();
        status = in.readInt();
        localPath = in.readString();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel in) {
            return new DownloadInfo( in );
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResPackageVer() {
        return resPackageVer;
    }

    public void setResPackageVer(String resPackageVer) {
        this.resPackageVer = resPackageVer;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(Long currPosition) {
        this.currPosition = currPosition;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public void copy(DownloadInfo info) {
        if (appVer != null ? !appVer.equals( info.appVer ) : info.appVer != null) {
            this.appVer = info.appVer;
        }
        if (caption != null ? !caption.equals( info.caption ) : info.caption != null) {
            this.caption = info.caption;
        }
        if (city != null ? !city.equals( info.city ) : info.city != null) {
            this.city = info.city;
        }
        if (district != null ? !district.equals( info.district ) : info.district != null) {
            this.district = info.district;
        }
        if (fileSize != null ? !fileSize.equals( info.fileSize ) : info.fileSize != null) {
            this.fileSize = info.fileSize;
        }
        if (missionId != null ? !missionId.equals( info.missionId ) : info.missionId != null) {
            this.missionId = info.missionId;
        }
        if (parkId != null ? !parkId.equals( info.parkId ) : info.parkId != null) {
            this.parkId = info.parkId;
        }
        if (parkName != null ? !parkName.equals( info.parkName ) : info.parkName != null) {
            this.parkName = info.parkName;
        }
        if (province != null ? !province.equals( info.province ) : info.province != null) {
            this.province = info.province;
        }
        if (remark != null ? !remark.equals( info.remark ) : info.remark != null) {
            this.remark = info.remark;
        }
        if (resPackageVer != null ? !resPackageVer.equals( info.resPackageVer ) : info.resPackageVer != null) {
            this.resPackageVer = info.resPackageVer;
        }
        if (resUrl != null ? !resUrl.equals( info.resUrl ) : info.resUrl != null) {
            this.resUrl = info.resUrl;
        }
        if (type != null ? !type.equals( info.type ) : info.type != null) {
            this.type = info.type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadInfo info = (DownloadInfo) o;

        if (id != null ? !id.equals( info.id ) : info.id != null) return false;
        if (appVer != null ? !appVer.equals( info.appVer ) : info.appVer != null) return false;
        if (caption != null ? !caption.equals( info.caption ) : info.caption != null) return false;
        if (city != null ? !city.equals( info.city ) : info.city != null) return false;
        if (district != null ? !district.equals( info.district ) : info.district != null)
            return false;
        if (fileSize != null ? !fileSize.equals( info.fileSize ) : info.fileSize != null)
            return false;
        if (missionId != null ? !missionId.equals( info.missionId ) : info.missionId != null)
            return false;
        if (parkId != null ? !parkId.equals( info.parkId ) : info.parkId != null) return false;
        if (parkName != null ? !parkName.equals( info.parkName ) : info.parkName != null)
            return false;
        if (province != null ? !province.equals( info.province ) : info.province != null)
            return false;
        if (remark != null ? !remark.equals( info.remark ) : info.remark != null) return false;
        if (resPackageVer != null ? !resPackageVer.equals( info.resPackageVer ) : info.resPackageVer != null)
            return false;
        if (resUrl != null ? !resUrl.equals( info.resUrl ) : info.resUrl != null) return false;
        return type != null ? !type.equals( info.type ) : info.type != null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (appVer != null ? appVer.hashCode() : 0);
        result = 31 * result + (caption != null ? caption.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (missionId != null ? missionId.hashCode() : 0);
        result = 31 * result + (parkId != null ? parkId.hashCode() : 0);
        result = 31 * result + (parkName != null ? parkName.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (resPackageVer != null ? resPackageVer.hashCode() : 0);
        result = 31 * result + (resUrl != null ? resUrl.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

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
        dest.writeString( appVer );
        dest.writeString( caption );
        dest.writeString( city );
        dest.writeString( district );
        if (fileSize == null) {
            dest.writeByte( (byte) 0 );
        } else {
            dest.writeByte( (byte) 1 );
            dest.writeLong( fileSize );
        }
        dest.writeString( missionId );
        dest.writeString( parkId );
        dest.writeString( parkName );
        dest.writeString( province );
        dest.writeString( remark );
        dest.writeString( resPackageVer );
        dest.writeString( resUrl );
        dest.writeString( type );
        dest.writeLong( currPosition );
        dest.writeInt( status );
        dest.writeString( localPath );
    }
}
