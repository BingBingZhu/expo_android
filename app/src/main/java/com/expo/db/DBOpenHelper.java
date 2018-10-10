package com.expo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {
    private List<Class> mEntries;

    public DBOpenHelper(Context context) {
        super( context, DBUtil.getDBName( context ), null, DBUtil.getDBVersion( context ) );
    }

    public DBOpenHelper(Context context, List<Class> entries) {
        super( context, DBUtil.getDBName( context ), null, DBUtil.getDBVersion( context ) );
        this.mEntries = entries;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            if (mEntries != null && !mEntries.isEmpty()) {
                for (Class clz : mEntries) {
                    TableUtils.createTableIfNotExists( connectionSource, clz );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            if (mEntries != null && !mEntries.isEmpty()) {
                for (Class clz : mEntries) {
                    TableUtils.dropTable( connectionSource, clz, true );
                }
                onCreate( database, connectionSource );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
