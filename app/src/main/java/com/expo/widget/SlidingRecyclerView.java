package com.expo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.expo.R;

/**
 * Created by tangyangkai on 16/6/12.
 * 可以侧滑删除项的RecyclerView
 */
public class SlidingRecyclerView extends SimpleRecyclerView {
    public static final int STATUS_CLOSED = 1;
    public static final int STATUS_CLOSING = 2;
    public static final int STATUS_OPENING = 3;
    public static final int STATUS_OPENED = 4;

    private final int SLIDING_NONE = 10;
    private final int SLIDING_HORIZONTAL = 11;
    private final int SLIDING_VERTICAL = 12;

    private View itemLayout;
    private SlidingHolder mOpenedHolder;
    private int mTouchSlop, pos;
    private int mStatus = STATUS_CLOSED;                  //项的滑动开/关状态
    private int slidingOrientation = SLIDING_NONE;      //触发滑动的方向，横向还是纵向
    private Rect itemBounds;
    private PointF lastDown;                                //上一次按下的位置坐标
    private int maxSlidingDistance;                         //最大滑动距离
    private boolean animating;                              //滑动后正在运动到最终位置的动画中
    private boolean actionStartWithDown;                    //滑动操作是否有按下开始
    private OnItemClickListener deleteListener;
    private int deleteItemPosition = -1;

    public SlidingRecyclerView(Context context) {
        this( context, null );
    }

