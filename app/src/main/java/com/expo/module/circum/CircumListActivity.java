package com.expo.module.circum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.entity.Circum;
import com.expo.entity.VrInfo;
import com.expo.module.online.detail.VRDetailActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hedan.textdrawablelibrary.TextViewDrawable;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CircumListActivity extends BaseActivity {

    @BindView(R.id.circun_list_recyclerview)
    RecyclerView mRecyclerView;

    private int mCircumType;   // 周边类型 1美食 2酒店 3购物 4交通 5景区

    private CommonAdapter mCateAdapter;
    private CommonAdapter mHotelAdapter;

    @Override
    protected int getContentView() {
        mCircumType = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_CIRCUN_TYPE, 0);
        switch (mCircumType){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return R.layout.activity_circum_list;
            default:
                return 0;
        }
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        switch (mCircumType){
            case 1:
                setTitle(1, "周边美食");
                initRecyclerView(mCircumType);
                break;
            case 2:
                setTitle(1, "周边酒店");
                initRecyclerView(mCircumType);
                break;
            case 3:
                setTitle(1, "周边购物");
                initRecyclerView(mCircumType);
                break;
            case 4:
                setTitle(1, "周边交通");
                break;
            case 5:
                setTitle(1, "周边景区");
                initRecyclerView(mCircumType);
                break;
        }
    }

    private void initRecyclerView(int circunType) {
        int vSpace = (int) getResources().getDimension(R.dimen.dms_20);
        List<Circum> data = new ArrayList<>();
        switch (circunType){
            case 1:
//                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
//                int margin = (int) getResources().getDimension(R.dimen.dms_30);
//                params.setMargins(margin, 0, margin, 0);
//                mRecyclerView.setLayoutParams(params);
                mCateAdapter = new CommonAdapter<Circum>(this, R.layout.item_circum_cate, data) {
                    @Override
                    protected void convert(ViewHolder holder, Circum circum, int position) {
                        holder.<SimpleDraweeView>getView(R.id.circun_cate_item_img).setImageURI(Constants.URL.FILE_BASE_URL + circum.getUrl());
                        holder.setText(R.id.circun_cate_item_name, LanguageUtil.chooseTest(circum.getCaption(), circum.getCaptionEn()));
                        holder.setText(R.id.circun_cate_item_score, "评分："+circum.getScore()+"分");
                        holder.setText(R.id.circun_cate_item_price, "￥"+circum.getPrice());
                        holder.setText(R.id.circun_cate_item_distance, "距离您"+getDistance());
                        holder.itemView.setOnClickListener(null);
                    }
                };
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                int leftRight = (int) getResources().getDimension(R.dimen.dms_14);
                mRecyclerView.addItemDecoration(new SpaceDecoration( leftRight, vSpace ));
                mRecyclerView.setAdapter(mCateAdapter);
                break;
            case 2:
                mHotelAdapter = new CommonAdapter<Circum>(this, R.layout.item_circum_hotel, data) {
                    @Override
                    protected void convert(ViewHolder holder, Circum circum, int position) {
                        holder.<SimpleDraweeView>getView(R.id.circun_hotel_item_img).setImageURI(Constants.URL.FILE_BASE_URL + circum.getUrl());
                        holder.setText(R.id.circun_hotel_item_name, LanguageUtil.chooseTest(circum.getCaption(), circum.getCaptionEn()));
                        holder.setText(R.id.circun_hotel_item_score, "评分："+circum.getScore()+"分");
                        holder.setText(R.id.circun_hotel_item_price, "￥"+circum.getPrice());
                        holder.setText(R.id.circun_hotel_item_type, "经济型");
                        holder.setText(R.id.circun_hotel_item_distance, "距离您"+getDistance());
                        holder.itemView.setOnClickListener(null);
                        holder.setOnClickListener(R.id.circun_hotel_item_telephone, null);
                        holder.setOnClickListener(R.id.circun_hotel_item_navi, null);
                    }
                };
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.addItemDecoration(new SpaceDecoration(vSpace));
                mRecyclerView.setAdapter(mHotelAdapter);
                break;
            case 3:
                setTitle(0, "周边购物");
                break;
            case 5:
                setTitle(0, "周边景区");
                break;
            case 4:
                setTitle(0, "周边交通");
                break;
        }
    }

    private String getDistance() {
        return "1.2km";
    }
    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(Context context, int circumType) {
        Intent intent =  new Intent( context, CircumListActivity.class );
        intent.putExtra(Constants.EXTRAS.EXTRA_CIRCUN_TYPE, circumType);
        context.startActivity(intent);
    }
}
