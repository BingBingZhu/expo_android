package com.expo.network.response;

import com.expo.entity.DataType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllTypeResp extends BaseResponse {
    @SerializedName("messageTypeLst")
    public List<DataType> messageTypes;
    @SerializedName("wikitypeLst")
    public List<DataType> wikiTypes;
    @SerializedName("venueTypeLst")
    public List<DataType> venueTypes;
    @SerializedName("feedbackTypeLst")
    public List<DataType> feedbackTypes;
}
