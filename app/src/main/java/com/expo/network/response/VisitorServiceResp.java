package com.expo.network.response;

import com.expo.entity.VisitorService;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VisitorServiceResp extends BaseResponse {
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("vslist")
    public List<VisitorService> vsList;
}
