package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.utils.StatusBarUtils;

public class AppBarView extends FrameLayout {

    private ImageView mBackView;
    private TextView mTitleView;
    private View mRightView;
    private OnClickListener mClickListener;
    private boolean mShowBackButton;
    private Drawable mBackImage;
    private String mTitle;
    private float mTitleSize;
    private int mTitleSizeUnit;
    private int mTitleColor;
    private int mBackgroundColor;

    public AppBarView(Context context) {
        this( context, null );
    }

    public AppBarView(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public AppBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init( attrs );
    }

    private void init(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes( attrs, R.styleable.AppBarView );
        mBackImage = a.getDrawable( R.styleable.AppBarView_backSrc );
        if (mBackImage == null) {
            mBackImage = getContext().getResources().getDrawable( R.mipmap.icon_navbar_back_black );
        }
        mTitleSize = getResources().getDimensionPixelSize( R.dimen.font_36 );
        mTitleSizeUnit = TypedValue.COMPLEX_UNIT_PX;
        mTitle = a.getString( R.styleable.AppBarView_title );
        mTitleColor = a.getColor( R.styleable.AppBarView_titleColor, getContext().getResources().getColor( R.color.caption_color ) );
        mBackgroundColor = a.getColor( R.styleable.AppBarView_backgroundColor, getContext().getResources().getColor( R.color.green_00cb99 ) );
        mShowBackButton = a.getBoolean( R.styleable.AppBarView_showBack, true );
        a.recycle();

        mBackView = new ImageView( getContext() );
        LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        addView( mBackView, lp );

        mTitleView = new TextView( getContext() );
        lp = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        lp.gravity = Gravity.CENTER;
        mTitleView.setMaxLines( 1 );
        mTitleView.setSingleLine();
        mTitleView.setEllipsize( TextUtils.TruncateAt.MARQUEE );
        mTitleView.setFocusableInTouchMode( true );
        mTitleView.setMarqueeRepeatLimit( -1 );
        mTitleView.setMaxWidth( (int) getContext().getResources().getDimension( R.dimen.dms_390 ) );
        addView( mTitleView, lp );
        setPadding( 0, StatusBarUtils.getStatusBarHeight( getContext() ), 0, 0 );
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (mShowBackButton) {
            mBackView.setVisibility( VISIBLE );
        } else {
            mBackView.setVisibility( GONE );
        }
        if (!TextUtils.isEmpty( mTitle )) {
            mTitleView.setText( mTitle );
        }
        setBackgroundColor( mBackgroundColor );
        mTitleView.setTextSize( mTitleSizeUnit, mTitleSize );
        mTitleView.setTextColor( mTitleColor );
        mBackView.setImageDrawable( mBackImage );
        mBackView.setId( R.id.title_back );
        mTitleView.setId( R.id.appbar_title );
        int margin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 11, getContext().getResources().getDisplayMetrics() );
        mBackView.setPadding( margin, margin, margin, margin );
        if (mClickListener != null) {
            mBackView.setOnClickListener( (v) -> mClickListener.onClick( v ) );
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged( w, h, oldw, oldh );
        mTitleView.setMaxWidth( w - mBackView.getMeasuredWidth() * 2 );
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.mClickListener = l;
    }

    public void shouldShowBackButtom(boolean show) {
        mShowBackButton = show;
    }


    public void setTitle(int resId) {
        mTitle = getContext().getResources().getString( resId );
        if (mTitleView != null) {
            mTitleView.setText( mTitle );
        }
    }

    public void setTitle(String title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText( mTitle );
        }
    }

    public void setTitleColor(int titleColor) {
        this.mTitleColor = titleColor;
        if (mTitleView != null) {
            mTitleView.setTextColor( titleColor );
        }
    }

    public void setTitleSize(int unit, float size) {
        this.mTitleSize = size;
        this.mTitleSizeUnit = unit;
        if (mTitleView != null) {
            this.mTitleView.setTextSize( mTitleSizeUnit, mTitleSize );
        }
    }

    public void setBackImageResource(int resId) {
        this.mBackImage = getContext().getResources().getDrawable( resId );
        if (this.mBackView != null) {
            this.mBackView.setImageDrawable( mBackImage );
        }
    }

    public void setmBackgroundColor(int colorId){
        this.mBackgroundColor = colorId;
    }

    public void setRightView(View view) {
        mRightView = view;
        LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT );
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        view.setPadding( (int) getContext().getResources().getDimension( R.dimen.dms_30 ), 0, (int) getContext().getResources().getDimension( R.dimen.dms_30 ), 0 );
        addView( mRightView, lp );
    }

    public View getRightView() {
        return mRightView;
    }
}
