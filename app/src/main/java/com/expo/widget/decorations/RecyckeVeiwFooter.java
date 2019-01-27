package com.expo.widget.decorations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

public class RecyckeVeiwFooter extends RecyclerView.ItemDecoration {
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 首先定义一个paint
        Paint paint = new Paint();
        // 绘制矩形区域-实心矩形
        // 设置颜色
        paint.setColor(Color.BLUE);
        // 设置样式-填充
        paint.setStyle(Paint.Style.FILL);
        // 绘制一个矩形
        c.drawRect(new Rect(0, 0, parent.getWidth(), parent.getHeight()), paint);
//        // 绘空心矩形
//        // 设置颜色
//        paint.setColor(Color.RED);
//        // 设置样式-空心矩形
//        paint.setStyle(Paint.Style.FILL);
//        // 绘制一个矩形
//        c.drawRect(new Rect(10, 10, 50, 20), paint);
        // 绘文字
        // 设置颜色
        paint.setColor(Color.GREEN);
        // 绘文字
        c.drawText("到底啦！", 30, 30, paint);
    }
}
