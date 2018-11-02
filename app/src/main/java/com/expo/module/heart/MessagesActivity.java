package com.expo.module.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.contract.MessagesContract;
import com.expo.db.QueryParams;
import com.expo.module.heart.adapter.MessageAdapter;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;

import butterknife.BindView;

/*
 * 各消息类型的消息列表，用同一个页面使用不同的列表项布局
 */
public class MessagesActivity extends BaseActivity<MessagesContract.Presenter> implements MessagesContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRvRecycler;

    MessageAdapter mAdapter;

    BaseAdapterItemClickListener mListener = new BaseAdapterItemClickListener() {
        @Override
        public void itemClick(View view, int position, Object o) {

        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_messages;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, getIntent().getIntExtra(Constants.EXTRAS.EXTRA_TITLE, 0));
        int layoutId = getIntent().getIntExtra(Constants.EXTRAS.EXTRAS, 0);

        mRvRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this);
        if (layoutId == R.layout.item_message_appointment) {
            mRvRecycler.setBackgroundResource(R.color.colorAccent);
        }
        mRvRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_20)));
        mAdapter.setLayoutId(layoutId);
        mAdapter.setListener(mListener);
        mRvRecycler.setAdapter(mAdapter);
        mAdapter.setData(mPresenter.getData(0, new QueryParams()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动消息列表页
     *
     * @param context
     * @param type    消息类型@see{Message} type
     */
    public static void startActivity(@NonNull Context context, @NonNull String type) {
        Intent in = new Intent(context, MessagesActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, type);
        context.startActivity(in);
    }

}
