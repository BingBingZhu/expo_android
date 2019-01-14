package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "visitor_service")
public class VisitorService implements Parcelable {

    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private int id;

    @DatabaseField(columnName = "code_id")
    @SerializedName("codeid")
    private String codeId ;

    @DatabaseField(columnName = "coordinate_assist")
    @SerializedName("coordinate_assist")
    private String coordinateAssist ;

    @DatabaseField(columnName = "counttry_code")
    @SerializedName("counttrycode")
    private String counttryCode;

    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime ;

    @DatabaseField(columnName = "dispose_type")
    @SerializedName("dispose_type")
    private String disposeType;

    @DatabaseField(columnName = "gps_latitude")
    @SerializedName("gps_latitude")
    private String gpsLatitude;

    @DatabaseField(columnName = "gps_longitude")
    @SerializedName("gps_longitude")
    private String gpsLongitude;

    @DatabaseField(columnName = "img_url1")
    @SerializedName("img_url1")
    private String imgUrl1 ;

    @DatabaseField(columnName = "img_url2")
    @SerializedName("img_url2")
    private String imgUrl2 ;

    @DatabaseField(columnName = "img_url3")
    @SerializedName("img_url3")
    private String imgUrl3 ;

    @DatabaseField(columnName = "lock_id")
    @SerializedName("lock_id")
    private String lockId ;

    @DatabaseField(columnName = "lock_name")
    @SerializedName("lock_name")
    private String lockName ;

    @DatabaseField(columnName = "lock_state")
    @SerializedName("lock_state")
    private String lockState ;

    @DatabaseField(columnName = "phone")
    @SerializedName("phone")
    private String phone;

    @DatabaseField(columnName = "platform")
    @SerializedName("platform")
    private String platform;

    @DatabaseField(columnName = "service_type")
    @SerializedName("servicetype")
    private String serviceType;

    @DatabaseField(columnName = "situation")
    @SerializedName("situation")
    private String situation;

    @DatabaseField(columnName = "state")
    @SerializedName("state")
    private String state ;

    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime ;

    @DatabaseField(columnName = "user_id")
    @SerializedName("userid")
    private String userId;

    @DatabaseField(columnName = "user_name")
    @SerializedName("username")
    private String userName;

    @DatabaseField(columnName = "complain_type")
    @SerializedName("complaintype")
    private String complainType ;

    @Expose()
    private int times = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCoordinateAssist() {
        return coordinateAssist;
    }

    public void setCoordinateAssist(String coordinateAssist) {
        this.coordinateAssist = coordinateAssist;
    }

    public String getCounttryCode() {
        return counttryCode;
    }

    public void setCounttryCode(String counttryCode) {
        this.counttryCode = counttryCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDisposeType() {
        return disposeType;
    }

    public void setDisposeType(String disposeType) {
        this.disposeType = disposeType;
    }

    public String getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(String gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public String getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(String gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComplainType() {
        return complainType;
    }

    public void setComplainType(String complainType) {
        this.complainType = complainType;
    }

    public VisitorService() {
    }

    protected VisitorService(Parcel in) {
        id = in.readInt();
        codeId = in.readString();
        coordinateAssist = in.readString();
        counttryCode = in.readString();
        createTime = in.readString();
        disposeType = in.readString();
        gpsLatitude = in.readString();
        gpsLongitude = in.readString();
        imgUrl1 = in.readString();
        imgUrl2 = in.readString();
        imgUrl3 = in.readString();
        lockId = in.readString();
        lockName = in.readString();
        lockState = in.readString();
        phone = in.readString();
        platform = in.readString();
        serviceType = in.readString();
        situation = in.readString();
        state = in.readString();
        updateTime = in.readString();
        userId = in.readString();
        userName = in.readString();
        complainType = in.readString();
        times = in.readInt();
    }

    public static final Creator<VisitorService> CREATOR = new Creator<VisitorService>() {
        @Override
        public VisitorService createFromParcel(Parcel in) {
            return new VisitorService(in);
        }

        @Override
        public VisitorService[] newArray(int size) {
            return new VisitorService[size];
        }
    };

    public synchronized void addTimes() {
        times++;
    }

    public int getTimes() {
        return times;
    }

    public Map toJson() {
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        return gson.fromJson(gson.toJson(this), map.getClass());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(codeId);
        dest.writeString(coordinateAssist);
        dest.writeString(counttryCode);
        dest.writeString(createTime);
        dest.writeString(disposeType);
        dest.writeString(gpsLatitude);
        dest.writeString(gpsLongitude);
        dest.writeString(imgUrl1);
        dest.writeString(imgUrl2);
        dest.writeString(imgUrl3);
        dest.writeString(lockId);
        dest.writeString(lockName);
        dest.writeString(lockState);
        dest.writeString(phone);
        dest.writeString(platform);
        dest.writeString(serviceType);
        dest.writeString(situation);
        dest.writeString(state);
        dest.writeString(updateTime);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(complainType);
        dest.writeInt(times);
    }
}
