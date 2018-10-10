package com.expo.utils;

import com.expo.entity.ActualScene;
import com.expo.entity.CommonInfo;
import com.expo.entity.DataType;
import com.expo.entity.DownloadInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.ScenicSpot;
import com.expo.entity.Subject;
import com.expo.entity.User;

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
    }

    /**
     * 存放页面跳转中传递参数使用到的KEY
     */
    class EXTRAS {
        public static final String EXTRA_TITLE = "extra_title";
        public static final String EXTRA_URL = "extra_url";
        public static final String EXTRA_LOGIN_STATE = "extra_login_state";
    }

    /**
     * 网络访问等用到的地址相关的字符串
     */
    class URL {
        //IP
        public static final String IP = "39.107.112.17";
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
        //sdcard保存文件的根地址
        public static final String BASE_FILE_PATH = "lucas/";
        //临时存储文件路径 如拍照裁剪图片等
        public static final String TEMP_PATH = BASE_FILE_PATH + "tmp/";
        //解压地址
        public static final String UNZIP_PATH = BASE_FILE_PATH + "unzip/";
        //实体类
        public static final Class[] DB_CLASSES = new Class[]{ActualScene.class, CommonInfo.class, DataType.class, DownloadInfo.class,
                Encyclopedias.class, Message.class, ScenicSpot.class, Subject.class, User.class};
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
}
