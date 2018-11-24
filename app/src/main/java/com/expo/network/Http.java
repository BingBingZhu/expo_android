package com.expo.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.expo.BuildConfig;
import com.expo.R;
import com.expo.base.BaseApplication;
import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.entity.User;
import com.expo.utils.Constants;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Http {

    private static OkHttpClient mClient;
    private static Retrofit mRetrofit;
    private static DataServer mServer;
    private static Gson mGson;


    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout( 10, TimeUnit.SECONDS )
                .connectTimeout( 10, TimeUnit.SECONDS );
        if (ExpoApp.getApplication().isDebug()) {
            builder.addInterceptor( new LogInterceptor() );
        }
        mClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .client( mClient )
                .baseUrl( Constants.URL.BASE_URL )
                .addCallAdapterFactory( RxJava2CallAdapterFactory.createAsync() )
                .addConverterFactory( GsonConverterFactory.create( getGsonInstance() ) )
                .build();
    }

    /**
     * 清理网络缓存数据
     */
    public static void cleanCache() {
        try {
            if (mClient != null) {
                mClient.cache().initialize();
            }
        } catch (IOException e) {
        }
    }

    /**
     * 获取网络缓存的数据的尺寸
     *
     * @return
     */
    public static long getCacheSize() {
        try {
            if (mClient != null) {
                return mClient.cache().size();
            }
        } catch (IOException e) {
        }
        return 0;
    }

    public static Gson getGsonInstance() {
        if (mGson == null) {
            synchronized (Http.class) {
                if (mGson == null) {
                    mGson = new GsonBuilder()
                            .excludeFieldsWithModifiers( Modifier.STATIC, Modifier.FINAL, Modifier.TRANSIENT )
                            .setDateFormat( "yyyy-MM-dd HH:mm:ss" )
                            .create();
                }
            }
        }
        return mGson;
    }

    public static DataServer getServer() {
        if (mServer == null) {
            synchronized (Http.class) {
                if (mServer == null) {
                    mServer = mRetrofit.create( DataServer.class );
                }
            }
        }
        return mServer;
    }

    public static <T> boolean request(Observer<T> observer, Observable<T>... observables) {
        if (!isConnected() && Thread.currentThread() == Looper.getMainLooper().getThread()) {
            ToastHelper.showShort( R.string.no_network_connection );
            return false;
        }
        if (observables.length > 1) {
            Observable.merge( Arrays.asList( observables ) )
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe( observer );
        } else if (observables.length == 1) {
            observables[0]
                    .observeOn( AndroidSchedulers.mainThread() )
                    .subscribe( observer );
        }
        return true;
    }

    public static Map<String, Object> getBaseParams() {
        Map<String, Object> params = new HashMap<>();
        params.put( "type", "1" );
        params.put( "Ver", BuildConfig.VERSION_NAME );
        User user = ExpoApp.getApplication().getUser();
        if (user != null) {
            params.put( "Uid", user.getUid() );
            params.put( "Ukey", user.getUkey() );
        }
        return params;
    }

    public static RequestBody buildRequestBody(Map<String, Object> params) {
        return RequestBody.create( MediaType.parse( "application/json" ), getGsonInstance().toJson( params ) );
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getApplication().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 加载网络图片为Bitmap
     *
     * @param url
     * @param listener
     * @param callerContext
     */
    public static void loadBitmap(String url, Http.OnLoadImageCompleteListener listener, Object callerContext) {
        ImageRequest ir = ImageRequest.fromUri( url );
        DataSource<CloseableReference<CloseableImage>> dataSource
                = Fresco.getImagePipeline().fetchDecodedImage( ir, callerContext );
        if (Fresco.getImagePipeline().isInBitmapMemoryCache( ir )) {                                  //有缓存时直接从缓存中加载
            Bitmap bmp = ((CloseableBitmap) dataSource.getResult().get()).getUnderlyingBitmap();
            if (listener != null) {
                listener.onComplete( url, bmp, callerContext );
            }
            CloseableReference<CloseableImage> closeableReference = dataSource.getResult();
            if (closeableReference != null) {
                CloseableReference.closeSafely( closeableReference );
            }
            dataSource.close();
        } else {                                                                                    //无缓存时从网络加载
            DataSubscriber<CloseableReference<CloseableImage>> dataSubscriber
                    = new BaseBitmapDataSubscriber() {
                @Override
                protected void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (listener != null) {
                        listener.onComplete( url, bitmap, callerContext );
                    }
                    dataSource.close();
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    if (listener != null) {
                        listener.onComplete( url, null, callerContext );
                    }
                    LogUtils.d( "--load user photo failure--", dataSource.getFailureCause() );
                    CloseableReference<CloseableImage> closeableReference = dataSource.getResult();
                    if (closeableReference != null) {
                        CloseableReference.closeSafely( closeableReference );
                    }
                }
            };
            dataSource.subscribe( dataSubscriber, UiThreadImmediateExecutorService.getInstance() );
        }
    }

    /**
     * 加载图完成监听
     */
    public interface OnLoadImageCompleteListener {
        void onComplete(String url, Bitmap bitmap, Object obj);
    }
}
