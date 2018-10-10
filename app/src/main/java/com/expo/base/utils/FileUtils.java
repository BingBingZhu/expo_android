package com.expo.base.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.annotations.NonNull;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static File createFile(String path) {
        if (TextUtils.isEmpty( path ))
            throw new IllegalArgumentException( "The path of file can not be empty." );
        try {
            File target = new File( path );
            if (!target.exists()) {
                if (!target.getParentFile().exists()) {
                    target.getParentFile().mkdirs();
                }
                if (target.createNewFile()) {
                    return target;
                }
            } else {
                return target;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(File file, String text) {
        if (file == null)
            throw new IllegalArgumentException( "The file can not be null" );
        if (TextUtils.isEmpty( text ))
            return;
        FileWriter writer = null;
        try {
            writer = new FileWriter( file );
            writer.write( text );
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
        }
    }

    public static void copy(@NonNull InputStream in, @NonNull OutputStream out) {
        try {
            int len;
            byte[] data = new byte[1024];
            while ((len = in.read( data )) != -1) {
                out.write( data, 0, len );
            }
            out.flush();
        } catch (IOException e) {
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取指定文件或文件夹的大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isFile()) {
            return file.length();
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            long size = 0;
            for (File childFile : childFiles) {
                size += getFileSize( childFile );
            }
            return size;
        }
        return 0;
    }

    /**
     * 删除指定文件或文件夹
     *
     * @param file
     */
    public static void deleteFiles(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (File childFile : childFiles) {
                deleteFiles( childFile );
            }
            file.delete();
        }
    }
}
