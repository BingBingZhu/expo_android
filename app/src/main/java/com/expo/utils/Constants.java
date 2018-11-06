package com.expo.utils;

import com.expo.entity.ActualScene;
import com.expo.entity.CommonInfo;
import com.expo.entity.DataType;
import com.expo.entity.DownloadInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.Subject;
import com.expo.entity.TouristType;
import com.expo.entity.User;
import com.squareup.picasso.RequestCreator;

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
        public static final String KEY_GUIDE_SHOWN = "key_guide_shown";
        public static final String KEY_LANGUAGE_CHOOSE = "key_language_choose";
        public static final String KEY_SHOW_SELECT_LANGUAGE = "key_app_first_use";
        public static final String KEY_HISTORY = "key_history";
    }

    /**
     * 存放页面跳转中传递参数使用到的KEY
     */
    class EXTRAS {
        public static final String EXTRA_TITLE = "extra_title";
        public static final String EXTRA_URL = "extra_url";
        public static final String EXTRA_LOGIN_STATE = "extra_login_state";
        public static final String EXTRA_ID = "extra_id";
        public static final String EXTRA_TAB_ID = "extra_tab_id";
        public static final String EXTRA_SPOT_ID = "extra_spot_id";
        public static final String EXTRAS = "extras";
        public static final String EXTRA_LONGITUDE = "extra_longitude";
        public static final String EXTRA_LATITUDE = "extra_latitude";
    }

    /**
     * 网络访问等用到的地址相关的字符串
     */
    class URL {
        public static final String ALI_BASE_URL = "http://47.95.215.6:8080/Api/";    //阿里云(识花)
        //IP
        public static final String IP = "39.105.120.171";
        //自己服务器基本请求用到的URL
        public static final String BASE_URL = "http://" + IP + ":8080/Api/";
        //自己服务器文件资源请求用到的URL
        public static final String FILE_BASE_URL = "http://" + IP + "/res/";
        public static final String SCENIC_SPOTS = "GetParksList";
        public static final String ACTUAL_SCENES = "GetVenuesList";
        //植物识别地址
        public static final String DISTINGUISH_PLANT = "http://plantgw.nongbangzhu.cn/plant/recognize";
    }

    /**
     * 对app中内容配置
     */
    class Config {

        public static final String BASE_FILE_PATH = "expo/";                          //数据存储文件夹
        public static final String IMAGE_PATH = BASE_FILE_PATH + "images/";       //图片存储文件夹
        public static final String CROP_SAVE_PATH = IMAGE_PATH + "crop/";             //图片裁剪存储文件
        public static final int USERINFO_CROP_IMAGE_ASPECT_X = 1;                                //图片裁剪相关配置
        public static final int USERINFO_CROP_IMAGE_ASPECT_Y = 1;
        public static final int USERINFO_CROP_IMAGE_OUTPUT_X = 500;
        public static final int USERINFO_CROP_IMAGE_OUTPUT_Y = 500;

        //下载任务最大同时下载数量
        public static final int DOWNLOAD_NUMBER = 2;
        //临时存储文件路径 如拍照裁剪图片等
        public static final String TEMP_PATH = BASE_FILE_PATH + "tmp/";
        //解压地址
        public static final String UNZIP_PATH = BASE_FILE_PATH + "unzip/";
        //实体类
        public static final Class[] DB_CLASSES = new Class[]{ActualScene.class, CommonInfo.class, DataType.class, DownloadInfo.class,
                Encyclopedias.class, Message.class, Subject.class, User.class, TouristType.class};
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
    }

    /**
     * startActivityForResult 的RequestCode
     */
    class RequestCode {

        public static final int REQUEST111 = 11;
        public static final int REQ_SELECT_IMAGE = 120;
        public static final int REQ_CROP = 125;
    }

    /**
     *
     */
    class TimeFormat {
        public static final String TYPE_ALL = "yyyy-MM-dd hh:mm";
        public static final String TYPE_SIMPLE = "yyyy-MM-dd";
        public static final String TYPE_YEAR = "yyyy";
    }

    /**
     * EventBus 的Id
     */
    class EventBusMessageId {
        public static final int EVENTBUS_ID_FRESH_USER = 1;//user反生变化
    }

}
