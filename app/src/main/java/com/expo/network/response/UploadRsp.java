package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class UploadRsp extends BaseResponse {
    @SerializedName(value = "FullUrl")
    public String fullUrl;
    @SerializedName(value = "ShortUrl")
    public String shortUrl;
}
