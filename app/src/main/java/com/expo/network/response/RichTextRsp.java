package com.expo.network.response;

import com.expo.entity.RichText;
import com.google.gson.annotations.SerializedName;

public class RichTextRsp extends BaseResponse {

    @SerializedName("obj")
    public RichText richText;

}
