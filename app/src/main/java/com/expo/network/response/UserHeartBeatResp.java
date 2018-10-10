package com.expo.network.response;

import com.expo.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LS on 2017/11/14.
 */

public class UserHeartBeatResp extends BaseResponse {
    public String UpdateTime;
    public List<Message> MessageList = new ArrayList<>();
}
