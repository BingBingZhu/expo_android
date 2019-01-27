package com.expo.network.response;

import com.expo.entity.VrLableInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VrLableInfoResp extends BaseResponse {
    public String updateTime;
    @SerializedName(value = "Objlist", alternate = "objlist")
    public List<VrLableInfo> vrLableInfos;
}
