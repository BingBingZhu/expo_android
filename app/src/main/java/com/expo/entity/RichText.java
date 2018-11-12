package com.expo.entity;

import com.google.gson.annotations.SerializedName;

public class RichText {

    @SerializedName("caption")
    private String caption;
    @SerializedName("content")
    private String content;
    @SerializedName("createtime")
    private String createTime;
    @SerializedName("id")
    private Integer id;
    @SerializedName("remark")
    private String remark;
    @SerializedName("type")
    private String type;
    @SerializedName("updatetime")
    private String updateTime;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
