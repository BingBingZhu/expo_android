package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class UpdateTimeResp extends BaseResponse {
    @SerializedName(value = "UpdateTime")
    public String updateTime;
    @SerializedName(value = "gCacheHeartInvTime")
    public String heartInvTime;//心跳间隔时间
    @SerializedName(value = "gCacheGetUpdateTimeInvTime")
    public String updateTimeInvTime;//数据更新间隔时间
    @SerializedName(value = "gCacheUpdateTimeAllType")
    public String allType;//所有类型数据变化时间
    @SerializedName(value = "gCacheUpdateTimeBadge")
    public String badge;//勋章数据更新时间
    @SerializedName(value = "gCacheUpdateTimeTopLine")
    public String topLine;//头条数据更新时间
    @SerializedName(value = "gCacheUpdateTimePanCam")
    public String panorama;//全景数据更新时间
    @SerializedName(value = "gCacheUpdateTimeRouter")
    public String router;//推荐路线更新时间
    //    @SerializedName(value = "gCacheUpdateTimeTourist")
//    public String tourist;//导游动作数据更新时间
    @SerializedName(value = "gCacheUpdateTimeTouristType")
    public String touristType;//导游类型数据更新时间
    @SerializedName(value = "gCacheUpdateTimeVenue")
    public String venue;//场馆数据更新时间
    @SerializedName(value = "gCacheUpdateTimeVenueType")
    public String venueType;//场馆类型更新时间
    @SerializedName(value = "gCacheUpdateTimeWiki")
    public String wiki;//百科数据更新时间
    @SerializedName(value = "gCacheUpdateTimeWikiType")
    public String wikiType;//百科类型更新时间
    @SerializedName(value = "gCacheUpdateTimeCommoninformation")
    public String commoninformation;//常用信息更新时间
    //    @SerializedName(value = "gCacheUpdateTimeSubject")
//    public String subject;//主题类型更新时间
    @SerializedName(value = "gCacheUpdateTimePark")
    public String parkList;//园区数据更新时间
    @SerializedName(value = "gCacheUpdateTimeQA")
    public String QAList;//问答数据更新时间
    @SerializedName(value = "gCacheUpdateTimePanLable")
    public String vrLableInfo;//全景标签数据更新时间
    @SerializedName(value = "gCacheUpdateTimeShowTimes")
    public String timeShowTimes;//园区活动更新时间
}
