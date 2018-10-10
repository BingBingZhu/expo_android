package com.expo.network.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    public static final int ERROR_NO_ERROR = 200; //返回成功
    public static final int ERROR_REQUEST_JSON = 400; // 请求的json参数异常
    public static final int ERROR_EXP_ERROR = 302; //内部调用错误
    public static final int ERROR_NO_REG = 300; // 用户未注册
    public static final int ERROR_UKEY_ERROR = 301; //令牌错误
    public static final int ERROR_UID_UNLOGIN = 304; //用户未登录
    public static final int ERROR_RELOGIN = 303; //用户在另外一个手机上登录
    public static final int ERROR_AR_TASK_HAD_COMPLETE = 305; //任务已完成
    public static final int ERROR_DATA_UNEXISTSD = 306;        //无数据
    public static final int ERROR_NO_REQ_VERIFICATION_CODE = 411; // 未请求过校验码
    public static final int ERROR_VERIFICATION_CODE_ERROR = 412; // 校验码错误
    public static final int ERROR_EXP_VERIFICATION_CODE = 413; // 校验码失效
    public static final int ERROR_SMS_ERROR = 414; // 短信通道异常
    public static final int ERROR_ACCOUNT_UNENABLE = 415; // 账号被冻结
    public static final int ERROR_ID_NOT_EXIST = 416; // ID不存在
    public static final int ERROR_CODE_TIMES_LIMIT = 417; // 验证码次数上限


    @SerializedName("Resb")
    public Integer code;//200 ok   else error
    @SerializedName("RsbInfo")
    public String msg;//返回的提示信息
    @SerializedName("Ver")
    public String ver;

}
