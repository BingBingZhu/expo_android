package com.expo.base.utils;

import android.os.Environment;

import com.facebook.cache.disk.FileCache;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;

public class DataCleanUtil {

    /**
     * 获取缓存数据大小
     *
     * @return
     */
    public static String getCacheSize() {
        long      totalSize = 0;
        FileCache fileCache = Fresco.getImagePipelineFactory().getMainFileCache();
        fileCache.trimToMinimum();
        totalSize += fileCache.getSize();
        totalSize += getFileCache();
        return sizeToString(totalSize);
    }

    /**
     * 清除缓存图片
     */
    public static void deleteCacheFiles() {
        File file = new File(Environment.getExternalStorageDirectory(), "images/");
        if (!file.exists()) {
            return;
        }
        deleteAllChildFile(file);
    }

    /**
     * 删除所有文件
     *
     * @param file
     */
    private static void deleteAllChildFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteAllChildFile(f);
            }
        } else {
            file.delete();
        }
    }


    /**
     * 文件尺寸转化为字符串
     *
     * @param totleSize
     * @return
     */
    public static String sizeToString(long totleSize) {
        String size;
        if (totleSize > 1024 * 1024 * 1024) {
            size = String.format("%.2f GB", totleSize / 1024f / 1024 / 1024);
        } else if (totleSize > 1024 * 1024) {
            size = String.format("%.2f MB", totleSize / 1024f / 1024);
        } else if (totleSize > 1024) {
            size = String.format("%.2f KB", totleSize / 1024f);
        } else {
            size = String.format("%.2f B", (float) totleSize);
        }
        return size;
    }

    /**
     * 获取图片缓存大小
     *
     * @return
     */
    public static long getFileCache() {
        File file = new File(Environment.getExternalStorageDirectory(), "images/");
        if (!file.exists()) {
            return 0;
        }
        return getAllChildFileSize(file);
    }

    /**
     * 获取文件夹中所有文件总大小
     *
     * @param file
     * @return
     */
    private static long getAllChildFileSize(File file) {
        if (file.isDirectory()) {
            long   size  = 0;
            File[] files = file.listFiles();
            for (File f : files) {
                size += getAllChildFileSize(f);
            }
            return size;
        } else {
            return file.length();
        }
    }
}
