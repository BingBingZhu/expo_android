package com.expo.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class SimpleHolder extends RecyclerView.ViewHolder {

    public SimpleHolder(View itemView) {
        super( itemView );
        ButterKnife.bind( this, this.itemView );
        this.itemView.setTag( this );
        this.itemView.setLayoutParams( new RecyclerView.LayoutParams( RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT ) );
    }
}
