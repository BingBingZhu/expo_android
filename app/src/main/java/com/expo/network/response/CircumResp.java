package com.expo.network.response;

import com.expo.entity.Circum;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CircumResp extends BaseResponse {
    @SerializedName("rsbinfo")
    public String circum;
}
