package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class VerifyCodeLoginResp extends BaseResponse {
    @SerializedName("caption")
    private String caption;
    @SerializedName("city")
    private String city;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("sex")
    private String sex;
    @SerializedName("Uid")
    private String id;
    @SerializedName("Ukey")
    private String key;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
