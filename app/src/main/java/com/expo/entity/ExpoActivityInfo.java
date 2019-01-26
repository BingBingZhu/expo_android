package com.expo.entity;

import com.blankj.utilcode.util.TimeUtils;
import com.expo.utils.Constants;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;

@DatabaseTable(tableName = "expo_activity_info")
public class ExpoActivityInfo {


    /**
     * canenable : 0
     * caption : string
     * captionen : string
     * checkstate : 0
     * createtime : string
     * enablestate : 0
     * enddate : string
     * id : 0
     * islinked : 0
     * isrecommended : 0
     * lat : string
     * linkh5url : string
     * linkh5urlen : string
     * linkid : string
     * lon : string
     * recommendedidx : 0
     * remark : string
     * remarken : string
     * startdate : string
     * times : string
     * updatetime : string
     */

    @DatabaseField(columnName = "can_enable")
    @SerializedName("canenable")
    private int canEnable;
    @DatabaseField(columnName = "caption")
    @SerializedName("caption")
    private String caption;
    @DatabaseField(columnName = "caption_en")
    @SerializedName("captionen")
    private String captionEn;
    @DatabaseField(columnName = "check_state")
    @SerializedName("checkstate")
    private int checkState;
    @DatabaseField(columnName = "create_time")
    @SerializedName("createtime")
    private String createTime;
    @DatabaseField(columnName = "enablestate")
    @SerializedName("enablestate")
    private int enablestate;
    @DatabaseField(columnName = "end_date")
    @SerializedName("enddate")
    private String endDate;
    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private int id;
    @DatabaseField(columnName = "is_link_ed")
    @SerializedName("islinked")
    private int isLinkEd;
    @DatabaseField(columnName = "is_recommended")
    @SerializedName("isrecommended")
    private int isRecommended;
    @DatabaseField(columnName = "lat")
    @SerializedName("lat")
    private String lat;
    @DatabaseField(columnName = "link_h5_url")
    @SerializedName("linkh5url")
    private String linkH5Url;
    @DatabaseField(columnName = "link_h5_url_en")
    @SerializedName("linkh5urlen")
    private String linkH5UrLen;
    @DatabaseField(columnName = "link_id")
    @SerializedName("linkid")
    private String linkId;
    @DatabaseField(columnName = "lon")
    @SerializedName("lon")
    private String lon;
    @DatabaseField(columnName = "recommended_idx")
    @SerializedName("recommendedidx")
    private int recommendedIdx;
    @DatabaseField(columnName = "remark")
    @SerializedName("remark")
    private String remark;
    @DatabaseField(columnName = "remark_en")
    @SerializedName("remarken")
    private String remarkEn;
    @DatabaseField(columnName = "start_date")
    @SerializedName("startdate")
    private String startDate;
    @DatabaseField(columnName = "times")
    @SerializedName("times")
    private String times;
    @DatabaseField(columnName = "update_time")
    @SerializedName("updatetime")
    private String updateTime;
    @DatabaseField(columnName = "start_time")
    private Long startTime;
    @DatabaseField(columnName = "end_time")
    private Long endTime;
    @DatabaseField(columnName = "time_interval")
    private int timeInterval;

    public int getCanEnable() {
        return canEnable;
    }

    public void setCanEnable(int canEnable) {
        this.canEnable = canEnable;
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

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getEnablestate() {
        return enablestate;
    }

    public void setEnablestate(int enablestate) {
        this.enablestate = enablestate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        setEndTime(TimeUtils.string2Millis(endDate, new SimpleDateFormat("yyyy-MM")));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsLinkEd() {
        return isLinkEd;
    }

    public void setIsLinkEd(int isLinkEd) {
        this.isLinkEd = isLinkEd;
    }

    public int getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(int isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLinkH5Url() {
        return linkH5Url;
    }

    public void setLinkH5Url(String linkH5Url) {
        this.linkH5Url = linkH5Url;
    }

    public String getLinkH5UrLen() {
        return linkH5UrLen;
    }

    public void setLinkH5UrLen(String linkH5UrLen) {
        this.linkH5UrLen = linkH5UrLen;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getRecommendedIdx() {
        return recommendedIdx;
    }

    public void setRecommendedIdx(int recommendedIdx) {
        this.recommendedIdx = recommendedIdx;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkEn() {
        return remarkEn;
    }

    public void setRemarkEn(String remarkEn) {
        this.remarkEn = remarkEn;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        setStartTime(TimeUtils.string2Millis(startDate, new SimpleDateFormat("yyyy-MM")));
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
        String[] timeArray = times.split("/");
        int interval = 0;
        for (int i = 0; i < timeArray.length; i++) {
            String start = timeArray[i].split("-")[0];
            Long time = TimeUtils.string2Millis(start, new SimpleDateFormat("mm:ss"));
            if (time <= Constants.TimeType.MORNING) {
                interval = interval | 1 << 0;
            } else if (time <= Constants.TimeType.AFTERNOON) {
                interval = interval | 1 << 1;
            } else {
                interval = interval | 1 << 2;
            }
        }
        setTimeInterval(interval);
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }
}
