package com.expo.upapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.utils.ToastHelper;
import com.expo.entity.AppInfo;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UpdateAppManager {

    private static UpdateAppManager mInstance;

    public static UpdateAppManager getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            synchronized (UpdateAppManager.class) {
                if (mInstance == null) {
                    mInstance = new UpdateAppManager();
                }
            }
        }
        return mInstance;
    }

    public static UpdateAppManager getInstance(){
        if (mInstance == null) {
            synchronized (UpdateAppManager.class) {
                if (mInstance == null) {
                    mInstance = new UpdateAppManager();
                }
            }
        }
        return mInstance;
    }

    public AppInfo isHaveUpdate(String appVersionName, List<AppInfo> infos){
        AppInfo appInfo = null;
        for (AppInfo info : infos){
            if (info.platformname.equals("android")){
                if (!info.ver.equals(appVersionName)){
                    appInfo = info;
                }
            }
        }
        return appInfo;
    }

    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/";
    // 下载应用存放全路径
    public static String FILE_NAME = FILE_PATH + "expo_2019.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //Log日志打印标签
    private static final String TAG = "Update_log";
    private static Context mContext;
    // 下载应用的进度条
    private ProgressBar pb;
    private TextView tvRate;
    private TextView tvFileSize;
    private TextView tvDownload;
    private TextView tvCancel;
    private long downloadId;
    DownloadManagerUtil downloadManagerUtil;


    /**
     * 显示提示更新对话框
     */
    public void showNoticeDialog(AppInfo versionInfo, boolean isInform) {
        downloadManagerUtil = new DownloadManagerUtil(mContext);
        this.versionInfo = versionInfo;
        Dialog dialog = new Dialog(mContext, R.style.ApkDownloadDialogStyle);
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_update_version, null);
        pb = v.findViewById(R.id.ver_update_pb);
        TextView tvCaption = v.findViewById(R.id.ver_update_caption);
        TextView tvRemark = v.findViewById(R.id.ver_update_remark);
        tvRate = v.findViewById(R.id.ver_update_rate);
        tvFileSize = v.findViewById(R.id.ver_update_apk_size);
        tvDownload = v.findViewById(R.id.ver_update_download);
        tvCancel = v.findViewById(R.id.ver_update_cancel);
        tvCaption.setText(AppUtils.getAppVersionName());
        tvRemark.setText(LanguageUtil.chooseTest(versionInfo.remark, versionInfo.remarken));
        tvRate.setText("0%");
        tvRate.setVisibility(View.GONE);
        tvFileSize.setText(getDoubleFomat((Double.valueOf(versionInfo.apkfilesize)) / 1024 / 1024) + "M");
        tvDownload.setOnClickListener(v1 -> {
//            new DownloadAsyncTask().execute();
            //调起系统下载功能
            if (downloadId != 0) {
                downloadManagerUtil.clearCurrentTask(downloadId);
            }
            downloadId = downloadManagerUtil.download(Constants.URL.FILE_BASE_URL+versionInfo.apkurl,
                    AppUtils.getAppName(), getDoubleFomat((Double.valueOf(versionInfo.apkfilesize)) / 1024 / 1024) + "M");
            if (StringUtils.equals("1", versionInfo.isforce)) {

            }else{
                dialog.dismiss();
                ToastHelper.showShort("已添加至下载列表");
            }
        });
        if (StringUtils.equals("1", versionInfo.isforce)) {
            dialog.setCancelable(false);
            tvCancel.setVisibility(View.GONE);
        }
        tvCancel.setOnClickListener(v12 -> {
            dialog.dismiss();
            if (isInform) {
                LocalBroadcastUtil.sendBroadcast(mContext, null, Constants.Action.ACTION_CANCEL_UPDATE);
            }
        });
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private double getDoubleFomat(double number/*,int scale*/) {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private AppInfo versionInfo;

    /**
     * 下载新版本应用
     */
    private class DownloadAsyncTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            Log.e(TAG, "执行至--onPreExecute");
            FILE_NAME = FILE_PATH + versionInfo.getResUrl();
            tvRate.setVisibility(View.VISIBLE);
            tvDownload.setEnabled(false);
            tvCancel.setEnabled(false);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(Constants.URL.FILE_BASE_URL + versionInfo.apkurl);
                connection = (HttpURLConnection) url.openConnection();
                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }
                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                int readLength = 0;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);  //从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;
                    int curProgress = (int) (((float) readLength / fileLength) * 100);
                    Log.e(TAG, "当前下载进度：" + curProgress);
                    publishProgress(curProgress, readLength);
                    if (readLength >= fileLength) {
                        break;
                    }
                }
                out.flush();
                return INSTALL_TOKEN;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e(TAG, "异步更新进度接收到的值：" + values[0]);
            pb.setProgress(values[0]);
            tvRate.setText(values[0] + "%");
            tvFileSize.setText(getDoubleFomat(((double) values[1]) / 1024 / 1024) + "M" +
                    "/" + getDoubleFomat((Double.valueOf(versionInfo.resfilesize)) / 1024 / 1024) + "M");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (StringUtils.equals("1", versionInfo.isforce)) {
                tvDownload.setEnabled(true);
                tvCancel.setEnabled(true);
            }
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    public void installApp() {
        File apkFile = new File(FILE_NAME);
        if (apkFile.exists()) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile( mContext, mContext.getPackageName() + ".fileprovider", apkFile );
            } else {
                uri = Uri.fromFile( apkFile );
            }
            Intent in = new Intent( Intent.ACTION_VIEW );
            in.setDataAndType( uri, "application/vnd.android.package-archive" );
            in.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            in.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
            mContext.startActivity( in );
        }
    }
}
