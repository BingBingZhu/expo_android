package com.expo.base.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
        File target = new File( path );
        return createFile( target );
    }

    public static File createFile(File target) {
        try {
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

    /**
     * 创建文件
     *
     * @param type
     * @param videoPath
     * @return
     */
    public static File createFile(String type, String videoPath) {
        String fileName = type;
        if ("video".equals( type )) {
            fileName += System.currentTimeMillis() + ".mp4";
        } else if ("image".equals( type )) {
            fileName += System.currentTimeMillis() + ".jpg";
        } else {
            fileName += System.currentTimeMillis();
        }

        try {
            File file = new File( Environment.getExternalStorageDirectory(), videoPath );
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File( file, fileName );
            if (!file.exists()) {
                if (file.createNewFile()) {
                    return file;
                }
            }
        } catch (IOException e) {
            LogUtils.e( TAG, "--create new file error--", e );
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

    public interface CompressCallback {
        void onCompleted(byte[] bytes);
    }

    /**
     * 图片压缩
     *
     * @param bitmap
     * @param maxSize
     * @param callback
     */
    public static void compressToFit(final Bitmap bitmap, final int maxSize, CompressCallback callback) {
        if (bitmap == null) return;
        new Thread() {
            @Override
            public void run() {
                ByteArrayOutputStream bos = null;
                int compressRatio = 80;
                int preCompress = 5;
                try {
                    bos = new ByteArrayOutputStream();
                    do {
                        bos.reset();
                        bitmap.compress( Bitmap.CompressFormat.JPEG, compressRatio, bos );
                        compressRatio -= preCompress;
                    } while (bos.size() > maxSize && compressRatio > 0);
                    if (callback != null) {
                        callback.onCompleted( bos.toByteArray() );
                    }
                } catch (Exception e) {
                    LogUtils.e( TAG, "compress bitmap error", e );
                } finally {
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }.start();
    }

    //保存照片
    public static void saveBitmap(String jpegName, Bitmap b) {
        try {
            FileOutputStream fout = new FileOutputStream( jpegName );
            BufferedOutputStream bos = new BufferedOutputStream( fout );
            b.compress( Bitmap.CompressFormat.JPEG, 100, bos );
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //保存照片
    public static void saveBitmap(File file, Bitmap b) {
        try {
            FileOutputStream fout = new FileOutputStream( file );
            BufferedOutputStream bos = new BufferedOutputStream( fout );
            b.compress( Bitmap.CompressFormat.JPEG, 100, bos );
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
