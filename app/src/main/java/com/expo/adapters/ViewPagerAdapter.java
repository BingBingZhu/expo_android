package com.expo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expo.R;
import com.expo.entity.RollData;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<RollData> mRolls;

    public ViewPagerAdapter(Context context, List<RollData> rolls){
        this.mContext = context;
        this.mRolls = rolls;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_banner_item, null);
        SimpleDraweeView img = v.findViewById(R.id.banner_image);
        TextView tvName = v.findViewById(R.id.banner_name);
        TextView tvCount = v.findViewById(R.id.banner_count);
        RollData roll = mRolls.get(position);
        img.setImageURI(roll.getUrl());
        tvName.setText(roll.getName());
        tvCount.setText(roll.getCount()+"次");
        return v;
    }
}
