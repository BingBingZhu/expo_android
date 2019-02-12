package com.expo.network.response;

import com.expo.db.dao.BaseDao;
import com.expo.entity.ScheduleTimeInfo;
import com.expo.entity.ScheduleVenue;
import com.expo.entity.SheduleInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SheduleInfoResp extends BaseResponse {
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("objlist")
    public List<SheduleInfo> objList;

    public void updateSheduleInfo(BaseDao dao) {
        dao.clear(ScheduleVenue.class);
        dao.clear(ScheduleTimeInfo.class);
        for (SheduleInfo si : objList) {
            si.updateSheduleVenue(dao);
            si.updateTimePartobjs(dao);
        }
    }

}
