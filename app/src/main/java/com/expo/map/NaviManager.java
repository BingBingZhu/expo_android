package com.expo.map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.expo.R;
import com.expo.base.utils.ToastHelper;
import com.expo.entity.Venue;

import java.io.File;

public class NaviManager implements View.OnClickListener/*, AMapNaviListener */{
    private static NaviManager naviManager;

    /**
     * 获取导航控制实例
     */
    public synchronized static NaviManager getInstance(Context context) {
        if (naviManager == null) {
            naviManager = new NaviManager();
        }
        mContext = context;
        return naviManager;
    }
    private static Context mContext;
//    private Venue venue;
    private Dialog dialog;
    private String name;
    private double lat;
    private double lng;

    public void showSelectorNavi(Venue venue){
//        this.venue = venue;
        this.name = venue.getCaption();
        this.lat = venue.getLat();
        this.lng = venue.getLng();
        showSelectorNavi();
    }

    public void showSelectorNavi(String name, double lat, double lng){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        showSelectorNavi();
    }


    /*跳转第三方导航app进行导航 start*/
    public void showSelectorNavi(){
        dialog = new Dialog(mContext, R.style.BottomActionSheetDialogStyle);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout_selector, null);
        Button btn1 = v.findViewById(R.id.dialog_select_1);
        Button btn2 = v.findViewById(R.id.dialog_select_2);
        Button btnCancel = v.findViewById(R.id.btn_cancel);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        dialog.setContentView(v);
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }
    private void startThredNavi(String packageName){
        boolean isExists = new File("/data/data/" + packageName).exists();
        if (isExists) {
            Intent naviIntent;
            if (packageName.equals("com.baidu.BaiduMap")){
                naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                        "baidumap://map/direction?origin=我的位置&destination=name:"+ name +
                                "|latlng:"+ lat +","+ lng /*+"|addr:北京市东城区东长安街"*/ +
                        "&coord_type=gcj02&mode=driving&src=com.casvd.lucaspark"));
//                naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
//                        "baidumap://map/direction?region="+ venue.getCity()+"&origin=我的位置&destination=name:"+ venue.getCaption()+
//                                "|latlng:"+ venue.getLat()+","+ venue.getLng()/*+"|addr:北京市东城区东长安街"*/ +
//                        "&coord_type=gcj02&mode=driving&src=com.casvd.lucaspark"));
            }else{
                naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(
                        "androidamap://route?sourceApplication=guangtuan&sname=我的位置&" +
                        "dlat="+ lat +"&dlon="+ lng +"&dname="+ name +"&dev=0&t=2"));
            }
            mContext.startActivity(naviIntent);
        }else {
            ToastHelper.showLong("未安装该应用");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_select_1:
                startThredNavi("com.baidu.BaiduMap");
                dialog.dismiss();
                break;
            case R.id.dialog_select_2:
                startThredNavi("com.autonavi.minimap");
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }
    /*跳转第三方导航app进行导航 end*/
}
