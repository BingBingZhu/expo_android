package com.expo.widget.laminatedbanner.holder;

/**
 * Created by on 17/5/26.
 */

public interface LaminatedHolderCreator<VH extends LaminatedViewHolder> {
    /**
     * 创建ViewHolder
     * @return
     */
    public VH createViewHolder();
}
