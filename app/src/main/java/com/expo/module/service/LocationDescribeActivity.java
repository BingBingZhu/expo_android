package com.expo.module.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.utils.Constants;
import com.expo.widget.MyRadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/*
 * 位置更改描述页
 */
public class LocationDescribeActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    @BindView(R.id.location_layout)
    RadioGroup mLayout;
    @BindView(R.id.location_describe_edit)
    EditText mEdit;
    @BindView(R.id.seek_help_scan_num)
    TextView mTvScanNum;

    View mSelectView;
    String mPoiId = "";
    boolean isHavePoiId = false;
    private SpeakInputHintDialog mInputDialog;

    @Override

    protected int getContentView() {
        return R.layout.activity_loaction_describe;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.title_location_describe_ac);
        initScanNum();
        mEdit.setText(getIntent().getStringExtra(Constants.EXTRAS.EXTRAS));
        mPoiId = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_ID);
        searchPoi(getIntent().getDoubleExtra(Constants.EXTRAS.EXTRA_LATITUDE, 0),
                getIntent().getDoubleExtra(Constants.EXTRAS.EXTRA_LONGITUDE, 0));
        initRecord();
    }

    @OnClick(R.id.seek_help_speech)
    public void record(View v) {
        if (mInputDialog == null) {
            mInputDialog = new SpeakInputHintDialog( getContext() );
            mInputDialog.setOnCloseListener( (view) -> stopRecord() );
        }
        if (!mInputDialog.isShowing()) {
            mInputDialog.show();
            startRecord();
        }
    }

    private void initScanNum() {
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvScanNum.setText(s.length()+"/300");
            }
        });
    }

    private void searchPoi(double lat, double lon) {
        if (lat == 0 || lon == 0) return;

        PoiSearch.Query query = new PoiSearch.Query("", "", "010");
        query.setPageSize(3);
        query.setCityLimit(true);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 100));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    /**
     * 启动位置描述页面
     *
     * @param activity
     * @param lat      当前定位位置的纬度
     * @param lng      当前定位位置的经度
     * @return RequestCode 请求码
     */
    public static void startActivityForResult(@NonNull Activity activity, double lat, double lng, String poiId, String coordinateAssist) {
        Intent in = new Intent(activity, LocationDescribeActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_LONGITUDE, lng);
        in.putExtra(Constants.EXTRAS.EXTRA_LATITUDE, lat);
        in.putExtra(Constants.EXTRAS.EXTRA_ID, poiId);
        in.putExtra(Constants.EXTRAS.EXTRAS, coordinateAssist);
        activity.startActivityForResult(in, Constants.RequestCode.REQ_GET_LOCAL);
    }

    /*
     * 设置返回数据结果
     */
    private void setResult(Double lat, Double lng, String locatName, String content) {
        if (lat == null || lng == null) {
            setResult(RESULT_CANCELED);
        } else {
            Intent result = new Intent();
            result.putExtra(Constants.EXTRAS.EXTRA_LATITUDE, lat);
            result.putExtra(Constants.EXTRAS.EXTRA_LONGITUDE, lng);
            result.putExtra(Constants.EXTRAS.EXTRAS, content);
            result.putExtra(Constants.EXTRAS.EXTRA_ID, mPoiId);
            result.putExtra(Constants.EXTRAS.EXTRA_SELECTED_POI_NAME, locatName);
            setResult(RESULT_OK, result);
        }
    }

    @OnClick(R.id.location_describe_save)
    public void clickSave(View view) {
        LatLonPoint point = (LatLonPoint) mSelectView.getTag();
        setResult(point.getLatitude(), point.getLongitude(), ((MyRadioButton) mSelectView).getText().toString(), mEdit.getText().toString());
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        LatLonPoint point = (LatLonPoint) mSelectView.getTag();
//        setResult(point.getLatitude(), point.getLongitude(), mEdit.getText().toString());
//        super.onBackPressed();
//    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        View headView = initItemView();
        mLayout.addView(headView);
        headView.performClick();
        headView.setOnClickListener(v -> mPoiId = "");
        for (PoiItem item : poiResult.getPois()) {
            MyRadioButton view = (MyRadioButton) initItemView(item);
            mLayout.addView(view);
            if (StringUtils.equals(mPoiId, item.getPoiId())) {
                view.setChecked(true);
                isHavePoiId = true;
            }
            view.setOnClickListener(v -> mPoiId = item.getPoiId());
        }
        if (!isHavePoiId)
            mPoiId = "";
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    // 动态添加
    private View initItemView() {
        View v = initItemView(getResources().getString(R.string.my_location), new LatLonPoint(-1, -1));
        mSelectView = v;
        return v;
    }

    private View initItemView(PoiItem item) {
        return initItemView(item.getTitle(), item.getLatLonPoint());
    }

    private View initItemView(String text, LatLonPoint point) {
        MyRadioButton textViewDrawable = new MyRadioButton(this);
        textViewDrawable.setLayoutParams(new LinearLayoutCompat.LayoutParams(-1, (int) getResources().getDimension(R.dimen.dms_110)));
        textViewDrawable.setText(text);
        textViewDrawable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) mSelectView = buttonView;
        });
        textViewDrawable.setTextAppearance(this, R.style.TextSize16);
        textViewDrawable.setTag(point);
        textViewDrawable.setGravity(Gravity.CENTER_VERTICAL);
        textViewDrawable.mDrawableSize = (int) getResources().getDimension(R.dimen.dms_30);
        textViewDrawable.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.dms_30));
        textViewDrawable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selector_radio_location, 0, 0, 0);
        return textViewDrawable;
    }
    private void initRecord() {
        asr = EventManagerFactory.create( this, "asr" );
        asr.registerListener( mListener ); //  EventListener 中 onEvent方法\
    }


    //语音识别
    private void startRecord() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        params.put( SpeechConstant.DECODER, 2 );
        params.put( SpeechConstant.VAD, SpeechConstant.VAD_DNN );
        params.put( SpeechConstant.VAD_ENDPOINT_TIMEOUT, 15000 );
        params.put( SpeechConstant.ACCEPT_AUDIO_VOLUME, false );
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        String json = null; // 可以替换成自己的json
        json = new JSONObject( params ).toString(); // 这里可以替换成你需要测试的json
        asr.send( event, json, null, 0, 0 );
    }

    private EventManager asr;

    private void stopRecord() {
        asr.send( SpeechConstant.ASR_STOP, null, null, 0, 0 ); //
    }

    EventListener mListener = new EventListener() {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            if (name.equals( SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL )) {
//                if (params.contains("\"nlu_result\"")) {
                if (params.contains( "\"final_result\"" )) {
                    try {
                        JSONObject gson = new JSONObject( params );
                        mEdit.setText( mEdit.getText().toString() + gson.optString( "best_result" ) );
//                        mEtEdit.setText(mEtEdit.getText().toString() + new String(data, offset, length));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}
