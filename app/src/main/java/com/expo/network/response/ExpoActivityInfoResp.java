package com.expo.network.response;

import com.expo.entity.ExpoActivityInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpoActivityInfoResp extends BaseResponse {
    public String Updatetime;
    @SerializedName("objlist")
    public List<ExpoActivityInfo> expoActivityInfos;
}
