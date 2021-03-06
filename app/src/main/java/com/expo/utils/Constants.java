package com.expo.utils;

import android.os.Environment;

import com.expo.R;
import com.expo.entity.Badge;
import com.expo.entity.CommonInfo;
import com.expo.entity.Contacts;
import com.expo.entity.CustomRoute;
import com.expo.entity.DataType;
import com.expo.entity.DownloadInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.FootPrint;
import com.expo.entity.Message;
import com.expo.entity.Park;
import com.expo.entity.PortalSite;
import com.expo.entity.QAd;
import com.expo.entity.QAt;
import com.expo.entity.RouteInfo;
import com.expo.entity.Schedule;
import com.expo.entity.ScheduleTimeInfo;
import com.expo.entity.ScheduleVenue;
import com.expo.entity.Subject;
import com.expo.entity.TopLineInfo;
import com.expo.entity.TouristType;
import com.expo.entity.Track;
import com.expo.entity.Tuple;
import com.expo.entity.User;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.entity.VisitorService;
import com.expo.entity.VrInfo;
import com.expo.entity.VrLableInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public interface Constants {
    /**
     * 存放SharePreference中使用到的KEY
     */
    class Prefs {
        public static final String KEY_LAST_MESSAGE_TIME = "key_last_message_time";
        public static final String KEY_SCENIC_SPOT_UPDATE_TIME = "key_scenic_spot_update_time";
        public static final String KEY_ACTUAL_SCENE_UPDATE_TIME = "key_actual_scene_update_time";
        public static final String KEY_COMMON_INFO_UPDATE_TIME = "key_common_info_update_time";
        public static final String KEY_SUBJECT_UPDATE_TIME = "key_subject_update_time";
        public static final String KEY_ENCYCLOPEDIAS_UPDATE_TIME = "key_wiki_update_time";
        public static final String KEY_TOURIST_TYPE_UPDATE_TIME = "key_tourist_type_list_update_time";
        public static final String KEY_SCENIC_SPOT_TYPE_UPDATE_TIME = "key_scenic_spot_type_update_time";
        public static final String KEY_PARK_UPDATE_TIME = "key_park_update_time";
        public static final String KEY_QA_UPDATE_TIME = "key_qa_update_time";
        public static final String KEY_BADGE_UPDATE_TIME = "key_badge_update_time";
        public static final String KEY_GUIDE_SHOWN = "key_guide_shown";
        public static final String KEY_LANGUAGE_CHOOSE = "key_language_choose";
        public static final String KEY_SHOW_SELECT_LANGUAGE = "key_app_first_use";
        public static final String KEY_HISTORY = "key_history";
        public static final String KEY_COUNTRY_CODE = "key_country_code";
        public static final String KEY_IS_OPEN_SLIDINGDRAWER = "key_is_open_slidingdrawer";
        public static final String KEY_HEART_INV_TIME = "key_heart_inv_time";
        public static final String KEY_UPDATE_TIME_INV_TIME = "key_update_time_inv_time";
        public static final String KEY_ALL_TYPE_UPDATE_TIME = "key_all_type_update_time";
        public static final String KEY_TOP_LINE_UPDATE_TIME = "key_top_line_update_time";
        public static final String KEY_VENUES_UPDATE_TIME = "key_venues_update_time";
        public static final String KEY_ROUTES_UPDATE_TIME = "key_routes_update_time";
        public static final String KEY_VR_INFO_UPDATE_TIME = "key_vr_info_update_time";
        public static final String KEY_VR_LABLE_INFO_UPDATE_TIME = "key_vr_lable_info_update_time";
        public static final String KEY_EXPO_ACTIVITY_UPDATE_TIME = "key_expo_activity_update_time";
        public static final String KEY_MAP_ON_OFF = "key_map_on_off";
        public static final String KEY_TRACK_ON_OFF = "key_track_on_off";
        public static final String KEY_MAP_PATTERN = "key_map_pattern";
        public static final String KEY_MODULE_ON_OFF = "key_module_on_off ";
        public static final String KEY_RUN_UP_COUNT = "key_run_up_count";
        public static final String KEY_RAW_SELECTOR_POSITION = "key_raw_selector_position";
        public static final String KEY_ROUTE_INDEX = "key_route_index";
        public static final String KEY_TRACK_UPDATE_TIME = "key_track_update_time";
        // 定位模式（int） 0：高精度  1：仅GPRS  2：低功耗
        public static final String KEY_LOCATION_MODE = "key_location_mode";
    }

    /**
     * 存放页面跳转中传递参数使用到的KEY
     */
    class EXTRAS {
        public static final String EXTRA_TITLE = "extra_title";
        public static final String EXTRA_URL = "extra_url";
        public static final String EXTRA_LOGIN_STATE = "extra_login_state";
        public static final String EXTRA_ID = "extra_id";
        public static final String EXTRA_SPOT_ID = "extra_spot_id";
        public static final String EXTRAS = "extras";
        public static final String EXTRA_LONGITUDE = "extra_longitude";
        public static final String EXTRA_LATITUDE = "extra_latitude";
        public static final String EXTRA_TEMPLATE_TYPE = "extra_template_type";
        public static final String EXTRA_DATA_ID = "extra_data_id";
        public static final String EXTRA_SHOW_TITLE = "extra_show_title";
        public static final String EXTRA_TITLE_COLOR_STYLE = "extra_title_color_style";
        public static final String EXTRA_USER_SCORE = "extra_user_score";
        public static final String EXTRA_SELECT_CONTACTS = "extra_select_contacts";
        public static final String EXTRA_SELECT_CONTACTS_MAX_COUNT = "extra_select_contacts_max_count";
        public static final String EXTRA_TRACK_CHANAGE = "extra_track_chanage";
        public static final String EXTRA_TEXT1 = "extra_text1";
        public static final String EXTRA_SELECTED_POI_NAME = "extra_selected_poi_name";
        public static final String EXTRA_USER_POINTS = "extra_user_points";
        public static final String EXTRA_ROUTE_NAME = "extra_route_name";
        public static final String EXTRA_JSON_DATA = "extra_json_data";
        public static final String EXTRA_VENUE_TYPE_NAME = "extra_venue_type_name";
        public static final String EXTRA_ENJOYS = "extra_enjoys";
        public static final String EXTRA_READ_COUNT = "extra_read_count";
        public static final String EXTRA_IS_START_LOCATION = "extra_is_start_location";
        public static final String EXTRA_PANORAMA_TYPE = "extra_panorama_type";
        public static final String EXTRA_CIRCUN_TYPE = "extra_circun_type";
        public static final String EXTRA_AT_VENUE_LIST = "extra_at_venue_list";
        public static final String EXTRA_BITMAP_BYTE_ARRAY = "extra_bitmap_byte_array";
        public static final String EXTRA_ONLY_PREVIEW = "extra_only_preview";
    }

    /**
     * 网络访问等用到的地址相关的字符串
     */
    class URL {
        public static final String ALI_BASE_URL = "http://47.95.215.6:8080/Api/";    // 阿里云(识花)
        //IP
//        public static final String IP = "39.105.120.171";
              public static final String IP = "36.112.128.48";
        //自己服务器基本请求用到的URL
//        public static final String BASE_URL = "http://" + IP + ":8080/Api/";
              public static final String BASE_URL = "http://" + IP + ":8089/Api/";
        //自己服务器文件资源请求用到的URL
//        public static final String FILE_BASE_URL = "http://" + IP + "/res/";
              public static final String FILE_BASE_URL = "http://36.112.128.167:8084/res/";
        public static final String PANORAMA_BASE_URL = "http://36.112.128.174:8089";
        public static final String SCENIC_SPOTS = "GetParksList";
        public static final String ACTUAL_SCENES = "GetVenuesList";
        //植物识别地址
        public static final String DISTINGUISH_PLANT = "http://plantgw.nongbangzhu.cn/plant/recognize";
        public static final String ENCYCLOPEDIAS_DETAIL_URL = "http://39.105.120.171/res/static/dist/index.html#/introduce";
        //        public static final String ENCYCLOPEDIAS_DETAIL_URL = "http://www.toolsmi.com/dist/index.html#/introduce";
//        public static final String ENCYCLOPEDIAS_DETAIL_URL = "http://192.168.1.143:8080/dist1/index.html#/introduce";
        public static final String HTML_404 = "file:///android_asset/web/404.html";
        public static final String UPPAY_APP_DOWNLOAD_URL = "http://47.92.247.143/res/static/UPPayPluginEx.apk";
        //        public static final String MAP_TILES_BASE_URL = FILE_BASE_URL + "titlemap/tiles/%d/%d_%d.png";
        public static final String MAP_TILES_BASE_URL = FILE_BASE_URL + "titlemap/tiles/%d/%d_%d.png";
        public static final String REGISTER_TICKET = "http://36.112.128.80:8890/ticketAppUser/appUserToOffWebUser";

        public static final String ASSETS_DEFAULT_TOUR_FILE_NAME = "defaulttour.zip";
        public static final String LOCAL_DEFAULT_TOUR_COPY_PATH = File.separator + ASSETS_DEFAULT_TOUR_FILE_NAME;
    }

    /**
     * 对app中内容配置
     */
    class Config {
        public static final String BASE_FILE_PATH = "expo/";                          //数据存储文件夹
        public static final String IMAGE_PATH = BASE_FILE_PATH + "images/";       //图片存储文件夹
        public static final String CROP_SAVE_PATH = IMAGE_PATH + "crop/";             //图片裁剪存储文件
        public static final String SCREEN_SAVE_PATH = IMAGE_PATH + "screen/";             //截图存储文件
        public static final int USERINFO_CROP_IMAGE_ASPECT_X = 1;                                //图片裁剪相关配置
        public static final int USERINFO_CROP_IMAGE_ASPECT_Y = 1;
        public static final int USERINFO_CROP_IMAGE_OUTPUT_X = 500;
        public static final int USERINFO_CROP_IMAGE_OUTPUT_Y = 500;

        //下载任务最大同时下载数量
        public static final int DOWNLOAD_NUMBER = 2;
        //根目录
        public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        //临时存储文件路径 如拍照裁剪图片等
        public static final String TEMP_PATH = BASE_FILE_PATH + "tmp/";
        //解压地址
        public static final String UNZIP_PATH = BASE_FILE_PATH + "unzip/";
        //实体类
        public static final Class[] DB_CLASSES = new Class[]{Venue.class, CommonInfo.class, DataType.class, DownloadInfo.class,
                Encyclopedias.class, Message.class, Subject.class, User.class, RouteInfo.class, TouristType.class, CustomRoute.class,
                TopLineInfo.class, VenuesType.class, Park.class, Badge.class, Track.class, FootPrint.class, VisitorService.class,
                Contacts.class, VrInfo.class, VrLableInfo.class, Tuple.class, ExpoActivityInfo.class, Schedule.class, PortalSite.class,
                ScheduleTimeInfo.class, ScheduleVenue.class, QAd.class, QAt.class};
        //下载任务最大同时下载数量
        public static final int IMAGE_MAX_COUNT = 3;
    }

    /**
     * 用于验证的一些正则表达式
     */
    class Exps {
        public static final String PHONE = "1\\d{10}";
        public static final String NUMBER = "\\d+";
        public static final String DOUBLE = "\\d+(\\.\\d+)?";
    }

    /**
     * 广播用到的Action
     */
    class Action {

        public static final String LOGIN_CHANGE_OF_STATE_ACTION = "login_change_of_state_action";
        public static final String ACTION_RECEIVE_MESSAGE = "action_receive_message";
        public static final String ACTION_CHANGE_LANGUAGE = "action_change_language";
        //        public static final String ACTION_CANCEL_UPDATE = "action_cancel_update";
        public static final String ACTION_TRACK_CHANAGE = "action_track_chanage";
        public static final String ACTION_LOCATION_CHANGED_IN_SCENES = "action_location_changed_in_scenes";
        public static final String ACTION_REDUCE_USER_POINTS = "action_reduce_user_points";
        public static final String ACTION_DOWNLOAD_APP_SUCCESS = "action_download_app_success";
        public static final String ACTION_DOWNLOAD_VR_APP_SUCCESS = "action_download_vr_app_success";
        public static final String ACTION_MAP_ON_OFF = "action_map_on_off";
    }

    /**
     * startActivityForResult 的RequestCode
     */
    class RequestCode {

        public static final int REQUEST111 = 11;
        public static final int REQ_SELECT_IMAGE = 120;
        public static final int REQ_CROP = 125;
        public static final int REQ_OPEN_GPS = 126;
        public static final int REQ_GET_LOCAL = 127;
        public static final int REQ_TO_CAMERA = 128;
        public static final int REQ_TO_CONTACTS = 129;
        public static final int REQ_TO_QRCODE = 130;
        public static final int REQ_TO_FIND_INFO = 131;
        public static final int REQ_INSTALL_PERMISS_CODE = 132;
        public static final int REQ_AR = 200;
    }

    /**
     *
     */
    class TimeFormat {
        public static final String TYPE_ALL = "yyyy-MM-dd HH:mm:ss";
        public static final String TYPE_SIMPLE = "yyyy-MM-dd";
        public static final String TYPE_YEAR = "yyyy";
    }

    /**
     * EventBus 的Id
     */
    class EventBusMessageId {
        public static final int EVENTBUS_ID_FRESH_USER = 1;//user反生变化
        public static final int EVENTBUS_ID_HEART_MESSAGE_UNREAD_COUNT = 2;//新的心跳消息
    }

    /**
     *
     */
    class NaviTip {
        //        public static final String TO_JS_NAVI_FILE_PATH = Config.IMAGE_PATH + "navi/";//文件包路径
        public static final String TO_JS_NAVI_TIP_TYPE = "1";//向js提示使用光团gif图片

        public static final String TO_JS_NAVI_TIP_START = "callstart/callstart";//出场问好（导航路线规划完成）
        public static final String TO_JS_NAVI_TIP_GPS_DIVERGE = "callGPSdiverge/callGPSdiverge";//偏离线路提示（自动重新规划时）
        public static final String TO_JS_NAVI_TIP_LONG_MUSIC_START = "callmusic/callmusic_start";//戴耳机听音乐-长时间等待（原地停留超过5分钟）
        public static final String TO_JS_NAVI_TIP_LONG_MUSIC_END = "callmusic/callmusic_end";//戴耳机听音乐-长时间等待（原地停留超过5分钟）
        public static final String TO_JS_NAVI_TIP_GPS_LOW = "callGPSlow/callGPSlow";//GPS信号弱或丢失（GPS信号弱）
        public static final String TO_JS_NAVI_TIP_GPS_LOST = "callGPSlost/callGPSlost";//GPS信号弱或丢失（GPS信号弱）
        public static final String TO_JS_NAVI_TIP_WAKE = "callwake/callwake";//姿势夸张睡觉
        public static final String TO_JS_NAVI_TIP_LEAVE_START = "callleave/callleave_start";//结束导航，抽泣、哭泣（要离开我吗）真的要离开我吗(人为点返回键）
        public static final String TO_JS_NAVI_TIP_LEAVE_END = "callleave/callleave_end";//回到导航
        public static final String TO_JS_NAVI_TIP_END = "callend/callend";//到达终点，庆祝
    }

    class RawResource {
        public static final int[] resourceIds = new int[]{0, R.raw.a10534, R.raw.b10629, R.raw.c10859, R.raw.d10875, R.raw.e10879, R.raw.f10880, R.raw.g10881};
        public static final String[] rawName = new String[]{"跟随系统", "a10534", "b10629", "c10859", "d10875", "e10879", "f10880", "g10881"};
    }

    class ContactsType {

        public static final Map<String, Integer> CONTACTS_TYPE_MAP = new HashMap<String, Integer>() {
            {
                put("1", R.string.card_type_shenfen);
                put("3", R.string.card_type_huixiang);
                put("4", R.string.card_type_taibao);
                put("2", R.string.card_type_huzhao);
            }
        };

        public static final Map<String, Integer> FIND_TYPE_MAP = new HashMap<String, Integer>() {
            {
                put("1", R.string.find_tab_scenic);
                put("2", R.string.find_tab_venue);
                put("3", R.string.find_tab_food);
                put("4", R.string.find_tab_botany);
                put("5", R.string.find_tab_other);
            }
        };

        public static final Map<String, Integer> WORK_TYPE_MAP = new HashMap<String, Integer>() {
            {
                put("1", R.string.work_tab_it);
                put("2", R.string.work_tab_doctor);
                put("3", R.string.work_tab_commerce);
                put("4", R.string.work_tab_finance);
                put("5", R.string.work_tab_sale);
            }
        };
    }

    class HandlerMsg {
        public static final int NAVI_NEAR_WIKI = 0;//附近景点

    }

    class ScoreType {
        public static final String TYPE_ENCYCLOPEDIAS = "1";//附近景点
        public static final String TYPE_SHARE = "2";//附近景点
    }


    class TimeType {
        //        public static final int MORNING = 11 * 3600 * 1000;//上午和中午的分割线
//        public static final int AFTERNOON = 14 * 3600 * 1000;//中午和下午的分割线
//        public static final int NIGHT = 19 * 3600 * 1000;//下午和晚上的分割线
        public static final int MORNING = 12 * 3600 * 1000;//上午和中午的分割线
        public static final int AFTERNOON = 19 * 3600 * 1000;//中午和下午的分割线
    }

    class VrType {
        public static final String VR_TYPE_VIDEO = "0";//视频
        public static final String VR_TYPE_IMG = "1";//图片
        public static final String VR_TYPE_VR = "2";//vr
    }

    class CHString {
        public static final String RESTAURANT = "\u9910\u5385";//餐厅
        public static final String FOODS = "\u7f8e\u98df";//美食
        public static final String HOTEL = "\u9152\u5e97";//酒店
        public static final String SCENIC_SPOT = "\u5c55\u56ed";//展园
        public static final String SCENIC_VENUE = "\u573a\u9986";//场馆
        public static final String PLANTS = "\u4e4b\u6700";//之最
        public static final String TOILET = "\u536b\u751f\u95f4";//卫生间
    }
}
