package com.expo.network;

import com.expo.entity.User;
import com.expo.network.response.AllTypeResp;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CheckThirdIdRegisterStateResp;
import com.expo.network.response.CommonInfoResp;
import com.expo.network.response.EncyclopediasResp;
import com.expo.network.response.GetDistinguishPlantList_Rsb;
import com.expo.network.response.ParkResp;
import com.expo.network.response.RouteHotCountResp;
import com.expo.network.response.RichTextRsp;
import com.expo.network.response.RouteInfoResp;
import com.expo.network.response.VenueResp;
import com.expo.network.response.SubjectResp;
import com.expo.network.response.TopLineResp;
import com.expo.network.response.TouristTypeResp;
import com.expo.network.response.UpdateTimeResp;
import com.expo.network.response.UploadRsp;
import com.expo.network.response.UserHeartBeatResp;
import com.expo.network.response.VenuesTypeResp;
import com.expo.network.response.VerificationCodeResp;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.network.response.VersionInfoResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

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
     * 终端通知app运行接口，用于统计和消息发送
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
     * 用户登出
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/Userlogout")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> userlogout(@Body RequestBody requestBody);

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
    Observable<VenueResp> loadSpots(@Path("path") String path, @Body RequestBody requestBody);

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
     * @param part
     * @return
     */
    @Headers({
            "Accept: */*",
    })
    @Multipart
    @POST("Resources/UploadResource")
    Observable<UploadRsp> uploadFile(@Part MultipartBody.Part part);


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
     * 导游列表更新接口
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetTouristTypeList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<TouristTypeResp> loadTouristTypeList(@Body RequestBody requestBody);

    /**
     * 设施类型更新接口
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetVenuesTypeList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VenuesTypeResp> loadVenuesTypeList(@Body RequestBody requestBody);

    /**
     * 获得公园列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetParksList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ParkResp> loadParksList(@Body RequestBody requestBody);

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

    /**
     * 识花
     * @param url
     * @param params
     * @return
     */
    @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8", "Authorization: APPCODE 54c15d43df5c4e1fbe18bde79d40adc9"})
    @FormUrlEncoded
    @POST
    Observable<GetDistinguishPlantList_Rsb> distinguishPlant(@Url String url, @FieldMap Map<String, String> params);

    ////路线相关接口
    /**
     * 获取路线列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetRouterList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<RouteInfoResp> loadRouteInfo(@Body RequestBody requestBody);

    /**
     * 获取路线热度列表 id / hotscounts
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetRouterHotCountList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<RouteHotCountResp> getRouterHotCountList(@Body RequestBody requestBody);

    /**
     * 获取路线列表
     *
     * @param requestBody
     * @return
     */
    @POST("/Api/manage/AddRouterHotClick")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> addRouterHotClick(@Body RequestBody requestBody);

    /**
     * 获取头条列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetTopLineList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<TopLineResp> getTopLineList(@Body RequestBody requestBody);

    /**
     * 获取头条列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/AddVisitorService")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> addVisitorService(@Body RequestBody requestBody);

    /**
     * 终端获取富文本根据id
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetRichText")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<RichTextRsp> getRichText(@Body RequestBody requestBody);
}
