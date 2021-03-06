package com.expo.module.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.CheckUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ContactsAddContract;
import com.expo.entity.Contacts;
import com.expo.module.mine.adapter.WorkAdapter;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.widget.MyContactsInfoView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 添加联系人
 */
public class ContactsAddActivity extends BaseActivity<ContactsAddContract.Presenter> implements ContactsAddContract.View {

    @BindView(R.id.contacts_add_type)
    MyContactsInfoView mMvType;
    @BindView(R.id.contacts_add_name)
    MyContactsInfoView mMvName;
    @BindView(R.id.contacts_add_id)
    MyContactsInfoView mMvId;

    Contacts mContacts;
    String mIdType;

    WorkAdapter mAdapter;
    List<String> mList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_contacts_add;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, getIntent().getIntExtra(Constants.EXTRAS.EXTRA_TITLE, R.string.title_contact_add));

        initContactsType();
        initContactsName();
        initContactsId();
        initWorkAdapter();

        mContacts = getIntent().getParcelableExtra(Constants.EXTRAS.EXTRAS);
        if (mContacts != null)
            freshContacts();
        else {
            mIdType = "1";
            mAdapter.mPosition = 0;
            ((TextView) mMvType.getRightView()).setText(Constants.ContactsType.CONTACTS_TYPE_MAP.get(mIdType));
        }

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动推荐路线列表页
     *
     * @param activity
     */
    public static void startActivity(@NonNull Activity activity, Contacts contacts, int titleRes) {
        Intent in = new Intent(activity, ContactsAddActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, contacts);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, titleRes);
        activity.startActivityForResult(in, Constants.RequestCode.REQUEST111);
    }

    public void initContactsType() {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(0);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrow_down, 0);
        textView.setPadding(0, 0, 0, 0);
        mMvType.addRightView(textView);
    }

    public void initContactsName() {
        EditText editText = new EditText(this);
        editText.setPadding(0, 0, 0, 0);
        editText.setBackgroundResource(0);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_28));
        editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        editText.setHint(R.string.item_contacts_name_hint);
        mMvName.addRightView(editText);
    }

    public void initContactsId() {
        EditText editText = new EditText(this);
        editText.setPadding(0, 0, 0, 0);
        editText.setBackgroundResource(0);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_28));
        editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        editText.setHint(R.string.item_contacts_identify_id_hint);
        mMvId.addRightView(editText);
    }

    public void freshContacts() {
        mIdType = mContacts.type;
        ((TextView) mMvType.getRightView()).setText(Constants.ContactsType.CONTACTS_TYPE_MAP.get(mContacts.type));
        ((EditText) mMvName.getRightView()).setText(mContacts.name);
        ((EditText) mMvId.getRightView()).setText(mContacts.ids + "");
    }

    public Contacts initContacts() {
        if (mContacts == null) mContacts = new Contacts();
        mContacts.type = mIdType;
        mContacts.name = ((EditText) mMvName.getRightView()).getText().toString();
        mContacts.ids = ((EditText) mMvId.getRightView()).getText().toString();
        mContacts.uid = ExpoApp.getApplication().getUser().getUid();
        return mContacts;
    }

    @OnClick(R.id.contacts_add_ok)
    public void clickOk(View view) {
        if (CheckUtils.isEmtpy(mIdType, R.string.check_string_id_type, true)) return;
        if (CheckUtils.isEmtpy(((EditText) mMvName.getRightView()).getText().toString(), R.string.check_string_empty_name, true))
            return;
        if (!CheckUtils.isCorrectPapersFormat(((EditText) mMvId.getRightView()).getText().toString(), mIdType)){
            ToastHelper.showShort(R.string.please_enter_the_correct_identification_number);
            return;
        }
//        if (StringUtils.equals("1", mIdType)) {
//            if (!CheckUtils.isIDCard(((EditText) mMvId.getRightView()).getText().toString(), true))
//                return;
//        } else if (StringUtils.equals("2", mIdType)) {
//            if (!CheckUtils.isPassport(((EditText) mMvId.getRightView()).getText().toString(), true))
//                return;
//        } else if (StringUtils.equals("3", mIdType)) {
//        } else if (StringUtils.equals("4", mIdType)) {
//            if (!CheckUtils.isTWCard(((EditText) mMvId.getRightView()).getText().toString(), true))
//                return;
//        }
        mPresenter.updateContactsData(initContacts());
    }

    @Override
    public void saveContacts() {
        setResult(RESULT_OK);
        finish();
    }

    public void initWorkAdapter() {
        mAdapter = new WorkAdapter(this);
        mAdapter.setSource(Constants.ContactsType.CONTACTS_TYPE_MAP);
        mList.add("1");
        mList.add("3");
        mList.add("4");
        mList.add("2");
        mAdapter.setData(mList);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.contacts_add_type)
    public void selectIdType(View view) {
        CommUtils.hideKeyBoard(this, view);
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ListHolder())
                .setAdapter(mAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        mIdType = mList.get(position);
                        mAdapter.mPosition = position;
                        ((TextView) mMvType.getRightView()).setText(Constants.ContactsType.CONTACTS_TYPE_MAP.get(mIdType));
                        dialog.dismiss();
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
//                .setContentHeight(SizeUtils.dp2px(300))
                .create();

        dialog.show();
    }

}
