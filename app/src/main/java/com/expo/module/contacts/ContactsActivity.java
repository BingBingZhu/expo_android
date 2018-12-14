package com.expo.module.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ContactsContract;
import com.expo.entity.Contacts;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.widget.AppBarView;
import com.expo.widget.decorations.SpaceDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ContactsActivity extends BaseActivity<ContactsContract.Presenter> implements ContactsContract.View {

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.contacts_ok)
    TextView mTvOk;
    private TextView mAddBtn;

    List<Contacts> mData;
    CommonAdapter mAdapter;

    boolean isSelect;
    Map<String, Contacts> mMap;

    int mSelectCount = 0;
    int mMaxCount;

    @Override
    protected int getContentView() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_contact_appointment);
        initTitleRightTextView(R.string.add, R.color.white, v -> ContactsAddActivity.startActivity(ContactsActivity.this, null));
        isSelect = getIntent().getBooleanExtra(Constants.EXTRAS.EXTRA_SELECT_CONTACTS, false);
        mMaxCount = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_SELECT_CONTACTS_MAX_COUNT, Integer.MAX_VALUE);
        mMap = new HashMap<>();
        mData = new ArrayList<>();

        if (isSelect) {
            mTvOk.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpaceDecoration spaceDecoration = new SpaceDecoration(0, getResources().getDimensionPixelSize(R.dimen.dms_4));
        mRecyclerView.addItemDecoration(spaceDecoration);
        mRecyclerView.setAdapter(mAdapter = new CommonAdapter(this, R.layout.item_contacts, mData) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                Contacts contacts = mData.get(position);

                holder.setVisible(R.id.item_contacts_check, isSelect);
                holder.setText(R.id.item_contacts_name, contacts.name);
                holder.setText(R.id.item_contacts_number, (contacts.ids + "").replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

                if (isSelect) {
                    holder.setChecked(R.id.item_contacts_check, mMap.containsKey(contacts.ids));
                    ((CheckBox) holder.getView(R.id.item_contacts_check)).setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            if (mSelectCount + 1 > mMaxCount) {
                                buttonView.setChecked(false);
                                ToastHelper.showShort(String.format(getResources().getString(R.string.max_select), mMaxCount));
                            } else {
                                mMap.put(contacts.ids, contacts);
                                Math.min(++mSelectCount, Math.min(mMaxCount, mData.size()));
                            }
                        } else {
                            mMap.remove(contacts.ids);
                            Math.min(--mSelectCount, Math.min(mMaxCount, mData.size()));
                        }
                    });
                }

                holder.itemView.setOnClickListener(v -> {
                    ContactsAddActivity.startActivity(ContactsActivity.this, contacts);
                });
            }
        });
        mPresenter.getContactsData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Activity activity, boolean isSelect, int maxCount) {
        Intent in = new Intent(activity, ContactsActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_SELECT_CONTACTS, isSelect);
        in.putExtra(Constants.EXTRAS.EXTRA_SELECT_CONTACTS_MAX_COUNT, maxCount);
        activity.startActivityForResult(in, Constants.RequestCode.REQ_TO_CONTACTS);
    }

    @Override
    public void freshContacts(List list) {
        mData.clear();
        if (list != null) {
            mData.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
        getConfirmText(mSelectCount, Math.min(mMaxCount, mData.size()));
    }

    @OnClick(R.id.contacts_ok)
    public void clickOK(View view) {
        List<Contacts> list = new ArrayList<>();
        for (Map.Entry<String, Contacts> entry : mMap.entrySet()) {
            list.add(entry.getValue());
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRAS.EXTRAS, Http.getGsonInstance().toJson(list));
        setResult(RESULT_OK, intent);
        finish();
    }

//    public void initTitleRightTextView(String s, int white, View.OnClickListener clickListener) {
//        if (null == mAddBtn) {
//            mAddBtn = new TextView(this);
//            ((AppBarView) getTitleView()).setRightView(mAddBtn);
//        }
//        mAddBtn.setTextAppearance(this, R.style.TextSizeWhite14);
//        mAddBtn.setText(R.string.add);
//        mAddBtn.setGravity(Gravity.CENTER);
//        mAddBtn.setOnClickListener(v -> {
//            ContactsAddActivity.startActivity(ContactsActivity.this, null);
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RequestCode.REQUEST111) {
                mPresenter.getContactsData();
            }
        }
    }

    private void getConfirmText(int first, int second) {
        mTvOk.setText(getResources().getString(R.string.confirm) + " (" + first + "/" + second + ")");
    }
}
