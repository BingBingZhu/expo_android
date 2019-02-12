package com.expo.entity;

import com.expo.db.dao.BaseDao;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SheduleInfo {
    @SerializedName("date")
    public String date;
    @SerializedName("gUpdateTimePensornal")
    public String gUpdateTimePensornal;
    @SerializedName("gUpdateTimeTeam")
    public String gUpdateTimeTeam;
    @SerializedName("venueObjs")
    public List<ScheduleVenue> venueObjs;
    @SerializedName("TimePartobjs")
    public Map<String, List<ScheduleTimeInfo>> timePartobjs;

    public void updateSheduleVenue(BaseDao dao) {
        for (ScheduleVenue sv : venueObjs) {
            sv.date = date;
        }
        dao.saveOrUpdateAll(venueObjs);
    }

    public void updateTimePartobjs(BaseDao dao) {
        for (String key : timePartobjs.keySet()) {
            //map.keySet()返回的是所有key的值
            List<ScheduleTimeInfo> list = timePartobjs.get(key);//得到每个key多对用value的值
            for (ScheduleTimeInfo st : list) {
                st.date = date;
                st.venueId = key;
            }
            dao.saveOrUpdateAll(list);
        }
    }

}
