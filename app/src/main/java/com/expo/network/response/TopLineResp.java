package com.expo.network.response;

import com.expo.entity.TopLineInfo;
import com.expo.entity.VenuesInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopLineResp extends BaseResponse {
    @SerializedName("Updatetime")
    public String updateTime;
    @SerializedName("Objlist")
    public List<TopLineInfo> topLine;
}
