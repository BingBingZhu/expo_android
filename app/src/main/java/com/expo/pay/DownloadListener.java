package com.expo.pay;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.expo.utils.Constants;

public class DownloadListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            long id = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, -1 );
            if (id != -1) {
                DownloadManager manager = (DownloadManager) context.getSystemService( Context.DOWNLOAD_SERVICE );
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById( id );
                Cursor cursor = manager.query( query );
                if (cursor != null && cursor.moveToFirst()) {
                    String uri = cursor.getString( cursor.getColumnIndex( DownloadManager.COLUMN_URI ) );
                    if (uri.contains( Constants.URL.UPPAY_APP_DOWNLOAD_URL )) {
                        JsMethod.installForNetwork( context, true );
                    }
                }
            }
        } else if (intent.getAction() == DownloadManager.ACTION_NOTIFICATION_CLICKED) {
            Intent viewDownloadIntent = new Intent( DownloadManager.ACTION_VIEW_DOWNLOADS );
            viewDownloadIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            context.startActivity( viewDownloadIntent );
        }
    }
}
