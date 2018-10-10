package com.expo.widget.decorations;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int hSpace;
    private int vSpace;

    public SpaceDecoration(int space) {
        this.hSpace = space;
        this.vSpace = space;
    }

    public SpaceDecoration(int hSpace, int vSpace) {
        this.hSpace = hSpace;
        this.vSpace = vSpace;
    }

    public SpaceDecoration(int left, int top, int right, int bottom, int space) {
        this.hSpace = space;
        this.vSpace = space;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
    }

    public SpaceDecoration(int left, int top, int right, int bottom, int hSpace, int vSpace) {
        this.hSpace = hSpace;
        this.vSpace = vSpace;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets( outRect, view, parent, state );
        int position = parent.getChildAdapterPosition( view );
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
            int orientation = ((GridLayoutManager) manager).getOrientation();
            int x = position % spanCount;//列
            int y = position / spanCount;//行
            int lineCount = (int) Math.ceil( state.getItemCount() / (float) spanCount );//总行数，从1开始
            int yLine = (int) Math.ceil( (position + 1) / (float) spanCount );//当前从1开始的行
            //判断是否总共一行或当前项在最后一行
            if (lineCount == 1 || yLine == lineCount) {
                if (orientation == LinearLayoutManager.VERTICAL) {
                    int endSize = (spanCount == 2 ? hSpace / 2 : (hSpace * 2) / 3);
                    int middleSize = (spanCount == 2 ? hSpace / 2 : hSpace / 3);
                    if (x == 0) {
                        outRect.set( left, top, right + endSize, bottom );
                    } else if (x == spanCount - 1) {
                        outRect.set( left + endSize, top, right, bottom );
                    } else {
                        outRect.set( left + middleSize, top, right + middleSize, bottom );
                    }
                } else {
                    int endSize = (spanCount == 2 ? vSpace / 2 : (vSpace * 2) / 3);
                    int middleSize = (spanCount == 2 ? vSpace / 2 : vSpace / 3);
                    if (y == 0) {
                        outRect.set( left, top, right, bottom + endSize );
                    } else if (y == spanCount - 1) {
                        outRect.set( left, top + endSize, right, bottom );
                    } else {
                        outRect.set( left, top + middleSize, right, bottom + middleSize );
                    }
                }
            } else {
                if (orientation == LinearLayoutManager.VERTICAL) {
                    int endSize = (spanCount == 2 ? hSpace / 2 : (hSpace * 2) / 3);
                    int middleSize = (spanCount == 2 ? hSpace / 2 : hSpace / 3);
                    if (x == 0) {
                        outRect.set( left, top, right + endSize, bottom + vSpace );
                    } else if (x == spanCount - 1) {
                        outRect.set( left + endSize, top, right, bottom + vSpace );
                    } else {
                        outRect.set( left + middleSize, top, right + middleSize, bottom + vSpace );
                    }
                } else {
                    int endSize = (spanCount == 2 ? vSpace / 2 : (vSpace * 2) / 3);
                    int middleSize = (spanCount == 2 ? vSpace / 2 : vSpace / 3);
                    if (y == 0) {
                        outRect.set( left, top, right + hSpace, bottom + endSize );
                    } else if (y == spanCount - 1) {
                        outRect.set( left, top + endSize, right + hSpace, bottom );
                    } else {
                        outRect.set( left, top + middleSize, right + hSpace, bottom + middleSize );
                    }
                }
            }
        } else if (manager instanceof LinearLayoutManager) {
            if (position != state.getItemCount() - 1) {
                int orientation = ((LinearLayoutManager) manager).getOrientation();
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    outRect.set( left, top, right + hSpace, bottom );
                } else {
                    outRect.set( left, top, right, bottom + vSpace );
                }
            } else {
                outRect.set( left, top, right, bottom );
            }
        }
    }
}