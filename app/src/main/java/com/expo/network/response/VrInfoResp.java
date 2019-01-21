package com.expo.network.response;

import com.expo.entity.VrInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VrInfoResp extends BaseResponse {
    public String updateTime;
    @SerializedName("Objlist")
    public List<VrInfo> vrInfos;
}
