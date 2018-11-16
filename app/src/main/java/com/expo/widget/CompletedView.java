package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.expo.R;

/**
 * 自定义圆形进度条
 */
public class CompletedView extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画实心小圆点的画笔
    private Paint mCirclePointPaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画圆环的画笔背景色
    private Paint mRingPaintBg;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆形颜色
    private int mCirclePointColor;
    // 圆环颜色
    private int mRingColor;
    // 圆环背景颜色
    private int mRingBgColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
//    // 字的长度
//    private float mTxtWidth;
//    // 字的高度
//    private float mTxtHeight;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;
    // 中心图标
    private Drawable mCenterDrawable;

    public CompletedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
    }

    //属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TasksCompletedView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, getResources().getDimensionPixelSize(R.dimen.dms_34));
        mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, getResources().getDimensionPixelSize(R.dimen.dms_6));
        mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, getContext().getResources().getColor(R.color.transparent));
        mCirclePointColor = getContext().getResources().getColor(R.color.white);
        mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_cringColor, getContext().getResources().getColor(R.color.green_00cb99));
        mRingBgColor = typeArray.getColor(R.styleable.TasksCompletedView_cringBgColor, getContext().getResources().getColor(R.color.gray_cc));
        mCenterDrawable = typeArray.getDrawable(R.styleable.TasksCompletedView_centerPic);
        mRingRadius = mRadius + mStrokeWidth / 2;
    }

    //初始化画笔
    private void initVariable() {
        // 内圆
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        // 小白点
        mCirclePointPaint = new Paint();
        mCirclePointPaint.setAntiAlias(true);
        mCirclePointPaint.setColor(mCirclePointColor);
        mCirclePointPaint.setStyle(Paint.Style.FILL);

        //外圆弧背景
        mRingPaintBg = new Paint();
        mRingPaintBg.setAntiAlias(true);
        mRingPaintBg.setColor(mRingBgColor);
        mRingPaintBg.setStyle(Paint.Style.STROKE);
        mRingPaintBg.setStrokeWidth(mStrokeWidth);


        //外圆弧
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        //mRingPaint.setStrokeCap(Paint.Cap.ROUND);//设置线冒样式，有圆 有方

//        //中间字
//        mTextPaint = new Paint();
//        mTextPaint.setAntiAlias(true);
//        mTextPaint.setStyle(Paint.Style.FILL);
//        mTextPaint.setColor(mRingColor);
//        mTextPaint.setTextSize(mRadius / 2);

//        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
//        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
    }

    //画图
    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        //内圆
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        //外圆弧背景
        RectF oval1 = new RectF();
        oval1.left = (mXCenter - mRingRadius);
        oval1.top = (mYCenter - mRingRadius);
        oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        canvas.drawArc(oval1, 0, 360, false, mRingPaintBg); //圆弧所在的椭圆对象、圆弧的起始角度、圆弧的角度、是否显示半径连线

        mCenterDrawable.setBounds((int) (oval1.left+mRingRadius/3), (int) (oval1.top+mRingRadius/3),
                (int) (oval1.right-mRingRadius/3), (int) (oval1.bottom-mRingRadius/3));
        mCenterDrawable.draw(canvas);


        //外圆弧
        if (mProgress > 0 ) {
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, false, mRingPaint); //

//            //字体
//            String txt = mProgress + "分";
//            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
//            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
        }

        canvas.drawCircle(getWidth() / 2, oval1.top, getResources().getDimensionPixelSize(R.dimen.dms_2), mCirclePointPaint);
    }

    //设置进度
    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();//重绘
    }

    public void setCenterPic(int resId){
        mCenterDrawable = getContext().getResources().getDrawable(resId);
        postInvalidate();//重绘
    }
}