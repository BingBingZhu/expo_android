package com.expo.network;

import com.expo.base.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LogInterceptor implements Interceptor {
    private static final String TAG = "LogInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        StringBuilder sb = new StringBuilder();
        Request request = chain.request();
        sb.append( "------------------------http request data------------------------\nurl:" );
        sb.append( request.url().toString() );
        Buffer buffer = new Buffer();
        request.body().writeTo( buffer );
        sb.append( "\nrequest body:" );
        sb.append( buffer.readString( Charset.defaultCharset() ) );
        Response response = chain.proceed( request );
        sb.append( "\nresponse code:" );
        sb.append( response.code() );
        sb.append( "\nresponse content:" );
        buffer.clear();
        response.body().source().readAll( buffer );
        String content = buffer.readString( Charset.defaultCharset() );
        sb.append( content );
        LogUtils.d( TAG, sb.toString() );
        response = response.newBuilder()
                .body( ResponseBody.create( MediaType.parse( "application/json" ), content ) )
                .build();
        return response;
    }
}