    public SlidingRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public SlidingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle );
        mTouchSlop = ViewConfiguration.get( context ).getScaledTouchSlop();
        itemBounds = new Rect();
        lastDown = new PointF();
    }

    public void setOnItemClickListener(OnItemClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof SlidingAdapter) {
            super.setAdapter( adapter );
        } else {
            throw new ClassCastException( "adapter can't cast to SlidingAdapter" );
        }
    }

    @Override
    public SlidingAdapter getAdapter() {
        return (SlidingAdapter) super.getAdapter();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (animating) return true;
        int count = getChildCount();
        if (count == 0) return super.onTouchEvent( event );
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pos = -1;
                lastDown.set( x, y );
                //通过点击的坐标计算当前的position
                int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                for (int i = count - 1; i >= 0; i--) {
                    final View child = getChildAt( i );
                    if (child.getVisibility() == View.VISIBLE) {
                        child.getHitRect( itemBounds );
                        if (itemBounds.contains( x, y )) {
                            pos = mFirstPosition + i;
                        }
                    }
                }
                if (pos == -1) {
                    if (mStatus == STATUS_OPENED) {
                        close();
                    }
                    return true;
                }
                if (mStatus == STATUS_OPENED) {
                    mOpenedHolder.btDelete.getHitRect( itemBounds );
                    itemBounds.set( itemBounds.left, mOpenedHolder.itemView.getTop(), itemBounds.right, mOpenedHolder.itemView.getBottom() );
                    if (itemBounds.contains( x, y )) {
                        deleteItemPosition = pos;
                    }
                    close();
                    return true;
                }
                //通过position得到item的viewHolder
                View view = getChildAt( pos - mFirstPosition );
                if (view == null) return true;
                SlidingHolder viewHolder = (SlidingHolder) getChildViewHolder( view );
                itemLayout = viewHolder.customView;
                mOpenedHolder = viewHolder;
                if (maxSlidingDistance == 0)
                    maxSlidingDistance = viewHolder.btDelete.getWidth();
                actionStartWithDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!actionStartWithDown) return true;
                float dx = x - lastDown.x;
                float dy = y - lastDown.y;
                lastDown.set( x, y );
                //判断首次触发的是横滑还是竖滑
                if (slidingOrientation == SLIDING_NONE && Math.abs( dy ) > mTouchSlop && Math.abs( dx ) < Math.abs( dy )) {
                    slidingOrientation = SLIDING_VERTICAL;
                } else if (slidingOrientation == SLIDING_NONE && Math.abs( dx ) > mTouchSlop && Math.abs( dx ) > Math.abs( dy )) {
                    slidingOrientation = SLIDING_HORIZONTAL;
                }
                //非横滑使用默认处理
                if (slidingOrientation != SLIDING_HORIZONTAL) break;
                if (mStatus == STATUS_CLOSED) {
                    mStatus = STATUS_OPENING;
                } else if (mStatus == STATUS_OPENED) {
                    mStatus = STATUS_CLOSING;
                }
                //横滑处理
                if (itemLayout.getLeft() >= 0 && dx >= 0) {
                    itemLayout.layout( 0, itemLayout.getTop(), itemLayout.getWidth(), itemLayout.getBottom() );
                } else if (Math.abs( itemLayout.getLeft() ) >= maxSlidingDistance && dx < 0) {
                    itemLayout.layout( -maxSlidingDistance, itemLayout.getTop(), -maxSlidingDistance + itemLayout.getWidth(), itemLayout.getBottom() );
                } else {
                    itemLayout.layout( (int) (itemLayout.getLeft() + dx), itemLayout.getTop(), (int) (itemLayout.getRight() + dx), itemLayout.getBottom() );
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (actionStartWithDown && slidingOrientation == SLIDING_NONE && deleteListener != null) {
                    deleteListener.onItemClick( this, pos );
                }
                if (slidingOrientation == SLIDING_HORIZONTAL) {
                    if (Math.abs( itemLayout.getLeft() ) >= maxSlidingDistance / 2) {
                        open();
                    } else {
                        close();
                    }
                }
                actionStartWithDown = false;
                slidingOrientation = SLIDING_NONE;
                break;
        }
        return super.onTouchEvent( event );
    }

    /**
     * 打开选中的项
     */
    private void open() {
        if (Math.abs( itemLayout.getLeft() ) == maxSlidingDistance) {
            mStatus = STATUS_OPENED;
        } else {
            mStatus = STATUS_OPENING;
            sliding( -maxSlidingDistance - itemLayout.getLeft() );
        }
    }

    /**
     * 关闭选中的项
     */
    private void close() {
        if (Math.abs( itemLayout.getLeft() ) == 0) {
            mStatus = STATUS_CLOSED;
            itemLayout = null;
            mOpenedHolder = null;
        } else {
            mStatus = STATUS_CLOSING;
            sliding( -itemLayout.getLeft() );
        }
    }

    private int i;

    /**
     * 动画滑动指定的距离
     *
     * @param distance
     */
    private void sliding(final int distance) {
        animating = true;
        final int pre = distance * 1 / 5;
        i = 0;
        postDelayed( new Runnable() {
            @Override
            public void run() {
                i++;
                if (i < 5) {
                    itemLayout.layout( itemLayout.getLeft() + pre, itemLayout.getTop(),
                            itemLayout.getRight() + pre, itemLayout.getBottom() );
                    postDelayed( this, 20 );
                } else {                                                                            //最后一次滑动
                    if (distance > 0) {                                                             //大于0表示是关闭
                        itemLayout.layout( 0, itemLayout.getTop(), itemLayout.getWidth(),
                                itemLayout.getBottom() );
                        mStatus = STATUS_CLOSED;
                    } else {                                                                        //小于0表示是打开
                        itemLayout.layout( -maxSlidingDistance, itemLayout.getTop(),
                                -maxSlidingDistance + itemLayout.getWidth(), itemLayout.getBottom() );
                        mStatus = STATUS_OPENED;
                    }
                    if (deleteItemPosition != -1) {                                                 //如果要删除的项存在，则移除此项
                        Object obj = getAdapter().removeItem( pos );
                        if (deleteListener != null)
                            deleteListener.onItemDelete( SlidingRecyclerView.this, obj );//通知触发删除操作
                        deleteItemPosition = -1;
                    }
                    animating = false;
                }
            }
        }, 20 );
    }

    /**
     * 侧滑删除Holder，在布局中提供一个删除按钮
     */
    public static class SlidingHolder extends ViewHolder {

        public View customView;
        public View btDelete;

        public SlidingHolder(View itemView) {
            super( LayoutInflater.from( itemView.getContext() ).inflate( R.layout.layout_sliding_item_delete, null ) );
            this.itemView.setLayoutParams( new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
            btDelete = this.itemView.findViewById( R.id.sliding_item_delete );
            this.customView = itemView;
            if (this.customView.getBackground() == null) {
                this.customView.setBackgroundColor( Color.WHITE );
            }
            ((ViewGroup) this.itemView).addView( itemView );
        }
    }

    public abstract static class SlidingAdapter<VH extends SlidingHolder> extends Adapter<VH> {
        public abstract Object removeItem(int position);
    }

    public interface OnItemClickListener {
        /**
         * 侧滑删除回调
         *
         * @param recyclerView
         * @param obj          被删除项
         */
        void onItemDelete(RecyclerView recyclerView, Object obj);

        /**
         * 项被点击回调
         *
         * @param recyclerView
         * @param position     点击项的下标
         */
        void onItemClick(RecyclerView recyclerView, int position);
    }
}
