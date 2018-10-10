package com.expo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.expo.R;


/**
 * Created by LS on 2017/11/1.
 */
public class SimpleRecyclerView extends RecyclerView {

    private View emptyView;
    private int emptyDataOffset;
    private boolean needHideWhenEmptyShowd = true;

    public SimpleRecyclerView(Context context) {
        this( context, null );
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle );
        init( attrs );
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes( attrs, R.styleable.SimpleRecyclerView );
        boolean hasDivide = a.getBoolean( R.styleable.SimpleRecyclerView_hasDivide, false );
        if (hasDivide) {
            setHasDivide();
        }
        a.recycle();
    }

    /**
     * 设置是否有分割线
     */
    public void setHasDivide() {
        LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) manager).getOrientation();
            addItemDecoration( new DividerItemDecoration( getContext(), orientation ) );
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (getAdapter() != null) {
            getAdapter().unregisterAdapterDataObserver( observer );
        }
        super.setAdapter( adapter );
        adapter.registerAdapterDataObserver( observer );
        showEmptyView();
    }

    private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            showEmptyView();
        }
    };

    /**
     * 显示空数据提示视图
     */
    private void showEmptyView() {
        if (shouldShowEmptyView()) {
            emptyView.setVisibility( VISIBLE );
            if (needHideWhenEmptyShowd) {
                this.setVisibility( GONE );
            }
        } else if (emptyView != null) {
            emptyView.setVisibility( GONE );
            if (needHideWhenEmptyShowd) {
                this.setVisibility( VISIBLE );
            }
        }
    }

    /**
     * 判断是否需要显示空数据提示视图
     *
     * @return
     */
    private boolean shouldShowEmptyView() {
        if (getAdapter() == null) {
            return false;
        }
        int count = getAdapter().getItemCount();
        if (emptyView != null && count <= emptyDataOffset) {
            return true;
        }
        return false;
    }

    /**
     * 设置数据为空时提示视图
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        showEmptyView();
    }

    public void removeEmptyView(View emptyView) {
        this.emptyView = emptyView;
        emptyView.setVisibility( GONE );
    }

    /**
     * 设置当数据数量小于多少时会出现空数据视图提示，默认为0
     *
     * @param emptyDataOffset
     */
    public void setEmptyDataOffset(int emptyDataOffset) {
        this.emptyDataOffset = emptyDataOffset;
    }


    /**
     * 设置显示空数据视图时是否隐藏列表视图，默认为true
     *
     * @param needHideWhenEmptyShowd
     */
    public void setNeedHideWhenEmptyShowd(boolean needHideWhenEmptyShowd) {
        this.needHideWhenEmptyShowd = needHideWhenEmptyShowd;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            callOnClick();
        }
        return super.onTouchEvent( e );
    }
}
