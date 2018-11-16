package com.expo.network.response;

import com.expo.entity.VenuesType;
import com.expo.network.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VenuesTypeResp extends BaseResponse {

    @SerializedName("Updatetime")
    public String updateTime;
    @SerializedName("VenuesTypeList")
    public List<VenuesType> venuesList;
}
