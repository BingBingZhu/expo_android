package com.expo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.module.main.MainActivity;
import com.expo.module.share.ShareUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareUtils {

    public static void showShare(Context context, String title, String text, String imageUrl, String shareUrl, PlatformActionListener listener) {
        ShareUtil mShareUtil = new ShareUtil((Activity) context);
        mShareUtil.setPlatformActionListener(listener);
        Dialog dialog = new Dialog(context, R.style.BottomActionSheetDialogStyle);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_layout_share, null);
        ImageView imgWechat = v.findViewById(R.id.share_wechat);
        ImageView imgQQ = v.findViewById(R.id.share_qq);
        ImageView imgSina = v.findViewById(R.id.share_sina);
        ImageView imgWechatMoments = v.findViewById(R.id.share_wechat_moments);
        ImageView imgQzone = v.findViewById(R.id.share_qzone);
        TextView tvCancel = v.findViewById(R.id.share_cancel);
        imgWechat.setOnClickListener(v1 -> { mShareUtil.doShare( Wechat.NAME, title, text, imageUrl, shareUrl ); dialog.dismiss(); });
        imgQQ.setOnClickListener(v1 -> { mShareUtil.doShare( QQ.NAME, title, text, imageUrl, shareUrl ); dialog.dismiss(); });
        imgSina.setOnClickListener(v1 -> { mShareUtil.doShare( SinaWeibo.NAME, title, text, imageUrl, shareUrl ); dialog.dismiss(); });
        imgQzone.setOnClickListener(v1 -> { mShareUtil.doShare( WechatMoments.NAME, title, text, imageUrl, shareUrl ); dialog.dismiss(); });
        imgWechatMoments.setOnClickListener(v1 -> { mShareUtil.doShare( QZone.NAME, title, text, imageUrl, shareUrl ); dialog.dismiss(); });
        tvCancel.setOnClickListener(v1 -> dialog.dismiss() );
        dialog.setContentView(v);
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }
}