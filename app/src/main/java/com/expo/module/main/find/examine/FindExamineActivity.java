package com.expo.module.main.find.examine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ImageUtils;
import com.expo.contract.ExamineContract;
import com.expo.contract.FindDetailContract;
import com.expo.entity.Find;
import com.expo.module.main.find.detail.FindDetailActivity;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.widget.AppBarView;
import com.expo.widget.CustomDefaultDialog;
import com.expo.widget.decorations.SpaceDecoration;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FindExamineActivity extends BaseActivity<ExamineContract.Presenter> implements ExamineContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    List<Find> mData;
    CommonAdapter mAdapter;
    boolean mIsShowRight = false;
    Handler mHandler = new Handler();

    @Override
    protected int getContentView() {
        return R.layout.activity_routes;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, getIntent().getIntExtra(Constants.EXTRAS.EXTRA_TITLE, 0));
        mIsShowRight = getIntent().getBooleanExtra(Constants.EXTRAS.EXTRAS, false);
        if (mIsShowRight)
            initTitleRightTextView(R.string.examine_wait, R.color.color_333,
                    v -> FindExamineActivity.startActivity(FindExamineActivity.this, R.string.examine_wait, false));

        mData = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int marginV = getResources().getDimensionPixelSize(R.dimen.dms_68);
        int lr = getResources().getDimensionPixelSize(R.dimen.dms_28);
        mRecyclerView.addItemDecoration(new SpaceDecoration(lr, marginV, lr, 0, 0));
        mRecyclerView.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_examine, mData) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                Find find = mData.get(position);
                if (!StringUtils.isEmpty(find.url1))
                    if (find.url1.endsWith(".mp4")) {
                        holder.setVisible(R.id.item_find_video_control, true);
                        new Thread(() -> {
                            Bitmap bitmap = ImageUtils.createVideoThumbnail(CommUtils.getFullUrl(find.url1), MediaStore.Images.Thumbnails.MINI_KIND);
                            if (mHandler != null)
                                mHandler.post(() -> {
                                    if (bitmap != null && holder.getView(R.id.item_examine_img) != null)
                                        ((ImageView) holder.getView(R.id.item_examine_img)).setImageBitmap(bitmap);
                                });
                        }).start();

                    } else {
                        holder.setVisible(R.id.item_find_video_control, false);
                        CommUtils.setImgPic(FindExamineActivity.this, CommUtils.getFullUrl(find.url1), (ImageView) holder.getView(R.id.item_examine_img));
                    }
                if (mIsShowRight) {
                    holder.setText(R.id.item_find_scans, find.views);
                    holder.setText(R.id.item_find_like, find.enjoys);
                    holder.setVisible(R.id.item_find_bottom_layout, true);
                    holder.setVisible(R.id.item_examine_delete, true);

                    holder.setOnClickListener(R.id.item_examine_delete, v -> showDeleteDialog(find.id, find.type, position));
                } else {
                    holder.setVisible(R.id.item_find_bottom_layout, false);
                    holder.setVisible(R.id.item_examine_delete, false);
                }

                holder.setText(R.id.item_examine_time, find.createtime);
                holder.setText(R.id.item_examine_content, find.caption);
                holder.setText(R.id.item_examine_state, getString(find.getStateRes()));
                holder.setTextColor(R.id.item_examine_state, getResources().getColor(find.getStateColor()));
                holder.setVisible(R.id.item_examine_state_layout, !mIsShowRight);
                holder.setVisible(R.id.item_examine_reason, false);
                if (!mIsShowRight) {
                    holder.setText(R.id.item_examine_state, getString(find.getStateRes()));
                    holder.setTextColor(R.id.item_examine_state, getResources().getColor(find.getStateColor()));
                    if (find.state == 1) {
                        holder.getView(R.id.item_examine_state_layout).setSelected(true);
                    } else if (find.state == 1) {
                    } else {
                        holder.setVisible(R.id.item_examine_reason, true);
                        holder.getView(R.id.item_examine_state_layout).setSelected(false);
                    }
                }

                holder.itemView.setOnClickListener(v -> FindDetailActivity.startActivity(FindExamineActivity.this, find));
            }
        });

        mPresenter.getMySocietyList(mIsShowRight);

        setEmptyFreshListener(0, v -> mPresenter.getMySocietyList(mIsShowRight));

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 我的发现/待审核页
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context, int title, boolean showRight) {
        Intent in = new Intent(context, FindExamineActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title);
        in.putExtra(Constants.EXTRAS.EXTRAS, showRight);
        context.startActivity(in);
    }

    private void showDeleteDialog(int id, int type, int positon) {
        CustomDefaultDialog dialog = new CustomDefaultDialog(getContext());
        dialog.setContent(R.string.delete_tips)
                .setOkText(R.string.delete)
                .setOkTextRed()
                .setOnOKClickListener(v1 -> { mPresenter.deleteSociety(id, type, positon); dialog.dismiss(); })
                .show();
    }

    @Override
    public void freshFind(List<Find> list) {
        if (mData == null) mData = new ArrayList();
        mData.clear();
        if (list != null) mData.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (mData.size() == 0) showEmptyView();
        else hideEmptyView();
    }

    @Override
    public void deleteSociety(int position) {
        mData.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
