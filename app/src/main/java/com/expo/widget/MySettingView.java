package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.expo.R;

public class MySettingView extends FrameLayout {

    TextView mLeftText;
    TextView mRightText;

    int mLeftStrId, mRightStrId;

    public MySettingView(Context context) {
        this(context, null);
    }

    public MySettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if (mLeftText == null) initLeftText();
        if (mRightText == null) initRightText();
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MySettingView);
            mLeftStrId = a.getResourceId(R.styleable.MySettingView_textLeft, 0);
            if (mLeftStrId != 0)
                mLeftText.setText(mLeftStrId);
            a.recycle();
        }
    }

    private void initLeftText() {
        mLeftText = new TextView(getContext());
        setDefaultStyle(mLeftText);
        ((LayoutParams) mLeftText.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;

        addView(mLeftText);
    }

    private void initRightText() {
        mRightText = new TextView(getContext());
        setDefaultStyle(mRightText);
        ((FrameLayout.LayoutParams) mRightText.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;

        mRightText.setCompoundDrawablePadding((int) getContext().getResources().getDimension(R.dimen.dms_12));
        mRightText.setPadding(0, 0, (int) getContext().getResources().getDimension(R.dimen.dms_26), 0);
        mRightText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrow_right, 0);
        addView(mRightText);
    }

    private void setDefaultStyle(TextView textView) {
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(getContext().getResources().getColor(R.color.gray_32));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.font_28));
        textView.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setRightText(int resString) {
        mRightStrId = resString;
        if (mRightText != null)
            mRightText.setText(resString);
    }

    /**
     *
     * @param text String方式的设值不随语言切换而变
     */
    public void setRightText(String text) {
        if (mRightText != null)
            mRightText.setText(text);
    }

    public void fresh() {
        if(mLeftStrId !=0 && mLeftText != null)
            mLeftText.setText(mLeftStrId);
        if(mRightStrId !=0 && mRightText != null)
            mRightText.setText(mRightStrId);
    }

}
