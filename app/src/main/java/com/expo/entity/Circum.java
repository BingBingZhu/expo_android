package com.expo.entity;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Circum/* implements Parcelable*/ {

    @SerializedName("business_id")
    private Integer businessId	;           //integer	店铺ID

    @SerializedName("business_url")
    private String businessUrl	;           //string	店铺详细信息跳转链接

    @SerializedName("name")
    private String name	;           //string	店铺名

    @SerializedName("branch_name")
    private String branchName	;           //string	店铺分店名，可能为空；例如：知春路店

    @SerializedName("address")
    private String address	;           //string	店铺地址

    @SerializedName("telephone")
    private String telephone	;           //string	带区号的电话号码

    @SerializedName("categories")
    private String[] categories	;           //list	所属分类信息列表，使用分类树结构，如[“美食,火锅”, “酒店,婚宴酒店”]

    @SerializedName("popular")
    private Integer popular	;           //integer	店铺人气

    @SerializedName("status")
    private Integer status	;           //integer	店铺状态，1代表正常营业，2代表暂停营业（比如停业装修、临时关门）

    @SerializedName("service")
    private float service	;           //float	服务评分，十分制

    @SerializedName("decoration")
    private float decoration	;           //float	环境评分，十分制

    @SerializedName("taste")
    private float taste	;           //float	口味评分，十分制

    @SerializedName("business_hour")
    private String businessHour	;           //string	营业时间

    @SerializedName("introduction")
    private String introduction	;           //string	店铺介绍

//    @SerializedName("s_photo_urls")
//    private Void smallPhoto_urls	;           //list	小图列表，尺寸为278×200

    @SerializedName("photo_urls")
    private String photoUrls	;           //list	大图列表，尺寸为800×800

    @SerializedName("avg_price")
    private Integer avgPrice	;           //integer	人均价格，单位:元，若没有人均，返回-1

    @SerializedName("avg_rating")
    private float avgRating	;           //float	星级评分（最高5星）

    @SerializedName("latitude")
    private double latitude	;           //float	纬度

    @SerializedName("longitude")
    private double longitude	;           //float	经度

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public Integer getPopular() {
        return popular;
    }

    public void setPopular(Integer popular) {
        this.popular = popular;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public float getService() {
        return service;
    }

    public void setService(float service) {
        this.service = service;
    }

    public float getDecoration() {
        return decoration;
    }

    public void setDecoration(float decoration) {
        this.decoration = decoration;
    }

    public float getTaste() {
        return taste;
    }

    public void setTaste(float taste) {
        this.taste = taste;
    }

    public String getBusinessHour() {
        return businessHour;
    }

    public void setBusinessHour(String businessHour) {
        this.businessHour = businessHour;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

//    public Void getSmallPhoto_urls() {
//        return smallPhoto_urls;
//    }
//
//    public void setSmallPhoto_urls(Void smallPhoto_urls) {
//        this.smallPhoto_urls = smallPhoto_urls;
//    }

    public String getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
