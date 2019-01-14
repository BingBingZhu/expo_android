package com.expo.base.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;

public class ViewUtils {

//    public static ViewUtils viewUtils;
//
//    public static ViewUtils getViewUtils(){
//        if (null == viewUtils){
//            viewUtils = new ViewUtils();
//        }
//        return viewUtils;
//    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight();
        final int screenWidth = ScreenUtils.getScreenWidth();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }


    public static void settingDialog(Context context, Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = (int) (display.getHeight() * 0.65);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            dialogWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        dialogWindow.setAttributes(lp);
    }
}
