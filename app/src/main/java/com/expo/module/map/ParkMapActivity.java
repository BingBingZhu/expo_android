package com.expo.module.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.ParkMapContract;
import com.expo.utils.Constants;

/*
 * 世园会地图导览页
 */
public class ParkMapActivity extends BaseActivity<ParkMapContract.Presenter> implements ParkMapContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_park_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动ParkMapActivity
     *
     * @param context
     * @param selectedId 导航标签被选中类型的id
     * @param spotId     要查看的景点的id
     */
    public static void startActivity(@NonNull Context context, @Nullable Long selectedId, @Nullable Long spotId) {
        Intent in = new Intent( context, ParkMapActivity.class );
        if (selectedId != null) {
            in.putExtra( Constants.EXTRAS.EXTRA_TAB_ID, selectedId );
        }
        if (spotId != null) {
            in.putExtra( Constants.EXTRAS.EXTRA_SPOT_ID, selectedId );
        }
        context.startActivity( in );
    }
}
