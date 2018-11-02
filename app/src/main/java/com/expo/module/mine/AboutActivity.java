package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseActivity;

import butterknife.BindView;

/*
 * 关于我们
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.app_version_name)
    TextView mTvVersionName;
    @BindView(R.id.app_info)
    TextView mTvAppInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.item_mine_about);

        mTvVersionName.setText(AppUtils.getAppVersionName());
        mTvAppInfo.setText("\u3000\u3000" + "2019年中国北京世界园艺博览会是经国际园艺生产者协会批准、国际展览局认可，由中国政府主办、北京市承办的最高级别的世界园艺博览会，其主题为“绿色生活美丽家园”，将成为中国加强生态文明建设、构筑绿色产业体系、推动绿色优势向经济优势转变、体现人与自然和谐共生的集中展示。本届世园会将于2019年4月至10月在北京市延庆区举行，规划总面积960公顷。园区选址距离八达岭长城10公里，被称为“长城脚下的世园会”。\n\n\u3000\u30002019年中国北京世界园艺博览会是经国际园艺生产者协会批准、国际展览局认可，由中国政府主办、北京市承办的最高级别的世界园艺博览会，其主题为“绿色生活美丽家园”，将成为中国加强生态文明建设、构筑绿色产业体系、推动绿色优势向经济优势转变、体现人与自然和谐共生的集中展示。本届世园会将于2019年4月至10月在北京市延庆区举行，规划总面积960公顷。园区选址距离八达岭长城10公里，被称为“长城脚下的世园会”。");
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, AboutActivity.class);
        context.startActivity(in);
    }
}
