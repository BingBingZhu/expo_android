package com.expo.module.heart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.BaseAdapterItemClickListener;
import com.expo.contract.MessagesContract;
import com.expo.db.QueryParams;
import com.expo.module.heart.message.MessageInterface;
import com.expo.utils.Constants;

import butterknife.BindView;

/*
 * 各消息类型的消息列表，用同一个页面使用不同的列表项布局
 */
public class MessagesActivity extends BaseActivity<MessagesContract.Presenter> implements MessagesContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRvRecycler;

    MessageInterface mAdaptor;

    @Override
    protected int getContentView() {
        return R.layout.activity_messages;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mPresenter.setAdaptor(0);
        mAdaptor.setData(mPresenter.getData(0, new QueryParams()));
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

    @Override
    public void setAdaptor(MessageInterface mi) {
        mAdaptor = mi;

        setTitle(0, mAdaptor.getTitle());
        mAdaptor.setAdapter(this, mRvRecycler);
    }
}
