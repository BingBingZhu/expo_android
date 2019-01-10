package com.expo.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.expo.R;

public class CustomDefaultDialog {

    private Dialog dialog;
    private String content;
    private String ok = "确认";
    private String cancel = "取消";
    private Context context;
    private boolean isRed;
    private boolean onlyOk;
    private boolean cancelable = true;

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

    /**
     * 设置提示内容
     * @param content
     * @return
     */
    public CustomDefaultDialog setContent(int content){
        this.content = context.getResources().getString(content);
        return this;
    }

    /**
     * 设置提示内容
     * @param content
     * @return
     */
    public CustomDefaultDialog setContent(String content){
        this.content = content;
        return this;
    }

    /**
     * 确认按钮文本
     * @param ok
     * @return
     */
    public CustomDefaultDialog setOkText(int ok){
        this.ok = context.getResources().getString(ok);
        return this;
    }

    /**
     * 确认按钮文本
     * @param ok
     * @return
     */
    public CustomDefaultDialog setOkText(String ok){
        this.ok = ok;
        return this;
    }

    /**
     * 设置确认按钮红色
     * @return
     */
    @SuppressLint("ResourceType")
    public CustomDefaultDialog setOkTextRed(){
        this.isRed = true;
        return this;
    }

    /**
     * 取消按钮文本
     * @param cancel
     * @return
     */
    public CustomDefaultDialog setCancelText(int cancel){
        this.cancel = context.getResources().getString(cancel);
        return this;
    }

    /**
     * 取消按钮文本
     * @param cancel
     * @return
     */
    public CustomDefaultDialog setCancelText(String cancel){
        this.cancel = cancel;
        return this;
    }

    /**
     * 确认点击事件
     * @param clickListener
     * @return
     */
    public CustomDefaultDialog setOnOKClickListener(View.OnClickListener clickListener){
        this.okClickListener = clickListener;
        return this;
    }

    /**
     * 取消点击事件
     * @param clickListener
     * @return
     */
    public CustomDefaultDialog setOnCancelClickListener(View.OnClickListener clickListener){
        this.cancelClickListener = clickListener;
        return this;
    }

    /**
     * 只有确认按钮
     * @return
     */
    public CustomDefaultDialog setOnlyOK(){
        this.onlyOk = true;
        return this;
    }

    /**
     * 点击外部是否消失
     * @param cancelable
     * @return
     */
    public CustomDefaultDialog setCancelable(boolean cancelable){
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 显示dialog
     */
    public void show(){
        dialog = new Dialog(context, R.style.TopActionSheetDialogStyle);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_layout_default, null);
        TextView tvContent = v.findViewById(R.id.default_dialog_content);
        TextView tvOK = v.findViewById(R.id.default_dialog_ok);
        TextView tvCancel = v.findViewById(R.id.default_dialog_cancel);
        View line = v.findViewById(R.id.default_dialog_line);
        if (onlyOk){
            tvCancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        if (isRed)
            tvOK.setTextColor(context.getResources().getColor(R.color.red_fe2121));
        tvContent.setText(content);
        tvOK.setText(ok);
        tvCancel.setText(cancel);
        tvOK.setOnClickListener(okClickListener);
        tvCancel.setOnClickListener( cancelClickListener );
        dialog.setContentView(v);
        dialog.setCancelable(cancelable);
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (((WindowManager)context.getSystemService( Context.WINDOW_SERVICE )).getDefaultDisplay().getWidth() * 0.8);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    public void dismiss(){
        if (null != dialog)
            dialog.dismiss();
    }

}
