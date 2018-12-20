package com.expo.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.NationalSmsCodeContract;
import com.expo.entity.NationalSmsCode;
import com.expo.utils.Constants;
import com.sahooz.library.Country;
import com.sahooz.library.LetterHolder;
import com.sahooz.library.PyAdapter;
import com.sahooz.library.PyEntity;
import com.sahooz.library.SideBar;
import com.sahooz.library.TitleItemDecoration;
import com.sahooz.library.VH;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * 国家短信代号选择
 */
public class NationalSmsCodeActivity extends BaseActivity<NationalSmsCodeContract.Presenter> implements NationalSmsCodeContract.View {

    @BindView(R.id.rv_pick)
    RecyclerView mRvPick;
    @BindView(R.id.side)
    SideBar mSide;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_letter)
    TextView mTvLetter;

//    private NationalSmsCode mNationalSmsCode;
    private ArrayList<Country> mSelectedCountries = new ArrayList<>();
    private ArrayList<Country> mAllCountries = new ArrayList<>();
    private NationalSmsCodeActivity.CAdapter adapter;


    @Override
    protected int getContentView() {
        return R.layout.activity_national_sms_code;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
//        mNationalSmsCode = getIntent().getParcelableExtra(Constants.EXTRAS.EXTRAS);
        setTitle(0, R.string.title_pick_ac);
        setDoubleTapToExit(false);

        mAllCountries.clear();
        mAllCountries.addAll(Country.getAll(this, null));
        mSelectedCountries.clear();
        mSelectedCountries.addAll(mAllCountries);
        adapter = new CAdapter(mSelectedCountries);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvPick.setLayoutManager(manager);
        mRvPick.setAdapter(adapter);
        TitleItemDecoration decoration = new TitleItemDecoration(this, adapter.getEntityList());
        mRvPick.addItemDecoration(decoration);
        mRvPick.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString().trim();
                search(string);
            }
        });

//        mSide.addIndex("#", mSide.indexes.size());
        mSide.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                mTvLetter.setVisibility(View.VISIBLE);
                mTvLetter.setText(letter);
                int position = adapter.getLetterPosition(letter);
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onReset() {
                mTvLetter.setVisibility(View.GONE);
            }
        });
    }

    private void search(String string) {
        mSelectedCountries.clear();
        for (Country country : mAllCountries) {
            if (country.name.toLowerCase().contains(string.toLowerCase()))
                mSelectedCountries.add(country);
        }
        adapter.update(mSelectedCountries);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动选择短信前缀国家代号
     *
     * @param activity
     * @param code     当前选择的国家代号
     * @return
     */
    public static int startActvityForResult(@NonNull Activity activity, @NonNull NationalSmsCode code) {
        Intent in = new Intent(activity, NationalSmsCodeActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRAS, code);
        int requestCode = 100;
        activity.startActivityForResult(in, requestCode);
        return requestCode;
    }

    /*
     * 设置返回的结果
     */
//    private void setResult(@Nullable NationalSmsCode code) {
//        if (code != null || !code.equals(mNationalSmsCode)) {
//            Intent extra = new Intent();
//            extra.putExtra(Constants.EXTRAS.EXTRAS, code);
//            setResult(RESULT_OK, extra);
//        } else {
//            setResult(RESULT_CANCELED);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        setResult(null);
//    }

    class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {

        public CAdapter(List<? extends PyEntity> entities) {
            super(entities);
        }

        @Override
        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
            return new LetterHolder(getLayoutInflater().inflate(R.layout.item_letter, parent, false));
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.item_country_large_padding, parent, false));
        }

        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
            VH vh = (VH) holder;
            final Country country = (Country) entity;
            vh.ivFlag.setImageResource(country.flag);
            vh.tvName.setText(country.name);
            vh.tvCode.setText("+" + country.code);
            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra("country", country.toJson());
                setResult(Activity.RESULT_OK, data);
                finish();
            });
        }

        @Override
        public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
            ((LetterHolder) holder).textView.setText(entity.letter.toUpperCase());
        }
    }
}
