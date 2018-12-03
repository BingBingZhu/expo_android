package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.UriUtils;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseEventMessage;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.UserInfoContract;
import com.expo.entity.User;
import com.expo.module.mine.adapter.WorkAdapter;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.PickerViewUtils;
import com.expo.widget.AppBarView;
import com.expo.widget.MyUserInfoView;
import com.facebook.common.util.UriUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;
import org.raphets.roundimageview.RoundImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 用户信息页
 */
public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter> implements UserInfoContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.user_img)
    MyUserInfoView mUserImg;
    @BindView(R.id.user_name)
    MyUserInfoView mUserName;
    @BindView(R.id.user_sex)
    MyUserInfoView mUserSex;
    @BindView(R.id.user_age)
    MyUserInfoView mUserAge;
    @BindView(R.id.user_email)
    MyUserInfoView mUserEmail;
    @BindView(R.id.user_work)
    MyUserInfoView mUserWork;

    RadioButton mRadioMale;
    RadioButton mRadioFemale;

    ArrayList<String> mImageList;
    private User mUser;
    private User mOldUser;
    boolean mChangeImg = false;
    private boolean isUpdateInfo;
    private TextView mSaveBtn;

    WorkAdapter mWorkAdapter;
    List<String> mWorkList = new ArrayList<String>();

    OnTimeSelectListener mListener = new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, View v) {
            mPresenter.setAge(TimeUtils.date2String(date, new SimpleDateFormat(Constants.TimeFormat.TYPE_SIMPLE)));
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_user_info_ac);

        initTitleRightTextView(isUpdateInfo);
        initCircleView();
        initUserNameView();
        initUserAgeView();
        initUserSexView();
        initUserEmailView();
        initUserWorkView();

        initWorkAdapter();

        mPresenter.loadUser();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动用户信息页
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, UserInfoActivity.class);
        context.startActivity(in);
    }

    public void initTitleRightTextView(boolean isShow) {
        if (null == mSaveBtn) {
            mSaveBtn = new TextView(this);
            ((AppBarView) getTitleView()).setRightView(mSaveBtn);
        }
        mSaveBtn.setTextAppearance(this, R.style.TextSizeWhite14);
        mSaveBtn.setText(R.string.save);
        mSaveBtn.setGravity(Gravity.CENTER);
        mSaveBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mSaveBtn.setOnClickListener(v -> {
            mUser.setNick(((TextView) mUserName.getRightView()).getText().toString());
            mPresenter.saveUserInfo(mChangeImg, mUser);
        });
    }

    public void initCircleView() {
        RoundImageView imageView = new RoundImageView(this);
        imageView.setType(RoundImageView.TYPE_CIRCLE);
        imageView.setImageResource(R.mipmap.ico_mine_def_photo);
        int width = (int) getResources().getDimension(R.dimen.dms_100);
        mUserImg.addRightView(imageView, width, width);
    }

    public void initUserNameView() {
        EditText editText = new EditText(this);
        editText.setBackgroundResource(0);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_28));
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setNick(s.toString().trim());
                showSaveBtn(mUser);
            }
        });
        mUserName.addRightView(editText);
    }

    public void initUserSexView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_user_info_sex_view, null);
        mRadioMale = view.findViewById(R.id.radio_male);
        mRadioFemale = view.findViewById(R.id.radio_female);
        mRadioMale.setOnCheckedChangeListener(this);
        mRadioFemale.setOnCheckedChangeListener(this);
        mUserSex.addRightView(view);
    }

    public void initUserAgeView() {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(0);
        textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mUserAge.addRightView(textView);
    }

    public void initUserEmailView() {
        EditText editText = new EditText(this);
        editText.setBackgroundResource(0);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_28));
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mUser.setNick(s.toString().trim());
                showSaveBtn(mUser);
            }
        });
        mUserEmail.addRightView(editText);
    }

    public void initUserWorkView() {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(0);
        textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mUserWork.addRightView(textView);
    }

    public void initWorkAdapter() {
        mWorkAdapter = new WorkAdapter(this);
        mWorkList.add("IT/互联网");
        mWorkList.add("销售");
        mWorkList.add("专员");
        mWorkList.add("电商");
        mWorkList.add("教育");
        mWorkList.add("医生");
        mWorkList.add("教师");
        mWorkList.add("公务员");
        mWorkList.add("IT/互联网");
        mWorkList.add("销售");
        mWorkList.add("专员");
        mWorkList.add("电商");
        mWorkList.add("教育");
        mWorkList.add("医生");
        mWorkList.add("教师");
        mWorkList.add("公务员");
        mWorkList.add("IT/互联网");
        mWorkList.add("销售");
        mWorkList.add("专员");
        mWorkList.add("电商");
        mWorkList.add("教育");
        mWorkList.add("医生");
        mWorkList.add("教师");
        mWorkList.add("公务员");
        mWorkAdapter.setData(mWorkList);
        mWorkAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.user_img)
    public void clickImg(View view) {
        ImageSelector.builder()
                .useCamera(true) // 设置是否使用拍照
                .setSingle(true)  //设置是否单选
                .setSelected(mImageList) // 把已选的图片传入默认选中。
                .setViewImage(true) //是否点击放大图片查看,，默认为true
                .start(this, Constants.RequestCode.REQUEST111); // 打开相册
    }

    @OnClick(R.id.user_age)
    public void clickAge(View view) {
        CommUtils.hideKeyBoard(this, view);
        PickerViewUtils.showTimePickView(this, mUser.getBirthDay(), mListener).show();
    }

    @OnClick(R.id.user_work)
    public void changeWork(View view) {
        CommUtils.hideKeyBoard(this, view);
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(new ListHolder())
                .setAdapter(mWorkAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//                        ((TextView) mUserWork.getRightView()).setText();
                        dialog.dismiss();
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setContentHeight(SizeUtils.dp2px(300))
                .create();

        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) return;
        switch (buttonView.getId()) {
            case R.id.radio_male:
                mUser.setSex("0");
                showSaveBtn(mUser);
                break;
            case R.id.radio_female:
                mUser.setSex("1");
                showSaveBtn(mUser);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (mImageList == null) mImageList = new ArrayList<>();
            if (requestCode == Constants.RequestCode.REQUEST111 && data != null) {
                UCrop.Options options = new UCrop.Options();
                //设置裁剪图片可操作的手势
                options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
                //设置隐藏底部容器，默认显示
                options.setHideBottomControls(true);
                //设置toolbar颜色
                options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorAccent));
                //设置状态栏颜色
                options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorAccent));
                options.setCircleDimmedLayer(true);
                options.setShowCropFrame(false);
                options.setCropGridColumnCount(0);
                options.setCropGridRowCount(0);
                UCrop.of(UriUtil.getUriForFile(new File(data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT).get(0))), UriUtil.getUriForFile(new File(data.getStringArrayListExtra(
                        ImageSelectorUtils.SELECT_RESULT).get(0))))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(400, 400)
                        .withOptions(options)
                        .start(this);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri resultUri = UCrop.getOutput(data);
                mChangeImg = true;
                mImageList.clear();
                mImageList.add(UriUtils.uri2File(resultUri, "").getPath());
                mUser.setPhotoUrl(mImageList.get(0));
                Picasso.with(this).load("file://" + mImageList.get(0)).placeholder(R.mipmap.ico_mine_def_photo)
                        .into((RoundImageView) mUserImg.getRightView());
                showSaveBtn(mUser);
            }
        }
    }

    @Override
    public void refreshUserInfo(User user) {
        mUser = user;
        mOldUser = user.clone();
        if (!StringUtils.isEmpty(mUser.getPhotoUrl()))
            Picasso.with(this).load(CommUtils.getFullUrl(mUser.getPhotoUrl())).placeholder(R.drawable.image_default).error(R.drawable.image_default).into((RoundImageView) mUserImg.getRightView());
        ((TextView) mUserName.getRightView()).setText(mUser.getNick());
        if (StringUtils.equals("0", mUser.getSex())) {
            mRadioMale.setChecked(true);
        } else if (StringUtils.equals("1", mUser.getSex())) {
            mRadioFemale.setChecked(true);
        }
        mPresenter.setAge(mUser.getBirthDay());
    }

    @Override
    public void changeAge(String birthDay, String age) {
        ((TextView) mUserAge.getRightView()).setText(age);
        mUser.setBirthDay(birthDay);
        showSaveBtn(mUser);
    }

    private void showSaveBtn(User user) {
        if (isUpdateInfo || null == mOldUser) {
            return;
        }
        isUpdateInfo = !mOldUser.equals(user);
        initTitleRightTextView(isUpdateInfo);
    }

    @Override
    public void saveUserInfo() {
        ToastHelper.showShort(R.string.save_user_info_success);
        EventBus.getDefault().post(new BaseEventMessage(Constants.EventBusMessageId.EVENTBUS_ID_FRESH_USER, null));
        finish();
    }
}
