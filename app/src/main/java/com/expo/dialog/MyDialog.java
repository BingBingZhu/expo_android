//package com.expo.dialog;
//
//import android.content.Context;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//
//public class MyDialog {
//
//
//    private static MyDialog instance;
//    private final static Object Lock = new Object();
//    private AlertDialog.Builder dialog;
//
//    public static MyDialog getInstance() {
//        if (instance == null) {
//            synchronized (Lock) {
//                if (instance == null) {
//                    instance = new MyDialog();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public void initDialog(Context context){
//        if (dialog == null) {
//            synchronized (Lock) {
//                if (dialog == null) {
//                    dialog = new AlertDialog.Builder(context).create();
//                }
//            }
//        }
//    }
//
//    public void setView(Context context, int layoutId){
//
//    }
//    public void setView(View view){
//        if(dialog == null)
//    }
//
//    public void disMiss() {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//    }
//
//}
