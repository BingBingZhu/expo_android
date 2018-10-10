package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class VerificationCodeResp extends BaseResponse {
    @SerializedName("RequestTime")
    public String requestTime;
    @SerializedName("VerificationCode")
    public String verificationCode;
}
