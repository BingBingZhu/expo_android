package com.expo.network.response;

import com.expo.entity.QAd;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QAResp extends BaseResponse {
    @SerializedName("qadlist")
    public List<QAd> qadlist;
}
