package com.expo.network.response;

import com.expo.entity.CommonInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonInfoResp extends BaseResponse {
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("ObjList")
    public List<CommonInfo> commonInfos;
}
