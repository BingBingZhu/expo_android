package com.expo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.expo.R;
import com.expo.base.utils.ToastHelper;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/*
 * 分享工具类，提供分享面板的显示和分享操作
 */
public class ShareUtil {

    private PlatformActionListener mPlatformActionListener;
    private HashMap<String, Object> mShareParams;


    public void setPlatformActionListener(PlatformActionListener listener) {
        this.mPlatformActionListener = listener;
    }

    public ShareUtil() {
        mShareParams = new HashMap<>();
    }

    final boolean formatShareData(Platform plat) {
        String name = plat.getName();

        boolean isWechat = "Wechat".equals(name) || "WechatMoments".equals(name);
        if (isWechat && !plat.isClientValid()) {
            ToastHelper.showShort(R.string.wechat_is_not_installed);
            return false;
        }

        if ("QZone".equals(name) && !plat.isClientValid()) {
            ToastHelper.showShort(R.string.qq_space_is_not_installed);
            return false;
        }
        if (!mShareParams.containsKey("shareType")) {
            int shareType = Platform.SHARE_TEXT;
            String filePath = String.valueOf(mShareParams.get("filePath"));
            if (filePath != null && (new File(filePath)).exists()) {
                if (QZone.NAME.equals(name) || SinaWeibo.NAME.equals(name)) {
                    shareType = Platform.SHARE_VIDEO;
                } else if (Wechat.NAME.equals(name)) {
                    shareType = Platform.SHARE_FILE;
                }
            } else {
                String imagePath = String.valueOf(mShareParams.get("imagePath"));
                if (imagePath != null && (new File(imagePath)).exists()) {
                    shareType = Platform.SHARE_IMAGE;
                    if (imagePath.endsWith(".gif") && isWechat) {
                        shareType = Platform.SHARE_EMOJI;
                    } else if (mShareParams.containsKey("url") && !TextUtils.isEmpty((String) mShareParams.get("url"))) {
                        shareType = Platform.SHARE_WEBPAGE;
                        if (mShareParams.containsKey("musicUrl") && !TextUtils.isEmpty((String) mShareParams.get("musicUrl")) && isWechat) {
                            shareType = Platform.SHARE_MUSIC;
                        }
                    }
                } else {
                    Object imageUrl = mShareParams.get("imageUrl");
                    if (imageUrl != null && !TextUtils.isEmpty(String.valueOf(imageUrl))) {
                        shareType = Platform.SHARE_IMAGE;
                        if (String.valueOf(imageUrl).endsWith(".gif") && isWechat) {
                            shareType = Platform.SHARE_EMOJI;
                        } else if (mShareParams.containsKey("url") && !TextUtils.isEmpty((String) mShareParams.get("url"))) {
                            shareType = Platform.SHARE_WEBPAGE;
                            if (mShareParams.containsKey("musicUrl") && !TextUtils.isEmpty((String) mShareParams.get("musicUrl")) && isWechat) {
                                shareType = Platform.SHARE_MUSIC;
                            }
                        }
                    } else {
                        Bitmap imageData = (Bitmap) mShareParams.get("imageData");
                        if (imageData != null) {
                            shareType = Platform.SHARE_IMAGE;
                            if (String.valueOf(imageUrl).endsWith(".gif") && isWechat) {
                                shareType = Platform.SHARE_EMOJI;
                            } else if (mShareParams.containsKey("url") && !TextUtils.isEmpty((String) mShareParams.get("url"))) {
                                shareType = Platform.SHARE_WEBPAGE;
                                if (mShareParams.containsKey("musicUrl") && !TextUtils.isEmpty((String) mShareParams.get("musicUrl")) && isWechat) {
                                    shareType = Platform.SHARE_MUSIC;
                                }
                            }
                        }
                    }
                }
            }
            mShareParams.put("shareType", shareType);
        }
        return true;
    }

    public void share() {
        String name = String.valueOf(mShareParams.get("platform"));
        Platform platform = ShareSDK.getPlatform(name);
        if (mPlatformActionListener != null) {
            platform.setPlatformActionListener(mPlatformActionListener);
        }
        if (formatShareData(platform)) {
            platform.share(new Platform.ShareParams(mShareParams));
        }
    }


