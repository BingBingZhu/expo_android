package com.expo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.expo.base.utils.FileUtils;
import com.expo.module.download.UnzipTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AssetCopyer {

    private Context mContext;

    public AssetCopyer(Context context) {
        mContext = context;
    }

    /**
     * 将文件从assets目录，考贝到 /data/data/包名/files/ 目录中。
     */
    public void copyAssetsFile2Phone(){
        try {
            InputStream inputStream = mContext.getAssets().open(Constants.URL.ASSETS_DEFAULT_TOUR_FILE_NAME);
            //getFilesDir() 获得当前APP的安装路径 /data/data/包名/files 目录
            File file = new File(mContext.getFilesDir().getAbsolutePath() + Constants.URL.LOCAL_DEFAULT_TOUR_COPY_PATH);
            if(!file.exists() || file.length()==0) {
                FileOutputStream fos =new FileOutputStream(file);   // 如果文件不存在，FileOutputStream会自动创建文件
                int len=-1;
                byte[] buffer = new byte[1024];
                while ((len=inputStream.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                }
                fos.flush();//刷新缓存区
                inputStream.close();
                fos.close();
//                ToastHelper.showLong("模型文件复制完毕");
                new UnzipTask().execute( file );    // 解压默认导游包
            } /*else {
                ToastHelper.showLong("模型文件已存在，无需复制");
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
