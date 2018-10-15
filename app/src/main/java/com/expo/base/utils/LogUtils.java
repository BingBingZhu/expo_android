package com.expo.base.utils;

import android.os.Environment;
import android.os.Process;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志输出等级工具类
 */
public class LogUtils {

    /**
     * DEBUG :是用来控制，是否打印日志，默认true
     */
    public static boolean DEBUG = true;

    /**
     * 是否将日志写入到文件中,默认false
     */
    public static boolean WRITE_LOG_TO_FILE = false;

    /**
     * 日志文件的路径
     */
    public static String logPath = "log/";

    /*
     * 将日志写入文件
     * @param tag   日志标签
     * @param message   日志内容
     * @param t    异常
     */
    private static void writeLog(String tag, String message, Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append( DateFormat.format( "yy-MM-dd HH:mm:ss", System.currentTimeMillis() ) );
        sb.append( " " );
        sb.append( String.valueOf( Process.myPid() ) );
        sb.append( "/" );
        sb.append( tag );
        sb.append( ":" );
        if (message != null) {
            sb.append( message );
            sb.append( "\n" );
        }
        if (t != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter( sw );
            t.printStackTrace( pw );
            pw.flush();
            sb.append( sw.toString() );
            try {
                sw.close();
            } catch (IOException e) {
            }
            pw.close();
        }
        String fileName = DateFormat.format( "yyMMdd", System.currentTimeMillis() ) + ".log";
        if (!logPath.endsWith( "/" ))
            logPath += "/";
        File logFile = FileUtils.createFile( Environment.getExternalStorageDirectory().getPath() + File.separator + logPath + fileName );
        FileUtils.write( logFile, sb.toString() );
    }

    public static void e(String tag, String message) {
        e( tag, message, null );
    }

    public static void e(String tag, Throwable t) {
        e( tag, "", t );
    }

    public static void e(String tag, String message, Throwable t) {
        if (DEBUG) {
            Log.e( tag, message, t );
        }
        if (WRITE_LOG_TO_FILE) {
            try {
                writeLog( tag, message, t );
            } catch (Exception e) {
            }
        }
    }

    public static void d(String tag, String message) {
        d( tag, message, null );
    }

    public static void d(String tag, Throwable t) {
        d( tag, "", t );
    }

    public static void d(String tag, String message, Throwable t) {
        if (DEBUG) {
            Log.d( tag, message, t );
        }
        if (WRITE_LOG_TO_FILE) {
            writeLog( tag, message, t );
        }
    }
}
