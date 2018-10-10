package com.expo.network.response;

import com.expo.entity.Encyclopedias;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EncyclopediasResp extends BaseResponse {
    @SerializedName("WikiList")
    public List<Encyclopedias> encyclopedias;
    @SerializedName("UpdateTime")
    public String updateTime;
}
