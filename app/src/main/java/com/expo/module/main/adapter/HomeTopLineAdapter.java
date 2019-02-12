package com.expo.module.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.ExpoActivityInfo;
import com.expo.utils.LanguageUtil;
import com.expo.widget.LimitScrollerView;

import java.util.List;

public class HomeTopLineAdapter implements LimitScrollerView.LimitScrollAdapter {

    private List<ExpoActivityInfo> mData;
    private Context mContext;

    public HomeTopLineAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List<ExpoActivityInfo> datas) {
        mData = datas;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public View getView(int index) {
        ExpoActivityInfo topLineInfo = mData.get(index);
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_ad, null, false);
        TextView text = (TextView) itemView.findViewById(R.id.text);
        text.setText(LanguageUtil.chooseTest(topLineInfo.getRemark(), topLineInfo.getRemarkEn()));
        itemView.setTag(topLineInfo);
        return itemView;
    }
}
