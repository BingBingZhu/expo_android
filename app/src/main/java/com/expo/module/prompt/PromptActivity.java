package com.expo.module.prompt;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.utils.Constants;

import butterknife.BindView;

public class PromptActivity extends BaseActivity {

    @BindView(R.id.prompt_list)
    ListView listView;

    private PromptAdapter mAdapter;
    private Ringtone mRingtone;

    @Override
    protected int getContentView() {
        return R.layout.activity_prompt;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, "选择提示音");
        mAdapter = new PromptAdapter(getContext());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PrefsHelper.setInt(Constants.Prefs.KEY_RAW_SELECTOR_POSITION, position);
            playNotificationSound(Constants.RawResource.resourceIds[position]);
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, PromptActivity.class);
        context.startActivity(intent);
    }

    private void playNotificationSound(int resouId){
        Uri uri;
        if (null != mRingtone){
            mRingtone.stop();
        }
        if (resouId == 0) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }else {
            uri = Uri.parse("android.resource://" + ExpoApp.getApplication().getPackageName() + "/" + resouId);
        }
        mRingtone = RingtoneManager.getRingtone(ExpoApp.getApplication().getApplicationContext(), uri);
        mRingtone.play();
    }
}
