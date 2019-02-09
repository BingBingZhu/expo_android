package com.expo.network;

import com.expo.entity.User;
import com.expo.network.response.AllTypeResp;
import com.expo.network.response.BadgeResp;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.CheckThirdIdRegisterStateResp;
import com.expo.network.response.CircumResp;
import com.expo.network.response.CommonInfoResp;
import com.expo.network.response.EncyclopediasResp;
import com.expo.network.response.ExpoActivityInfoResp;
import com.expo.network.response.GetDistinguishPlantList_Rsb;
import com.expo.network.response.PanResHotResp;
import com.expo.network.response.ParkResp;
import com.expo.network.response.PortalSiteResp;
import com.expo.network.response.RichTextRsp;
import com.expo.network.response.RouteHotCountResp;
import com.expo.network.response.RouteInfoResp;
import com.expo.network.response.ScheduleResp;
import com.expo.network.response.SocietyListResp;
import com.expo.network.response.SubjectResp;
import com.expo.network.response.TopLineResp;
import com.expo.network.response.TouristTypeResp;
import com.expo.network.response.UpdateTimeResp;
import com.expo.network.response.UploadRsp;
import com.expo.network.response.UserHeartBeatResp;
import com.expo.network.response.VenueResp;
import com.expo.network.response.VenuesTypeResp;
import com.expo.network.response.VerificationCodeResp;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.network.response.VersionInfoResp;
import com.expo.network.response.VisitorServiceResp;
import com.expo.network.response.VrInfoResp;
import com.expo.network.response.VrLableInfoResp;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
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
     * 徽章更新接口
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetBadgeList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BadgeResp> getBadgeList(@Body RequestBody requestBody);

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
     *
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
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetRichText")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<RichTextRsp> getRichText(@Body RequestBody requestBody);

    /**
     * 获取游客服务列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/findVisitorServiceList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VisitorServiceResp> findVisitorServiceList(@Body RequestBody requestBody);

    /**
     * 获取社交圈数据
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetMySocietyList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<SocietyListResp> getMySocietyList(@Body RequestBody requestBody);

    /**
     * 获取社交圈数据
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetSocietyListFilter")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<SocietyListResp> getSocietyListFilter(@Body RequestBody requestBody);

    /**
     * 新增一个圈
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/AddSociety")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> addSociety(@Body RequestBody requestBody);

    /**
     * 为某个圈点赞
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/DeleteSociety")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> deleteSociety(@Body RequestBody requestBody);

    /**
     * 为某个圈点赞
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/SetEnjoySociety")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> setEnjoySociety(@Body RequestBody requestBody);

    /**
     * 为某个圈增加观看数
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/SetSocietyViews")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> setSocietyViews(@Body RequestBody requestBody);

    /**
     * 使用优惠卷
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/SetUsedCoupon")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> setUsedCoupon(@Body RequestBody requestBody);

    /**
     * 增加积分（1查看百科详情 2 分享百科）
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/UserScoreChange")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> userScoreChange(@Body RequestBody requestBody);

    /**
     * 增加积分（1查看百科详情 2 分享百科）
     *
     * @param requestBody
     * @return
     */
    @POST("/bigdataapi/submitTouristRecord")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> submitTouristRecord(@Body RequestBody requestBody);


    /**
     * 票务注册
     *
     * @param url
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<HashMap<String, String>> registerTicket(@Url String url, @Field("phone") String phone, @Field("source") String source);

    /**
     * 获取全景资源列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetPanCamList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VrInfoResp> getPanCamList(@Body RequestBody requestBody);

    /**
     * 获取全景标签资源列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetPanLableList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VrLableInfoResp> getPanLableList(@Body RequestBody requestBody);

    /**
     * 获取全景标签资源列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetPanResHotViews")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<PanResHotResp> getPanResHot(@Body RequestBody requestBody);

    /**
     * 为某个全景资源增加观看数
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/SetPanResViews")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<BaseResponse> setPanResViews(@Body RequestBody requestBody);

    /**
     * 为某个全景资源增加观看数
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetShowTimesList_Rsb")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ExpoActivityInfoResp> getShowTimesList_Rsb(@Body RequestBody requestBody);

    /**
     * 获取全景标签资源列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetBussinessCircleListByParams")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<CircumResp> getBussinessCircleListByParams(@Body RequestBody requestBody);

    /**
     * 获取全景标签资源列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetScheduleVenList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ScheduleResp> getScheduleVenList(@Body RequestBody requestBody);

    /**
     * 获取全景标签资源列表
     *
     * @param requestBody
     * @return
     */
    @POST("Terminal/GetPortalSiteList")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<PortalSiteResp> loadPortalList(@Body RequestBody requestBody);
}
