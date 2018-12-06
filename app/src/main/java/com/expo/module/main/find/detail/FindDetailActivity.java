package com.expo.module.main.find.detail;

import android.app.Activity;
import android.app.Dialog;
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

    List<Find> mList;
    CommonAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_find_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, getIntent().getStringExtra(Constants.EXTRAS.EXTRA_TITLE));

        mList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycler.setLayoutManager(manager);
        new PagerSnapHelper().attachToRecyclerView(mRecycler);
        mRecycler.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_find_detail, mList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }
        });
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Activity context, ArrayList<Find> list) {
        Intent in = new Intent(context, FindDetailActivity.class);
        in.putParcelableArrayListExtra(Constants.EXTRAS.EXTRAS, list);
        context.startActivity(in);
    }

    @OnClick(R.id.find_detail_close)
    public void clickClose(View view) {
        finish();
    }

}
