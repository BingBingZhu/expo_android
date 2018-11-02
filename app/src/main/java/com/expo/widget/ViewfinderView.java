package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;

import com.expo.R;

/**
 * Created by LS on 2017/12/6.
 */

public class ViewfinderView extends View implements Runnable {

    private float mStrokeMarginRatio = 0.2f;
    private boolean mIsSquare = true;
    private int mStrokeWidth = 10;
    private int mStrokeLength = 20;
    private int mStrokeColor;
    private int mOutsideColor;
    private Drawable mScanLineDrawable;         // 扫描线
    private RectF mViewfinderBounds;
    private RectF mLeftCover;
    private RectF mTopCover;
    private RectF mRightCover;
    private RectF mBottomCover;
    private Paint mPaint;
    private Path mPath;
    private int mCurrentDistance;
    private int mPreDistance = 10;
    private long mDelay = 50;
    private Handler mHandler = new Handler();
    private Bitmap mDistinguishBitmap;
    private Bitmap mDistinguishBlurBitmap;
    private Rect mDistinguishBmpBounds;
    private Rect mBounds;

    public ViewfinderView(Context context) {
        this(context, null);
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewfinderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mViewfinderBounds = new RectF();
        mLeftCover = new RectF();
        mRightCover = new RectF();
        mTopCover = new RectF();
        mBottomCover = new RectF();
        mBounds = new Rect();
//        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mStrokeColor = getResources().getColor(R.color.white);
        mOutsideColor = getResources().getColor(R.color.black_translucent_50);
        if (attrs == null) return;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        mStrokeLength = a.getDimensionPixelSize(R.styleable.ViewfinderView_strokeLength, mStrokeLength);
        mStrokeMarginRatio = a.getFloat(R.styleable.ViewfinderView_strokeMarginRatio, mStrokeMarginRatio);
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.ViewfinderView_strokeWidth, mStrokeWidth);
        mIsSquare = a.getBoolean(R.styleable.ViewfinderView_isSquare, mIsSquare);
        mStrokeColor = a.getColor(R.styleable.ViewfinderView_strokeColor, mStrokeColor);
        mOutsideColor = a.getColor(R.styleable.ViewfinderView_outsideColor, mOutsideColor);
        mScanLineDrawable = a.getDrawable(R.styleable.ViewfinderView_scanLine);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float mStrokeMargin = mStrokeMarginRatio * w;
        mBounds.set(0, 0, w, h);
        if (mIsSquare) {
            float width = w - 2 * mStrokeMargin;
            mViewfinderBounds.set(mStrokeMargin, (h - width) * 0.39F, w - mStrokeMargin, (h + width) * 0.45F);
        } else {
            mViewfinderBounds.set(mStrokeMargin, mStrokeMargin, w - mStrokeMargin, h - mStrokeMargin);
        }
        mLeftCover.set(0, 0, mViewfinderBounds.left, h);
        mTopCover.set(mViewfinderBounds.left, 0, mViewfinderBounds.right, mViewfinderBounds.top);
        mRightCover.set(mViewfinderBounds.right, 0, w, h);
        mBottomCover.set(mViewfinderBounds.left, mViewfinderBounds.bottom, mViewfinderBounds.right, h);
//        //左上
//        mPath.moveTo(mViewfinderBounds.left, mViewfinderBounds.top + mStrokeLength);
//        mPath.lineTo(mViewfinderBounds.left, mViewfinderBounds.top);
//        mPath.lineTo(mViewfinderBounds.left + mStrokeLength, mViewfinderBounds.top);
//        //右上
//        mPath.moveTo(mViewfinderBounds.right - mStrokeLength, mViewfinderBounds.top);
//        mPath.lineTo(mViewfinderBounds.right, mViewfinderBounds.top);
//        mPath.lineTo(mViewfinderBounds.right, mViewfinderBounds.top + mStrokeLength);
//        //右下
//        mPath.moveTo(mViewfinderBounds.right, mViewfinderBounds.bottom - mStrokeLength);
//        mPath.lineTo(mViewfinderBounds.right, mViewfinderBounds.bottom);
//        mPath.lineTo(mViewfinderBounds.right - mStrokeLength, mViewfinderBounds.bottom);
//        //左下
//        mPath.moveTo(mViewfinderBounds.left + mStrokeLength, mViewfinderBounds.bottom);
//        mPath.lineTo(mViewfinderBounds.left, mViewfinderBounds.bottom);
//        mPath.lineTo(mViewfinderBounds.left, mViewfinderBounds.bottom - mStrokeLength);
        setScanLineBounds();
    }

    private void setScanLineBounds() {
        if (mScanLineDrawable != null) {
            mScanLineDrawable.setBounds((int) mViewfinderBounds.left,
                    (int) (mCurrentDistance + mViewfinderBounds.top - mScanLineDrawable.getIntrinsicHeight()),
                    (int) mViewfinderBounds.right,
                    (int) (mCurrentDistance + mViewfinderBounds.top));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDistinguishBitmap != null) {
            if (mDistinguishBlurBitmap != null) {
                canvas.drawBitmap(mDistinguishBlurBitmap, mDistinguishBmpBounds, mBounds, null);
            }
            canvas.drawBitmap(mDistinguishBitmap, mDistinguishBmpBounds, mViewfinderBounds, null);
        }
        mPaint.setColor(mOutsideColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mLeftCover, mPaint);
        canvas.drawRect(mRightCover, mPaint);
        canvas.drawRect(mTopCover, mPaint);
        canvas.drawRect(mBottomCover, mPaint);
        canvas.save();
        canvas.clipRect(mViewfinderBounds);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mStrokeColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(mViewfinderBounds.left, mViewfinderBounds.top, mViewfinderBounds.left, mViewfinderBounds.bottom, mPaint);
        canvas.drawLine(mViewfinderBounds.left, mViewfinderBounds.top, mViewfinderBounds.right, mViewfinderBounds.top, mPaint);
        canvas.drawLine(mViewfinderBounds.right, mViewfinderBounds.top, mViewfinderBounds.right, mViewfinderBounds.bottom, mPaint);
        canvas.drawLine(mViewfinderBounds.left, mViewfinderBounds.bottom, mViewfinderBounds.right, mViewfinderBounds.bottom, mPaint);
//        canvas.drawPath(mPath, mPaint);
        if (mScanLineDrawable != null) {
            mScanLineDrawable.draw(canvas);
        }
        canvas.restore();
    }

    private boolean generateBlurBitmap() {
        if (mDistinguishBitmap == null) {
            return false;
        }
        if (mDistinguishBlurBitmap == null) {
            this.mDistinguishBlurBitmap = Bitmap.createBitmap(mDistinguishBitmap.getWidth(), mDistinguishBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        }
        RenderScript rs = RenderScript.create(getContext());
        Allocation input = Allocation.createFromBitmap(rs, mDistinguishBitmap);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(25);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(mDistinguishBlurBitmap);
        input.destroy();
        output.destroy();
        scriptIntrinsicBlur.destroy();
        rs.destroy();
        return true;
    }

    @Override
    public void run() {
        if (mScanLineDrawable != null) {
            if (mCurrentDistance > mViewfinderBounds.width() + 2 * mScanLineDrawable.getIntrinsicHeight()) {
                mCurrentDistance = 0;
            } else {
                mCurrentDistance += mPreDistance;
            }
            setScanLineBounds();
            invalidate();
        }
        mHandler.postDelayed(this, mDelay);
    }

    public void stopScanAnimation() {
        mHandler.removeCallbacks(this);
        mCurrentDistance = 0;
        setScanLineBounds();
        invalidate();
    }

    public void startScanAnimation() {
        mDistinguishBitmap = null;
        mDistinguishBlurBitmap = null;
        mHandler.postDelayed(this, mDelay);
    }

    public Rect getViewfinderBounds(int width, int height) {
        int l = (int) (mViewfinderBounds.left / getWidth() * width);
        int t = (int) (mViewfinderBounds.top / getHeight() * height);
        int r = (int) (mViewfinderBounds.width() / getWidth() * width + l);
        int b = (int) (mViewfinderBounds.height() / getHeight() * height + t);
        return new Rect(l, t, r, b);
    }

    public void setDistinguishBitmap(Bitmap bmp) {
        this.mDistinguishBitmap = bmp;
        if (mDistinguishBlurBitmap == null) {
            generateBlurBitmap();
        } else {
            mDistinguishBlurBitmap = null;
        }
        if (this.mDistinguishBmpBounds == null) {
            this.mDistinguishBmpBounds = new Rect(0, 0, mDistinguishBitmap.getWidth(), mDistinguishBitmap.getHeight());
        } else {
            this.mDistinguishBmpBounds.set(0, 0, mDistinguishBitmap.getWidth(), mDistinguishBitmap.getHeight());
        }
        invalidate();
    }
}
