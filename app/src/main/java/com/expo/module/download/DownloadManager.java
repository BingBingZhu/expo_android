package com.expo.module.download;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;

import com.expo.adapters.DownloadData;
import com.expo.adapters.DownloadInfoAdapter;
import com.expo.adapters.TouristTypeAdapter;
import com.expo.base.utils.FileUtils;
import com.expo.base.utils.LogUtils;
import com.expo.db.dao.BaseDao;
import com.expo.db.dao.BaseDaoImpl;
import com.expo.entity.DownloadInfo;
import com.expo.entity.TouristType;
import com.expo.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadManager {

    public static final int DOWNLOAD_IDLE = 0;
    public static final int DOWNLOAD_WAITING = 1;
    public static final int DOWNLOAD_STARTED = 2;
    public static final int DOWNLOAD_ERROR = 3;
    public static final int DOWNLOAD_STOPPED = 4;
    public static final int DOWNLOAD_FINISH = 5;

    private LinkedList<DownloadTask> mTaskQueue;
    private LinkedList<DownloadTask> mStartedTaskQueue;
    private List<DownloadListener> mListeners;
    private OkHttpClient mClient;
    private Handler handler = new Handler();
    private static DownloadManager mInstance;
    private BaseDao mDao;

    private List<TouristType> mTouristData;

    private DownloadManager() {
        mDao = new BaseDaoImpl();
    }

    public static DownloadManager getInstance() {
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }

    public void setData(List<TouristType> touristData){
        this.mTouristData  = touristData;
    }

    public void addTask(DownloadData info) {
        if (mTaskQueue == null) {
            mTaskQueue = new LinkedList<>();
        }
        if (!mTaskQueue.contains( info )) {
            mTaskQueue.add( new DownloadTask( info, mListener, this ) );
            handler.removeCallbacks( loopRunnable );
            handler.post( loopRunnable );
        }
    }

    private DownloadListener mListener = new DownloadListener() {
        @Override
        public void onProgressUpdate(DownloadData info) {
            if (info instanceof TouristTypeAdapter){
                for (TouristType type : mTouristData){
                    if (type.getId() == info.getId()){
//                        int index = mTouristData.indexOf(type);
//                        TouristType touristType = mTouristData.get(index);
//                        touristType.setDownState(info.getStatus());
//                        touristType.setLocalPath(info.getLocalPath());
                        type.setCurrPosition(info.getCurrPosition());
//                        mTouristData.set(index, touristType);
                        mDao.saveOrUpdate( type );
                    }
                }
            }else if (info instanceof DownloadInfoAdapter){

            }
//            mDao.saveOrUpdate( info );
            if (mListeners != null && !mListeners.isEmpty()) {
                for (int i = 0; i < mListeners.size(); i++) {
                    mListeners.get( i ).onProgressUpdate( info );
                }
            }
        }

        @Override
        public void onStatusChanged(DownloadData info) {
            if (info instanceof TouristTypeAdapter){
                for (TouristType type : mTouristData){
                    if (type.getId() == info.getId()){
//                        int index = mTouristData.indexOf(type);
//                        TouristType touristType = mTouristData.get(index);
                        type.setDownState(info.getStatus());
//                        touristType.setLocalPath(info.getLocalPath());
//                        touristType.setCurrPosition(info.getCurrPosition());
//                        mTouristData.set(index, touristType);
                        mDao.saveOrUpdate( type );
                    }
                }
            }else if (info instanceof DownloadInfoAdapter){

            }
//            mDao.saveOrUpdate( info );
            if (mListeners != null && !mListeners.isEmpty()) {
                for (int i = 0; i < mListeners.size(); i++) {
                    mListeners.get( i ).onStatusChanged( info );
                }
            }
        }
    };

    public void addDownloadUpdateListener(DownloadListener listener) {
        if (mListeners == null) {
            mListeners = new LinkedList<>();
        }
        mListeners.add( listener );
    }

    public void removeDownloadListener(DownloadListener listener) {
        if (mListeners != null && mListeners.contains( listener )) {
            mListeners.remove( listener );
        }
    }


    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            if (mStartedTaskQueue == null) {
                mStartedTaskQueue = new LinkedList<>();
            }
            if (!mTaskQueue.isEmpty() && mStartedTaskQueue.size() < Constants.Config.DOWNLOAD_NUMBER) {
                DownloadTask task = mTaskQueue.removeFirst();
                mStartedTaskQueue.addLast( task );
                task.execute();
            } else if (mTaskQueue.isEmpty()) {
                handler.removeCallbacks( this );
                return;
            }
            handler.postDelayed( this, 2000 );
        }
    };

    private void removeTask(DownloadTask task) {
        if (mStartedTaskQueue != null && mStartedTaskQueue.contains( task )) {
            mStartedTaskQueue.remove( task );
        } else if (mTaskQueue != null && mTaskQueue.contains( task )) {
            mTaskQueue.remove( task );
        }
    }

    public void removeTask(DownloadData info) {
        if (info.getStatus() == DOWNLOAD_WAITING && mTaskQueue == null) {
            return;
        } else if (info.getStatus() == DOWNLOAD_WAITING) {
            for (DownloadTask task : mTaskQueue) {
                if (task.info.getId() == info.getId()) {
                    task.stop();
                    mTaskQueue.remove( task );
                }
            }
        } else if (info.getStatus() == DOWNLOAD_STARTED && mStartedTaskQueue == null) {
            return;
        } else if (info.getStatus() == DOWNLOAD_STARTED) {
            for (DownloadTask task : mStartedTaskQueue) {
                if (task.info.getId() == info.getId()) {
                    task.stop();
                    mStartedTaskQueue.remove( task );
                }
            }
        }
    }


    public void removeAllTask() {
        if (mTaskQueue == null) {
            return;
        } else {
            for (DownloadTask task : mTaskQueue) {
                mTaskQueue.remove( task );
            }
            for (DownloadTask task : mStartedTaskQueue) {
                task.stop();
                mStartedTaskQueue.remove( task );
            }
        }
    }

    public OkHttpClient getClient() {
        if (mClient == null) {
            synchronized (DownloadManager.class) {
                if (mClient == null) {
                    mClient = new OkHttpClient.Builder()
                            .readTimeout( 10, TimeUnit.SECONDS )
                            .connectTimeout( 10, TimeUnit.SECONDS )
                            .build();
                }
            }
        }
        return mClient;
    }

    public static class DownloadTask extends AsyncTask<Void, Integer, Integer> {
        private static final String TAG = "DownloadTask";
        DownloadData info;
        DownloadListener listener;
        boolean stop;
        DownloadManager manager;
        Call call;

        public DownloadTask(DownloadData info, DownloadListener listener, DownloadManager manager) {
            this.info = info;
            this.listener = listener;
            this.manager = manager;
        }

        @Override
        protected void onPreExecute() {
            if (info != null) {
                info.setStatus( DOWNLOAD_WAITING );
                if (TextUtils.isEmpty( info.getLocalPath() )) {
                    String suffix = info.getResUrl().substring( info.getResUrl().lastIndexOf( "." ) );
                    String path = String.format( "%s/%s%s%s.tmp", Environment.getExternalStorageDirectory().getAbsolutePath(),
                            Constants.Config.TEMP_PATH, String.valueOf( System.currentTimeMillis() ), suffix );
                    info.setLocalPath( path );
                }
                notifyStatusChanged();
            }
        }

        @Override
        protected Integer doInBackground(Void... strings) {
            if (info == null || TextUtils.isEmpty( info.getResUrl() )) {
                return DOWNLOAD_ERROR;
            }
            InputStream is = null;
            Response response = null;
            int state = DOWNLOAD_FINISH;
            try {
                File file = FileUtils.createFile( info.getLocalPath() );
                RandomAccessFile saveFile = new RandomAccessFile( file, "rw" );
                Request request = new Request.Builder()
                        .get()
                        .url( Constants.URL.FILE_BASE_URL + info.getResUrl() )
                        .addHeader( "Range", "bytes=" + info.getCurrPosition() + "-" )
                        .build();
                call = manager.getClient().newCall( request );
                response = call.execute();
                if (response.code() == 200) {
                    info.setCurrPosition( 0l );
                } else if (response.code() == 206) {
                    String contentRange = response.header( "Content-Range" );
                    if (TextUtils.isEmpty( contentRange ))
                        return DOWNLOAD_ERROR;
                    String position = contentRange.split( " " )[1].split( "-" )[0];
                    if (position.matches( Constants.Exps.NUMBER )) {
                        info.setCurrPosition( Long.parseLong( position ) );
                    }
                } else {
                    return DOWNLOAD_ERROR;
                }
                info.setStatus( DOWNLOAD_STARTED );
                is = response.body().byteStream();
                int len;
                byte[] data = new byte[1024];
                boolean first = true;
                saveFile.seek( info.getCurrPosition() );
                while (!stop && (len = is.read( data )) != -1) {
                    if (first) {
                        first = false;
                        publishProgress( 0 );
                    }
                    info.setCurrPosition( info.getCurrPosition() + len );
                    saveFile.write( data, 0, len );
                    publishProgress( 1 );
                }
                String path = file.getAbsolutePath();
                path = path.substring( 0, path.lastIndexOf( "." ) );
                File zipFile = new File( path );
                file.renameTo( zipFile );
                new UnzipTask().execute( zipFile );
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.d( TAG, e );
                state = DOWNLOAD_ERROR;
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                if (response != null) {
                    response.close();
                }
            }
            return state;
        }

        @Override
        protected void onPostExecute(Integer status) {
            info.setStatus( status );
            manager.removeTask( this );
            notifyStatusChanged();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == 0) {
                notifyStatusChanged();
            } else {
                notifyProgressUpdate();
            }
        }

        private void notifyStatusChanged() {
            manager.handler.removeCallbacks( notifyStatus );
            manager.handler.post( notifyStatus );

        }

        private Runnable notifyStatus = new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onStatusChanged( info );
                }
            }
        };

        private Runnable notifyProgress = new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onProgressUpdate( info );
                }
            }
        };

        private void notifyProgressUpdate() {
            manager.handler.removeCallbacks( notifyProgress );
            manager.handler.post( notifyProgress );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DownloadTask that = (DownloadTask) o;

            return info != null ? info.equals( that.info ) : that.info == null;
        }

        @Override
        public int hashCode() {
            return info != null ? info.hashCode() : 0;
        }

        public void stop() {
            stop = true;
            call.cancel();
            info.setStatus( DOWNLOAD_STOPPED );
            if (info instanceof TouristTypeAdapter){
                for (TouristType type : manager.mTouristData){
                    if (type.getId() == info.getId()){
//                        int index = mTouristData.indexOf(type);
//                        TouristType touristType = mTouristData.get(index);
                        type.setDownState(info.getStatus());
//                        touristType.setLocalPath(info.getLocalPath());
//                        type.setCurrPosition(info.getCurrPosition());
//                        mTouristData.set(index, touristType);
                        manager.mDao.saveOrUpdate( type );
                    }
                }
            }else if (info instanceof DownloadInfoAdapter){

            }
//            manager.mDao.saveOrUpdate( info );
            manager.removeTask( this );
            notifyStatusChanged();
            this.cancel( true );
        }
    }

    public interface DownloadListener {
        void onProgressUpdate(DownloadData info);

        void onStatusChanged(DownloadData info);
    }




    public long download(Context context, String url, String title, String desc) {
        Uri uri = Uri.parse(url);
        LogUtils.e("aaaaaaaaaaaa   url", url);
        android.app.DownloadManager.Request req = new android.app.DownloadManager.Request(uri);
        //设置WIFI下进行更新
        req.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI);
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility( android.app.DownloadManager.Request.VISIBILITY_VISIBLE | android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, title);
        //7.0以上的系统适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            req.setRequiresDeviceIdle(false);
            req.setRequiresCharging(false);
        }
        //通知栏标题
        req.setTitle(title);
        //通知栏描述信息
        req.setDescription(desc);
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");
        //获取下载任务ID
        android.app.DownloadManager dm = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder localBuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(localBuilder.build());
        }
        return dm.enqueue(req);
    }

    /**
     * 下载前先移除前一个任务，防止重复下载
     * @param downloadId
     */
    public void clearCurrentTask(Context context, long downloadId) {
        android.app.DownloadManager dm = (android.app.DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            dm.remove(downloadId);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
}
