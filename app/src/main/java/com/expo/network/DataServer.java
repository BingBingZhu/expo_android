package com.expo.network;

import com.expo.entity.User;
import com.expo.network.response.AllTypeResp;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CheckThirdIdRegisterStateResp;
import com.expo.network.response.CommonInfoResp;
import com.expo.network.response.EncyclopediasResp;
import com.expo.network.response.SpotsResp;
import com.expo.network.response.SubjectResp;
import com.expo.network.response.UpdateTimeResp;
import com.expo.network.response.UploadRsp;
import com.expo.network.response.UserHeartBeatResp;
import com.expo.network.response.VerificationCodeResp;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.network.response.VersionInfoResp;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 所有业务网络请求的接口
 */
public interface DataServer {

    /**
     * 检查指定数据的更新时间
     *
     * @param path        业务地址
     * @param requestBody 上传参数
     * @return rxjava主题对象
     */
    @POST("Terminal/{path}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<UpdateTimeResp> checkUpdateTime(@Path("path") String path, @Body RequestBody requestBody);

    /**
     * 检查指定数据的更新时间
     *
     * @param requestBody 上传参数
     * @return rxjava主题对象
     */
    @POST("Terminal/UserlogAppRun")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> userlogAppRun(@Body RequestBody requestBody);


    /**
     * 通过手机号获取验证码
     *
     * @param requestBody 上传参数
     * @return rxjava主题对象
     */
    @POST("Terminal/RequestVerificationCode")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VerificationCodeResp> getVerificationCode(@Body RequestBody requestBody);

    /**
     * 验证码登录
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/VerifyCodeLogin")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VerifyCodeLoginResp> verifyCodeLogin(@Body RequestBody requestBody);

    /**
     * 检查第三方openid是否已注册过
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/CheckThirdIdRegisterState")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<CheckThirdIdRegisterStateResp> checkThirdIdRegisterState(@Body RequestBody requestBody);

    /**
     * 第三方登录
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/RequestThirdLogin")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VerifyCodeLoginResp> requestThirdLogin(@Body RequestBody requestBody);

    /**
     * 获取景点数据
     *
     * @param path        景点数据的具体路径
     * @param requestBody
     * @return
     */
    @POST("Terminal/{path}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<SpotsResp> loadSpots(@Path("path") String path, @Body RequestBody requestBody);

    /**
     * 获取通用信息数据
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetCommoninformationList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<CommonInfoResp> loadCommonInfos(@Body RequestBody requestBody);

    /**
     * 获取主题数据
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetSubjectList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<SubjectResp> loadSubjects(@Body RequestBody requestBody);

    /**
     * 获取用户信息
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetUserInfo")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<User> loadUserInfo(@Body RequestBody requestBody);


    /**
     * 修改用户信息
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/SetUserInfo")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> setUserInfo(@Body RequestBody requestBody);


    /**
     * 上传文件
     *
     * @param requestBody
     * @return
     */
    @POST("Resources/UploadResource")
    Observable<UploadRsp> uploadFile(@Body RequestBody requestBody);


    /**
     * 所有数据是否有更新的接口
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetAllUpdateTimeList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<UpdateTimeResp> checkUpdateTime(@Body RequestBody requestBody);

    /**
     * 百科数据更新接口
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetWikiCaptionsList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<EncyclopediasResp> loadEncyclopedias(@Body RequestBody requestBody);

    /**
     * 心跳接口
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/UserHeartBeat")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<UserHeartBeatResp> sendHeartBeat(@Body RequestBody requestBody);


    /**
     * 获取app更新信息
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetAppUpdateInfo_Rsb")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VersionInfoResp> getAppUpdateInfo(@Body RequestBody requestBody);


    /**
     * 意见反馈
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/SubmitFeedback")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> submitFeedback(@Body RequestBody requestBody);

    /**
     * 获取通用信息数据
     *
     * @param requestBody
     * @return
     */
    @POST("Types/GetAllTypesListFilter")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<AllTypeResp> loadAllTypes(@Body RequestBody requestBody);
}
