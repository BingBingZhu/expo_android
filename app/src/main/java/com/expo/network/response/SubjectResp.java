package com.expo.network.response;

import com.expo.entity.Subject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectResp extends BaseResponse {
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("Objlist")
    public List<Subject> subjects;
}
