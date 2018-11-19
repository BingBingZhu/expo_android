//package com.expo.adapters;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//public class SceneAndEncy implements Parcelable {
//
//    private String caption;
//    private String remark;
//    private String picUrl;
//    private Integer recommend;
//
//    public SceneAndEncy() {
//    }
//
//    public SceneAndEncy(String caption, String remark, String picUrl, Integer recommend) {
//        this.caption = caption;
//        this.remark = remark;
//        this.picUrl = picUrl;
//        this.recommend = recommend;
//    }
//
//    public String getCaption() {
//        return caption;
//    }
//
//    public void setCaption(String caption) {
//        this.caption = caption;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//    public String getEncy() {
//        return picUrl;
//    }
//
//    public void setPicUrl(String picUrl) {
//        this.picUrl = picUrl;
//    }
//
//    public Integer getRecommend() {
//        return recommend;
//    }
//
//    public void setRecommend(Integer recommend) {
//        this.recommend = recommend;
//    }
//
//    protected SceneAndEncy(Parcel in) {
//    }
//
//    public static final Creator<SceneAndEncy> CREATOR = new Creator<SceneAndEncy>() {
//        @Override
//        public SceneAndEncy createFromParcel(Parcel in) {
//            return new SceneAndEncy(in);
//        }
//
//        @Override
//        public SceneAndEncy[] newArray(int size) {
//            return new SceneAndEncy[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//    }
//}
