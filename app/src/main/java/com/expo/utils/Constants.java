package com.expo.utils;

import android.os.Environment;

import com.expo.entity.ActualScene;
import com.expo.entity.CommonInfo;
import com.expo.entity.DataType;
import com.expo.entity.DownloadInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.RouteInfo;
import com.expo.entity.Subject;
import com.expo.entity.User;
import com.expo.entity.VenuesInfo;
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
        public static final String KEY_GUIDE_SHOWN = "key_guide_shown";
        public static final String KEY_LANGUAGE_CHOOSE = "key_language_choose";
        public static final String KEY_SHOW_SELECT_LANGUAGE = "key_app_first_use";
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
        //IP
        public static final String IP = "39.105.120.171";
        //自己服务器基本请求用到的URL
        public static final String BASE_URL = "http://" + IP + ":8080/Api/";
        //自己服务器文件资源请求用到的URL
        public static final String FILE_BASE_URL = "http://" + IP + "/res/";
        public static final String SCENIC_SPOTS = "GetParksList";
        public static final String ACTUAL_SCENES = "GetVenuesList";
    }

    /**
     * 对app中内容配置
     */
    class Config {
        //下载任务最大同时下载数量
        public static final int DOWNLOAD_NUMBER = 2;
        //根目录
        public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        //sdcard保存文件的根地址
        public static final String BASE_FILE_PATH = "lucas/";
        //临时存储文件路径 如拍照裁剪图片等
        public static final String TEMP_PATH = BASE_FILE_PATH + "tmp/";
        //解压地址
        public static final String UNZIP_PATH = BASE_FILE_PATH + "unzip/";
        //实体类
        public static final Class[] DB_CLASSES = new Class[]{ActualScene.class, CommonInfo.class, DataType.class, DownloadInfo.class,
                Encyclopedias.class, Message.class, Subject.class, User.class, RouteInfo.class, VenuesInfo.class};
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
