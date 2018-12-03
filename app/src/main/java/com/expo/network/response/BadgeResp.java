package com.expo.network.response;

import com.expo.entity.Badge;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BadgeResp extends BaseResponse {
    @SerializedName("Objlist")
    public List<Badge> badges;
    @SerializedName("Updatetime")
    public String updatetime;
}
