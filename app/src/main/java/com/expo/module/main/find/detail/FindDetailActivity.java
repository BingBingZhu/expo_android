package com.expo.module.main.find.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FindPublishContract;
import com.expo.entity.Find;
import com.expo.module.camera.CameraActivity;
import com.expo.module.main.find.publish.FindPublishAdapter;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.expo.utils.Constants.EXTRAS.EXTRAS;

public class FindDetailActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    Find mFind;
    CommonAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_find_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        mRootView.setTopPadding();
        mFind = getIntent().getParcelableExtra(Constants.EXTRAS.EXTRAS);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(manager);
        new PagerSnapHelper().attachToRecyclerView(mRecycler);
        mRecycler.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_find_detail, mFind.picUrl) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                Picasso.with(FindDetailActivity.this).load(mFind.picUrl.get(position)).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.item_find_img));
                Picasso.with(FindDetailActivity.this).load(mFind.head).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((ImageView) holder.getView(R.id.item_find_head));
                holder.setText(R.id.item_find_name, mFind.name);
                holder.setText(R.id.item_find_content, mFind.content);
                holder.setText(R.id.item_find_position, (position + 1) + "/" + mFind.picUrl.size());
                holder.setText(R.id.item_find_like_count, mFind.like);
                holder.setText(R.id.item_find_scans_count, mFind.scans);
            }

        });
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context, Find find) {
        Intent in = new Intent(context, FindDetailActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, find);
        context.startActivity(in);
    }

    @OnClick(R.id.find_detail_close)
    public void clickClose(View view) {
        finish();
    }

}
