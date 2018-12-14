package com.expo.module.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.expo.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkAdapter extends BaseAdapter {

    Context mContext;
    List<String> mData;
    Map<String, Integer> mDataSource;
    public int mPosition = -1;

    public WorkAdapter(Context context) {
        mContext = context;
    }

    public void setSource(Map<String, Integer> dataSource) {
        this.mDataSource = dataSource;
    }

    public void setData(List<String> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_info_work, null);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        if (mPosition == position)
            holderView.text.setSelected(true);
        else
            holderView.text.setSelected(false);
        holderView.text.setText(mDataSource.get(mData.get(position)));
        return convertView;
    }

    class HolderView {
        @BindView(R.id.user_work)
        TextView text;

        public HolderView(View view) {
            text = view.findViewById(R.id.user_work);
        }
    }
}
