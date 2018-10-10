package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.expo.R;

public class CameraButton extends View {

    private int mColor;
    private int mRingColor;
    private int mThickness;
    private int mInterval;
    private int mTime;
    private Paint mPaint;
    private RectF mRingRect;
    private RectF mCore;
    private boolean isDown;
    private boolean hasEnd;
    private boolean longPressed;
    private int currentTime;
    private Handler mHandle = new Handler( Looper.getMainLooper() );
    private OnLongPressListener onLongPressListener;

    public CameraButton(Context context) {
        this( context, null );
    }

    public CameraButton(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public CameraButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init( attrs );
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes( attrs, R.styleable.CameraButton );
        mColor = a.getColor( R.styleable.CameraButton_android_color, Color.WHITE );
        mRingColor = a.getColor( R.styleable.CameraButton_ringColor, Color.WHITE );
        int thicknessDef = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics() );
        int interval = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics() );
        mThickness = a.getDimensionPixelSize( R.styleable.CameraButton_thickness, thicknessDef );
        mInterval = a.getDimensionPixelSize( R.styleable.CameraButton_interval, interval );
        mTime = a.getInteger( R.styleable.CameraButton_time, 60 ) * 1000;
        a.recycle();
        mPaint = new Paint();
        mRingRect = new RectF();
        mCore = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged( w, h, oldw, oldh );
        float a = mThickness / 2f;
        mRingRect.set( a, a, w - a, h - a );
        float b = mThickness + mInterval;
        mCore.set( b, b, w - b, h - b );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor( Color.TRANSPARENT, PorterDuff.Mode.CLEAR );
        mPaint.reset();
        mPaint.setAntiAlias( true );
        mPaint.setStrokeWidth( mThickness );
        mPaint.setColor( mColor );
        mPaint.setStyle( Paint.Style.STROKE );
        mPaint.setStrokeCap( Paint.Cap.ROUND );
        mPaint.setStrokeJoin( Paint.Join.ROUND );
        if (isDown && !hasEnd) {
            int angle = 360 * currentTime / mTime;
            canvas.drawArc( mRingRect, angle, 360, false, mPaint );
            mPaint.setColor( mRingColor );
            canvas.drawArc( mRingRect, -90, angle, false, mPaint );
        } else {
            canvas.drawOval( mRingRect, mPaint );
        }
        mPaint.setStyle( Paint.Style.FILL );
        canvas.drawOval( mCore, mPaint );
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isDown && currentTime < mTime) {
                if (currentTime == 0) {
                    longPressed = true;
                    if (onLongPressListener != null) {
                        onLongPressListener.onStart();
                    }
                }
                currentTime += 200;
                mHandle.postDelayed( this, 200 );
                invalidate();
            } else {
                timerEnd();
            }
        }
    };

    private void timerEnd() {
        mHandle.removeCallbacks( timerRunnable );
        if (longPressed && !hasEnd) {
            hasEnd = true;
            invalidate();
            if (onLongPressListener != null) {
                onLongPressListener.onStop();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentTime = 0;
                isDown = true;
                hasEnd = false;
                longPressed = false;
                mHandle.postDelayed( timerRunnable, 500 );
                break;
            case MotionEvent.ACTION_UP:
                isDown = false;
                timerEnd();
                if (!longPressed) {
                    callOnClick();
                }
                break;
        }
        return true;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.onLongPressListener = onLongPressListener;
    }

    public interface OnLongPressListener {
        void onStart();

        void onStop();
    }
}
