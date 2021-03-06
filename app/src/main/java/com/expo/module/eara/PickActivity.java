//package com.expo.module.eara;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.expo.R;
//import com.expo.base.BaseActivity;
//import com.expo.contract.LoginContract;
//import com.expo.widget.AppBarView;
//import com.sahooz.library.Country;
//import com.sahooz.library.LetterHolder;
//import com.sahooz.library.PyAdapter;
//import com.sahooz.library.PyEntity;
//import com.sahooz.library.SideBar;
//import com.sahooz.library.VH;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PickActivity extends BaseActivity<LoginContract.Presenter> {
//
//    private ArrayList<Country> selectedCountries = new ArrayList<>();
//    private ArrayList<Country> allCountries = new ArrayList<>();
//
//    @Override
//    protected int getContentView() {
//        return R.layout.activity_pick;
//    }
//
//    @Override
//    protected void onInitView(Bundle savedInstanceState) {
//        AppBarView mTitleView = (AppBarView) findViewById(R.id.login_back);
//        RecyclerView rvPick = (RecyclerView) findViewById(R.id.rv_pick);
//        SideBar side = (SideBar) findViewById(R.id.side);
//        EditText etSearch = (EditText) findViewById(R.id.et_search);
//        TextView tvLetter = (TextView) findViewById(R.id.tv_letter);
//
//        setDoubleTapToExit(false);
//
//        allCountries.clear();
//        allCountries.addAll(Country.getAll(this, null));
//        selectedCountries.clear();
//        selectedCountries.addAll(allCountries);
//        final CAdapter adapter = new CAdapter(selectedCountries);
//        rvPick.setAdapter(adapter);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        rvPick.setLayoutManager(manager);
//        rvPick.setAdapter(adapter);
////        rvPick.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String string = s.toString();
//                selectedCountries.clear();
//                for (Country country : allCountries) {
//                    if (country.bg_vr_card.toLowerCase().contains(string.toLowerCase()))
//                        selectedCountries.add(country);
//                }
//                adapter.update(selectedCountries);
//            }
//        });
//
//        side.addIndex("#", side.indexes.size());
//        side.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
//            @Override
//            public void onLetterChange(String letter) {
//                tvLetter.setVisibility(View.VISIBLE);
//                tvLetter.setText(letter);
//                int position = adapter.getLetterPosition(letter);
//                if (position != -1) {
//                    manager.scrollToPositionWithOffset(position, 0);
//                }
//            }
//
//            @Override
//            public void onReset() {
//                tvLetter.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    @Override
//    protected boolean hasPresenter() {
//        return false;
//    }
//
//    class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {
//
//        public CAdapter(List<? extends PyEntity> entities) {
//            super(entities);
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
//            return new LetterHolder(getLayoutInflater().inflate(R.layout.item_letter, parent, false));
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
//            return new VH(getLayoutInflater().inflate(R.layout.item_country_large_padding, parent, false));
//        }
//
//        @Override
//        public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
//            VH vh = (VH) holder;
//            final Country country = (Country) entity;
//            vh.ivFlag.setImageResource(country.flag);
//            vh.tvName.setText(country.bg_vr_card);
//            vh.tvCode.setText("+" + country.code);
//            holder.itemView.setOnClickListener(v -> {
//                Intent data = new Intent();
//                data.putExtra("country", country.toJson());
//                setResult(Activity.RESULT_OK, data);
//                finish();
//            });
//        }
//
//        @Override
//        public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
//            ((LetterHolder) holder).textView.setText(entity.letter.toUpperCase());
//        }
//    }
//}
