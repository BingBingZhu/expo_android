package com.expo.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.expo.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

public class CustomDefaultDialog {

    private Dialog dialog;
    private String content;
    private String ok = "确认";
    private String cancel = "取消";
    private Context context;
    private boolean isRed;


    private View.OnClickListener okClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };
    private View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    public CustomDefaultDialog(Context context){
        this.context = context;
    }

    public CustomDefaultDialog setContent(int content){
        this.content = context.getResources().getString(content);
        return this;
    }

    public CustomDefaultDialog setContent(String content){
        this.content = content;
        return this;
    }

    public CustomDefaultDialog setOkText(int ok){
        this.ok = context.getResources().getString(ok);
        return this;
    }

    public CustomDefaultDialog setOkText(String ok){
        this.ok = ok;
        return this;
    }

    @SuppressLint("ResourceType")
    public CustomDefaultDialog setOkTextRed(){
        this.isRed = true;
        return this;
    }

    public CustomDefaultDialog setCancelText(int cancel){
        this.cancel = context.getResources().getString(cancel);
        return this;
    }

    public CustomDefaultDialog setCancelText(String cancel){
        this.cancel = cancel;
        return this;
    }

    public CustomDefaultDialog setOnOKClickListener(View.OnClickListener clickListener){
        this.okClickListener = clickListener;
        return this;
    }

    public CustomDefaultDialog setOnCancelClickListener(View.OnClickListener clickListener){
        this.cancelClickListener = clickListener;
        return this;
    }

    public void show(){
        dialog = new Dialog(context, R.style.TopActionSheetDialogStyle);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_layout_default, null);
        TextView tvContent = v.findViewById(R.id.default_dialog_content);
        TextView tvOK = v.findViewById(R.id.default_dialog_ok);
        TextView tvCancel = v.findViewById(R.id.default_dialog_cancel);
        if (isRed)
            tvOK.setTextColor(context.getResources().getColor(R.color.red_fe2121));
        tvContent.setText(content);
        tvOK.setOnClickListener(okClickListener);
        tvCancel.setOnClickListener( cancelClickListener );
        dialog.setContentView(v);
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    public void dismiss(){
        if (null != dialog)
            dialog.dismiss();
    }

}
