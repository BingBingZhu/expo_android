package com.expo.network.response;

import com.expo.entity.VenuesInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VenuesInfoResp extends BaseResponse {
    @SerializedName("Updatetime")
    public String updateTime;
    @SerializedName("VenuesList")
    public List<VenuesInfo> venuesList;
}
