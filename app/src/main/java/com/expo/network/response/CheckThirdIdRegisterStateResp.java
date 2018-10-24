package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class CheckThirdIdRegisterStateResp extends BaseResponse {
    @SerializedName("Uid")
    public String uid;
    @SerializedName("Ukey")
    public String ukey;
    @SerializedName("state")
    public int state;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
