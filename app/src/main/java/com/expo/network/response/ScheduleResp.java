package com.expo.network.response;

import com.expo.entity.Schedule;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleResp extends BaseResponse {
    @SerializedName("Updatetime")
    public String updateTime;
    @SerializedName("objlist")
    public List<Schedule> schedules;
}
