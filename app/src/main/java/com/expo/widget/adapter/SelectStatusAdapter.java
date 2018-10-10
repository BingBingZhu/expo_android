package com.expo.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.expo.widget.SimpleHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LS on 2017/11/21.
 * 带有先选中状态的适配器
 */

public abstract class SelectStatusAdapter<VH extends SimpleHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {
    private int selectedPosition;
    private List<View.OnClickListener> clickListeners;
    private boolean needSelectedState = true;

    /**
     * 获取当前选中项下标
     *
     * @return
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * 设置默认选中项下标 默认0
     *
     * @param position
     */
    public void setDefaultSelected(int position) {
        this.selectedPosition = position;
    }

    /**
     * 设置选中项下标
     *
     * @param position
     */
    public void setSelectedPosition(int position) {
        if (position < 0 || position > getItemCount()) return;
        int tmp = selectedPosition;
        this.selectedPosition = position;
//        notifyDataSetChanged();
        notifyItemChanged( tmp );                                                                     //更新之前的选中项
        notifyItemChanged( selectedPosition );                                                        //更新新选中项
    }


    public void setNeedSelectedState(boolean need) {
        needSelectedState = need;
    }

    public boolean isNeedSelectedState() {
        return needSelectedState;
    }

    /**
     * 添加项点击监听
     *
     * @param clickListener
     */
    public void addOnClickListener(View.OnClickListener clickListener) {
        if (clickListener == null) return;
        if (clickListeners == null) {
            clickListeners = new ArrayList<>();
        }
        if (!this.clickListeners.contains( clickListener ))
            this.clickListeners.add( clickListener );
    }

    /**
     * 移除项点击监听
     *
     * @param clickListener
     */
    public void removeOnClickListener(View.OnClickListener clickListener) {
        if (clickListener == null || clickListeners == null || clickListeners.isEmpty()) return;
        if (this.clickListeners.contains( clickListener ))
            this.clickListeners.remove( clickListener );
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH vh = getViewHolder( parent, viewType );
        if (vh != null)
            vh.itemView.setOnClickListener( this );
        return vh;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        if (needSelectedState && position == selectedPosition) {
            holder.itemView.setSelected( true );
        } else {
            holder.itemView.setSelected( false );
        }
        onBindCustomViewHolder( holder, position );
    }

    @Override
    public void onClick(View v) {
        if (clickListeners != null && !clickListeners.isEmpty()) {
            for (View.OnClickListener clickListener : clickListeners) {
                clickListener.onClick( v );
            }
        }
        int tmp = selectedPosition;
        selectedPosition = ((SimpleHolder) v.getTag()).getAdapterPosition();
        notifyItemChanged( tmp );
        notifyItemChanged( selectedPosition );
    }

    /**
     * 获取指定项
     *
     * @param position
     * @return
     */
    public abstract Object getItem(int position);

    /**
     * 获取ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract VH getViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定用户holder数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindCustomViewHolder(VH holder, int position);
}