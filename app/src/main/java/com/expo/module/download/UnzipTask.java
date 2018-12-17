package com.expo.module.download;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import com.expo.base.ExpoApp;
import com.expo.base.utils.FileUtils;
import com.expo.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by LS on 2017/12/21.
 * Zip压缩文件解压任务
 */

public class UnzipTask extends AsyncTask<File, Void, Void> {

    @Override
    protected Void doInBackground(File... files) {
        InputStream is;
        ZipEntry zipEntry;
        File target;
        FileOutputStream fos;
        for (File file : files) {
            try {
                ZipFile zipFile;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    zipFile = new ZipFile(file, Charset.forName("GBK"));
                } else {
                    zipFile = new ZipFile(file);
                }
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                File f = new File(Environment.getExternalStorageDirectory(),
                        Constants.Config.UNZIP_PATH + ExpoApp.getApplication().getPackageName() + File.separator + file.getName().substring(0, file.getName().indexOf(".")));
                if (f.isDirectory() && !f.exists()) {
                    f.mkdirs();
                }
                while (entries.hasMoreElements()) {
                    try {
                        zipEntry = entries.nextElement();
                        String filePath = Constants.Config.UNZIP_PATH + ExpoApp.getApplication().getPackageName()
                                + File.separator + file.getName().substring(0, file.getName().indexOf(".")) + File.separator + zipEntry.getName();
                        filePath = new String(filePath.getBytes("utf-8"), "GB2312");
                        target = new File(Environment.getExternalStorageDirectory(),
                                filePath);
                        if (zipEntry.isDirectory() && !target.exists()) {
                            target.mkdirs();
                        } else {
                            if (target.exists()) {
                                target.delete();
                            }
                            if (!target.getParentFile().exists()) {
                                target.getParentFile().mkdirs();
                            }
                            if (!target.createNewFile()) {
//                            throw new RuntimeException( "can not create file on " + target.getPath() );
                            }
                            fos = new FileOutputStream(target);
                            is = zipFile.getInputStream(zipEntry);
                            FileUtils.copy(is, fos);
                            if (fos != null) {
                                fos.close();
                            }
                            if (is != null) {
                                is.close();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
