package com.expo.network.response;

import com.expo.entity.Plant;

import java.util.List;

public class GetDistinguishPlantList_Rsb extends BaseResponse {
    public String Status;
    public String Message;
    public List<Plant> Result;

    public GetDistinguishPlantList_Rsb() {
        this.code = 200;
    }
}
