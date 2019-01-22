package com.expo.network.response;

import com.expo.entity.Park;
import com.expo.entity.Tuple;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PanResHotResp extends BaseResponse {

    @SerializedName("Clickinfos")
    public List<Tuple> tupleList;
}
