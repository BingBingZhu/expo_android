package com.expo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ThumbnailUtils;

public class BitmapUtils {

    public static Bitmap circleBitmap(Bitmap source, int width, int height) {
        //以Bitmap的宽度值作为新的bitmap的宽高值。
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //以此bitmap为基准，创建一个画布
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(Bitmap.createScaledBitmap(source, width, height, true), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
        paint.setAntiAlias(true);
        canvas.drawOval(new RectF(0, 0, width, height), paint);
        return bitmap;
    }

    /**
     * 将彩色图转换为纯黑白二色
     *
     * @param bmp
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp, int color) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                if (grey == Color.TRANSPARENT)
                    continue;
                if (0 != color) {       // 是否渲染
                    grey = color;
                    pixels[width * i + j] = grey;
                    continue;
                }

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);
        return resizeBmp;
    }

}