    public void setPlatformName(String platform) {
        mShareParams.put("platform", platform);
    }

    /**
     * title标题，在印象笔记、邮箱、信息、微信（包括好友、朋友圈和收藏）、
     * 易信（包括好友、朋友圈）、人人网和QQ空间使用，否则可以不提供
     */
    public void setTitle(String title) {
        mShareParams.put("title", title);
    }

    /**
     * titleUrl是标题的网络链接，仅在人人网和QQ空间使用，否则可以不提供
     */
    public void setTitleUrl(String titleUrl) {
        mShareParams.put("titleUrl", titleUrl);
    }

    /**
     * text是分享文本，所有平台都需要这个字段
     */
    public void setText(String text) {
        mShareParams.put("text", text);
    }

    /**
     * imagePath是本地的图片路径，除Linked-In外的所有平台都支持这个字段
     */
    public void setImagePath(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            mShareParams.put("imagePath", imagePath);
        }
    }

    /**
     * imageUrl是图片的网络路径，新浪微博、人人网、QQ空间和Linked-In支持此字段
     */
    public void setImageUrl(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            mShareParams.put("imageUrl", imageUrl);
        }
    }

    /**
     * imageData是bitmap图片，微信、易信支持此字段
     */
    public void setImageData(Bitmap iamgeData) {
        if (iamgeData != null) {
            mShareParams.put("imageData", iamgeData);
        }
    }

    /**
     * url在微信（包括好友、朋友圈收藏）和易信（包括好友和朋友圈）中使用，否则可以不提供
     */
    public void setUrl(String url) {
        mShareParams.put("url", url);
    }

    /**
     * filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
     */
    public void setFilePath(String filePath) {
        mShareParams.put("filePath", filePath);
    }

    /**
     * comment是我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
     */
    public void setComment(String comment) {
        mShareParams.put("comment", comment);
    }

    /**
     * site是分享此内容的网站名称，仅在QQ空间使用，否则可以不提供
     */
    public void setSite(String site) {
        mShareParams.put("site", site);
    }

    /**
     * siteUrl是分享此内容的网站地址，仅在QQ空间使用，否则可以不提供
     */
    public void setSiteUrl(String siteUrl) {
        mShareParams.put("siteUrl", siteUrl);
    }

    /**
     * 分享地纬度，新浪微博、腾讯微博和foursquare支持此字段
     */
    public void setLatitude(float latitude) {
        mShareParams.put("latitude", latitude);
    }

    /**
     * 分享地经度，新浪微博、腾讯微博和foursquare支持此字段
     */
    public void setLongitude(float longitude) {
        mShareParams.put("longitude", longitude);
    }

    /**
     * 设置微信分享的音乐的地址
     */
    public void setMusicUrl(String musicUrl) {
        mShareParams.put("musicUrl", musicUrl);
    }

    /**
     * 设置一个总开关，用于在分享前若需要授权，则禁用sso功能
     */
    public void disableSSOWhenAuthorize() {
        mShareParams.put("disableSSO", true);
    }

    /**
     * 设置视频网络地址
     */
    public void setVideoUrl(String url) {
        mShareParams.put("url", url);
        mShareParams.put("shareType", Platform.SHARE_VIDEO);
    }

    /**
     * 腾讯微博分享多张图片
     */
    public void setImageArray(String[] imageArray) {
        mShareParams.put("imageArray", imageArray);
    }

    /**
     * 设置在执行分享到QQ或QZone的同时，分享相同的内容腾讯微博
     */
    public void setShareToTencentWeiboWhenPerformingQQOrQZoneSharing() {
        mShareParams.put("isShareTencentWeibo", true);
    }

    public static void showShare(Context context, String title, String text, String imageUrl, String shareUrl, PlatformActionListener listener) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTitle(title);
        oks.setText(text);
        oks.setImageUrl(imageUrl);
        oks.setUrl(shareUrl);
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setText(text + shareUrl);
                }
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                if ("QQ".equals(platform.getName())) {
                    paramsToShare.setTitleUrl(shareUrl);
                }
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitleUrl(shareUrl);
                }
            }
        });
        if (listener != null)
            oks.setCallback(listener);
        oks.show(context);
    }
}
