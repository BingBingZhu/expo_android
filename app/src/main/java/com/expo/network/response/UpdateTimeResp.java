package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class UpdateTimeResp extends BaseResponse {
    @SerializedName(value = "UpdateTime")
    public String updateTime;
    @SerializedName(value = "gCacheUpdateTimeRouter")
    public String router;
    @SerializedName(value = "gCacheUpdateTimeTourist")
    public String tourist;
    @SerializedName(value = "gCacheUpdateTimeTouristType")
    public String touristType;
    @SerializedName(value = "gCacheUpdateTimeVenue")
    public String actualScene;
    @SerializedName(value = "gCacheUpdateTimeVenueType")
    public String scenicSpotType;
    @SerializedName(value = "gCacheUpdateTimeWiki")
    public String wiki;
    @SerializedName(value = "gCacheUpdateTimeWikiType")
    public String wikiType;
    @SerializedName(value = "gCacheUpdateTimeCommoninformation")
    public String commoninformation;
    @SerializedName(value = "gCacheUpdateTimeSubject")
    public String subject;
    @SerializedName(value = "gCacheUpdateTimePark")
    public String parkList;
}
