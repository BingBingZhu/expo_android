package com.expo.module.circum;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.CirnumListContract;
import com.expo.entity.Circum;
import com.expo.map.NaviManager;
import com.expo.module.webview.WebActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.StarBar;
import com.expo.widget.decorations.SpaceDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class CircumListActivity extends BaseActivity<CirnumListContract.Presenter> implements CirnumListContract.View {

    @BindView(R.id.circun_list_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_view)
    PtrClassicFrameLayout mPtrView;

    private int mCircumType;   // 周边类型 1美食 2酒店 3购物 4景区

    private CommonAdapter mCateAdapter;
    private CommonAdapter mHotelAdapter;
    private CommonAdapter mShopScenicAdapter;
    private int page = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_circum_list;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mCircumType = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_CIRCUN_TYPE, 0);
        switch (mCircumType){
            case 1:
                setTitle(1, "周边美食");
                break;
            case 2:
                setTitle(1, "周边酒店");
                break;
            case 3:
                setTitle(1, "周边购物");
                break;
            case 4:
                setTitle(1, "周边景区");
                break;
        }
        mCircums = new ArrayList<>();
        initRecyclerView(mCircumType, mCircums);
        showLoadingView();
        mPresenter.loadCircumData(mCircumType, page);
        initLoadMore();
    }

    private void initLoadMore() {
        mPtrView.setMode(PtrFrameLayout.Mode.LOAD_MORE);
        mPtrView.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page++;
                mPresenter.loadCircumData(mCircumType, page);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
    }

    private void initRecyclerView(int circunType, List<Circum> data) {
        int vSpace = (int) getResources().getDimension(R.dimen.dms_20);
        switch (circunType){
            case 1:     // 美食
                mCateAdapter = new CommonAdapter<Circum>(this, R.layout.item_circum_cate, data) {
                    @Override
                    protected void convert(ViewHolder holder, Circum circum, int position) {
                        holder.<SimpleDraweeView>getView(R.id.circun_cate_item_img).setImageURI(circum.getPhotoUrls());
                        holder.setText(R.id.circun_cate_item_name, circum.getName());
                        holder.setText(R.id.circun_cate_item_score, "评分："+circum.getTaste()+"分");
                        holder.setText(R.id.circun_cate_item_price, "￥"+circum.getAvgPrice());
                        holder.setText(R.id.circun_cate_item_distance, circum.getAddress()/*"距离您" + mPresenter.getDistance(circum.getLatitude(), circum.getLongitude())*/);
                        holder.itemView.setOnClickListener(v -> WebActivity.startActivity(getContext(), circum.getBusinessUrl(), circum.getName()));
                    }
                };
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                int leftRight = (int) getResources().getDimension(R.dimen.dms_14);
                mRecyclerView.addItemDecoration(new SpaceDecoration( leftRight, vSpace ));
                mRecyclerView.setAdapter(mCateAdapter);
                break;
            case 2:     // 酒店
                mHotelAdapter = new CommonAdapter<Circum>(this, R.layout.item_circum_hotel, data) {
                    @Override
                    protected void convert(ViewHolder holder, Circum circum, int position) {
                        holder.<SimpleDraweeView>getView(R.id.circun_hotel_item_img).setImageURI(circum.getPhotoUrls());
                        holder.setText(R.id.circun_hotel_item_name, circum.getName());
                        holder.setText(R.id.circun_hotel_item_score, "评分："+circum.getService()+"分");
                        holder.setText(R.id.circun_hotel_item_price, "￥"+circum.getAvgPrice());
                        String type = "经济型";
                        for (String s : circum.getCategories()){
                            if (s.indexOf("型") > 0){
                                type = s;
                            }
                        }
                        holder.setText(R.id.circun_hotel_item_type, type);
                        holder.setText(R.id.circun_hotel_item_distance, circum.getAddress()/*"距离您" + mPresenter.getDistance(circum.getLatitude(), circum.getLongitude())*/);
                        holder.itemView.setOnClickListener(v -> WebActivity.startActivity(getContext(), circum.getBusinessUrl(), circum.getName()));
                        holder.setOnClickListener(R.id.circun_hotel_item_telephone, v -> telephone(circum.getTelephone()));
                        holder.setOnClickListener(R.id.circun_hotel_item_navi, v -> navi(circum.getName(), circum.getLatitude(), circum.getLongitude()));
                    }
                };
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.addItemDecoration(new SpaceDecoration(vSpace));
                mRecyclerView.setAdapter(mHotelAdapter);
                break;
            case 3:     // 购物
            case 4:     // 景区
                mShopScenicAdapter = new CommonAdapter<Circum>(this, R.layout.item_circum_shop_and_scenic, data) {
                    @Override
                    protected void convert(ViewHolder holder, Circum circum, int position) {
                        holder.<SimpleDraweeView>getView(R.id.circun_shop_and_scenic_item_img).setImageURI(circum.getPhotoUrls());
                        holder.setText(R.id.circun_shop_and_scenic_item_name, circum.getName());
                        holder.setText(R.id.circun_shop_and_scenic_item_score, "评分："+circum.getDecoration()+"分");
                        holder.<StarBar>getView(R.id.circun_shop_and_scenic_item_star).setStarMark((circum.getDecoration()/2F));
                        holder.setText(R.id.circun_shop_and_scenic_item_distance, circum.getAddress()/*"距离您" + mPresenter.getDistance(circum.getLatitude(), circum.getLongitude())*/);
                        holder.itemView.setOnClickListener(v -> WebActivity.startActivity(getContext(), circum.getBusinessUrl(), circum.getName()));
                    }
                };
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.addItemDecoration(new SpaceDecoration(vSpace));
                mRecyclerView.setAdapter(mShopScenicAdapter);
                break;
        }
    }

    private void navi(String name, double latitude, double longitude) {
        NaviManager.getInstance(getContext()).showSelectorNavi(name, latitude, longitude);
    }

    private Dialog dialog;

    private void telephone(String telephone) {
        dialog = new Dialog(getContext(), R.style.BottomActionSheetDialogStyle);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_telephone, null);
        Button btn = v.findViewById(R.id.dialog_telephone);
        Button btnCancel = v.findViewById(R.id.btn_cancel);
        btn.setText("酒店电话：" + telephone);
        btn.setOnClickListener(v1 -> {
            Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( "tel:" + telephone ) );
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity( intent );
            dialog.dismiss();
        });
        btnCancel.setOnClickListener(v12 -> dialog.dismiss());
        dialog.setContentView(v);
        Window dialogWindow = dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, int circumType) {
        Intent intent =  new Intent( context, CircumListActivity.class );
        intent.putExtra(Constants.EXTRAS.EXTRA_CIRCUN_TYPE, circumType);
        context.startActivity(intent);
    }

    private ArrayList<Circum> mCircums;

    @Override
    public void loadCircumDataRes(ArrayList<Circum> circums) {
        if (null == circums || circums.size() == 0) {
            if (page > 0) {
                page--;
                ToastHelper.showShort(R.string.no_more_data_available);
            }
        } else {
            this.mCircums.addAll(circums);
        }
        mPtrView.refreshComplete();
        switch (mCircumType){
            case 1:
                mCateAdapter.notifyDataSetChanged();
                break;
            case 2:
                mHotelAdapter.notifyDataSetChanged();
                break;
            case 3:
            case 4:
                mShopScenicAdapter.notifyDataSetChanged();
                break;
        }
    }
}
