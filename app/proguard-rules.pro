#-keep class com.expo.** { *; }      # 测试第三方库混淆  暂不混淆自有代码
#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class com.expo.entity.**{ *; }
-keep class com.expo.network.response.**{ *; }

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#极光推送混淆 start
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#3D 地图
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.loc.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
# 搜索
-keep class com.amap.api.services.**{*;}
# 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
# 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

# Mob Share SDK
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R{*;}
-keep class m.framework.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn **.R$*

## 保持所有baidu包下的文件不被混淆
# 百度统计
# 语音识别
# 图像识别
## Baidu_Mtj_android_3.9.3.1.jar ##
## BaiduLBS_Android.jar ##
-keep class com.baidu.** { *; }

# 图像识别
#-libraryjars log4j-1.2.17.jar
-dontwarn org.apache.log4j.**
-keep class  org.apache.log4j.** { *; }

-keep class com.google.i18n.phonenumbers.** { *; }  ## libphonenumber-8.9.9.jar ##
-keep class com.tencent.** { *; }  ## tbs_sdk_thirdapp.jar ##
-keep class com.unionpay.** { *; }  ## qmf-ppplugin-android-3.0.3.aar ##
-keep class com.chinaums.** { *; }  ## UPPayAssistEx.jar ##
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** { *; }  ## UPPayPluginExPro.jar ##

# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
#-keepclassmembers class * {
#    @com.j256.ormlite.field.DatabaseField *;
#}

# 保持第三方包fastjson不被混淆
-keep class com.alibaba.fastjson.** {*;}

# 不混淆okhttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** {*;}
-dontwarn okio.**


#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

-keepclassmembers class com.expo.module.webview.*$JsHook {
   public *;
}

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------

-keep interface com.expo.contract.** { *; }

#----------------------------------------------------------------------------

#---------------------------------5.其他-----------------------
# 不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
# 防止构造被移除或者被重命名
-keepclassmembers class com.expo.**{
    public <init>(android.content.Context);
    public <init>(android.os.Parcel);
    public <init>();
}
# EventBus
-keepattributes Annotation
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5                                             #指定代码压缩级别
-dontusemixedcaseclassnames                                 #混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses                            #指定不忽略非公共类库
-dontpreverify                                              #不预校验，如果需要预校验，是-dontoptimize
-ignorewarnings                                             #屏蔽警告
-verbose                                                    #混淆时记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService




#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.view.View
-keep class android.support.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------