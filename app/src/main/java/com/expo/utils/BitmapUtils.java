package com.expo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.expo.R;

public class BitmapUtils {

    public static Bitmap circleBitmap(Bitmap source, int width, int height) {
        //以Bitmap的宽度值作为新的bitmap的宽高值。
        Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        //以此bitmap为基准，创建一个画布
        Canvas canvas = new Canvas( bitmap );
        Paint paint = new Paint();
        paint.setShader( new BitmapShader( Bitmap.createScaledBitmap( source, width, height, true ), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR ) );
        paint.setAntiAlias( true );
        canvas.drawOval( new RectF( 0, 0, width, height ), paint );
        return bitmap;
    }

}
