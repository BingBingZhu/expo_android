package com.expo.db;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Arrays;

public class DBUtil {
    private static final String TAG = "DBUtil";
    private static final String DB_NAME = "db_name";
    private static final String DB_VERSION = "db_version";

    private static String mDBName;
    private static int mDBVersion;

    public static String getDBName(Context context) {
        if (mDBName == null) {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager()
                        .getApplicationInfo( context.getPackageName(), PackageManager.GET_META_DATA );
                mDBName = applicationInfo.metaData.getString( DB_NAME, "application.db" );
            } catch (PackageManager.NameNotFoundException e) {
                mDBName = "application.db";
            }
        }
        return mDBName;
    }

    public static int getDBVersion(Context context) {
        if (mDBVersion == 0) {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager()
                        .getApplicationInfo( context.getPackageName(), PackageManager.GET_META_DATA );
                mDBVersion = applicationInfo.metaData.getInt( DB_VERSION, 1 );
            } catch (PackageManager.NameNotFoundException e) {
                mDBVersion = 1;
            }
        }
        return mDBVersion;
    }

    public static void init(Context context, Class[] entities) {
        new Thread( () -> {
            try {
                new DBOpenHelper( context, Arrays.asList( entities ) ).getWritableDatabase().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } ).start();
    }

    public static <T, ID> Dao<T, ID> getDao(Context context, Class<T> clazz) {
        try {
            DBOpenHelper helper = OpenHelperManager.getHelper( context, DBOpenHelper.class );
            return helper.getDao( clazz );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
