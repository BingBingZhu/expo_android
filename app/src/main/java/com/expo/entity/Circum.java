package com.expo.entity;

public class Circum {
    private Long id;
    private String caption;
    private String captionEn;
    private int type;   // 周边类型 1美食 2酒店 3购物 4交通 5景区
    private double lat;
    private double lng;
    private String phone;
    private String url;
    private String score;
    private String price;
    private String address;

//    business_id	integer	店铺ID
//    business_url	string	店铺详细信息跳转链接
//    name	string	店铺名
//    branch_name	string	店铺分店名，可能为空；例如：知春路店
//    address	string	店铺地址
//    telephone	string	带区号的电话号码
//    categories	list	所属分类信息列表，使用分类树结构，如[“美食,火锅”, “酒店,婚宴酒店”]
//    popular	integer	店铺人气
//    status	integer	店铺状态，1代表正常营业，2代表暂停营业（比如停业装修、临时关门）
//    service	float	服务评分，十分制
//    decoration	float	环境评分，十分制
//    taste	float	口味评分，十分制
//    business_hour	string	营业时间
//    introduction	string	店铺介绍
//    s_photo_urls	list	小图列表，尺寸为278×200
//    photo_urls	list	大图列表，尺寸为800×800
//    avg_price	integer	人均价格，单位:元，若没有人均，返回-1
//    avg_rating	float	星级评分（最高5星）
//    latitude	float	纬度
//    longitude	float	经度

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

    public String getCaptionEn() {
        return captionEn;
    }

    public void setCaptionEn(String captionEn) {
        this.captionEn = captionEn;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
