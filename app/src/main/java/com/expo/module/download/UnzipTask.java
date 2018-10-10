package com.expo.module.download;

import android.os.AsyncTask;
import android.os.Environment;

import com.expo.base.ExpoApp;
import com.expo.base.utils.FileUtils;
import com.expo.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                ZipFile zipFile = new ZipFile( file );
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    zipEntry = entries.nextElement();
                    target = new File( Environment.getExternalStorageDirectory(),
                            Constants.Config.UNZIP_PATH + ExpoApp.getApplication().getPackageName() + File.separator + zipEntry.getName() );
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
                            throw new RuntimeException( "can not create file on " + target.getPath() );
                        }
                        fos = new FileOutputStream( target );
                        is = zipFile.getInputStream( zipEntry );
                        FileUtils.copy( is, fos );
                        if (fos != null) {
                            fos.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
