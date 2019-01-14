/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.CustomerLogo;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;

import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.base.utils.StatusBarUtils;
import com.mob.tools.gui.MobViewPager;
import com.mob.tools.utils.ResHelper;

/**
 * 九宫格的抽象类
 */
public abstract class PlatformPage extends OnekeySharePage {
    private ClassicTheme impl;
    /**
     * 点击九格宫，展示编辑界面，要执行的子线程
     */
    private Runnable beforeFinish;
    private LinearLayout llPanel;
    private boolean finished;

    public PlatformPage(OnekeyShareThemeImpl impl) {
        super(impl);
        this.impl = ResHelper.forceCast(impl);
    }

    public void onCreate() {
        StatusBarUtils.setStatusBarFullTransparent( activity );
        StatusBarUtils.setStatusBarLight( activity, true );
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(0x4c000000));
        activity.overridePendingTransition(0, 0);

        LinearLayout llPage = new LinearLayout(activity);
        llPage.setOrientation(LinearLayout.VERTICAL);
        activity.setContentView(llPage);

        TextView vTop = new TextView(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        vTop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        llPage.addView(vTop, lp);

        llPanel = new LinearLayout(activity);
        llPanel.setOrientation(LinearLayout.VERTICAL);
        lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llPage.addView(llPanel, lp);
        /* 增加分享到 */
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin16 = ExpoApp.getApplication().getResources().getDimensionPixelSize(R.dimen.dms_32);
        int margin22 = ExpoApp.getApplication().getResources().getDimensionPixelSize(R.dimen.dms_44);
        int margin14 = ExpoApp.getApplication().getResources().getDimensionPixelSize(R.dimen.dms_28);
        int margin10 = ExpoApp.getApplication().getResources().getDimensionPixelSize(R.dimen.dms_20);
        LinearLayout buttonLayout = new LinearLayout(activity);
        buttonLayout.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(margin16, margin22, 0, 0);
        TextView tv = new TextView(activity);
        tv.setText(R.string.share_to);
        tv.setTextSize(15);
        tv.setTextColor(getContext().getResources().getColor(R.color.color_333));
        tv.setBackgroundColor(Color.WHITE);
        buttonLayout.addView(tv,lp2);
        llPanel.addView(buttonLayout,lp);
        /* 增加分享到 */
        MobViewPager mvp = new MobViewPager(activity);
        ArrayList<Object> cells = collectCells();
        PlatformPageAdapter adapter = newAdapter(cells);
        lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, adapter.getPanelHeight());
        llPanel.addView(mvp, lp);

        IndicatorView vInd = new IndicatorView(activity);
        lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, adapter.getBottomHeight());
        llPanel.addView(vInd, lp);

        vInd.setScreenCount(adapter.getCount());
        vInd.onScreenChange(0, 0);
        adapter.setIndicator(vInd);
        mvp.setAdapter(adapter);
        /* 增加取消分享按钮 */
        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,0);
        LinearLayout buttonLayout2 = new LinearLayout(activity);
        buttonLayout2.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams lp22 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp22.setMargins(0, 0, 0, margin14);
        TextView cancelShare = new TextView(activity);
        cancelShare.setPadding(0, 0, 0, margin10);
        cancelShare.setText(R.string.stop_sharing);
        cancelShare.setGravity(Gravity.CENTER);
        cancelShare.setTextSize(15);
        cancelShare.setTextColor(getContext().getResources().getColor(R.color.color_333));
        cancelShare.setBackgroundColor(Color.WHITE);
        cancelShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        buttonLayout2.addView(cancelShare,lp22);
        llPanel.addView(buttonLayout2,lp);
        /* 增加取消分享按钮 */
    }

    protected abstract PlatformPageAdapter newAdapter(ArrayList<Object> cells);

    protected ArrayList<Object> collectCells() {
        ArrayList<Object> cells = new ArrayList<Object>();

        Platform[] platforms = ShareSDK.getPlatformList();
        if (platforms == null) {
            platforms = new Platform[0];
        }
        HashMap<String, String> hides = getHiddenPlatforms();
        if (hides == null) {
            hides = new HashMap<String, String>();
        }
        for (Platform p : platforms) {
            if (!hides.containsKey(p.getName())) {
                if (isCanShare(p)) {
                    cells.add(p);
                }
            }
        }

        ArrayList<CustomerLogo> customers = getCustomerLogos();
        if (customers != null && customers.size() > 0) {
            cells.addAll(customers);
        }

        return cells;
    }

    public final void showEditPage(final Platform platform) {
        beforeFinish = new Runnable() {
            public void run() {
                boolean isSilent = isSilent();
                boolean isCustomPlatform = platform instanceof CustomPlatform;
                boolean isUseClientToShare = isUseClientToShare(platform);
                if (isSilent || isCustomPlatform || isUseClientToShare) {
                    shareSilently(platform);
                } else {
                    ShareParams sp = formateShareData(platform);
                    if (sp != null) {
                        // 编辑分享内容的统计
                        ShareSDK.logDemoEvent(3, platform);
                        sp.setOpenCustomEven(true);
                        if (getCustomizeCallback() != null) {
                            getCustomizeCallback().onShare(platform, sp);
                        }
                        impl.showEditPage(activity, platform, sp);
                    }
                }
            }
        };
        finish();
    }

    public final void performCustomLogoClick(final View v, final CustomerLogo logo) {
        beforeFinish = new Runnable() {
            public void run() {
                logo.listener.onClick(v);
            }
        };
        finish();
    }

    private boolean isCanShare(Platform platform) {
        String name = platform.getName();
        if ("Cmcc".equals(name) || "Accountkit".equals(name) || "Telecom".equals(name)) {
            return false;
        }
        return true;
    }

    public boolean onFinish() {
        if (finished) {
            finished = false;
            return false;
        }

        if (beforeFinish == null) {
            // 取消分享菜单的统计
            ShareSDK.logDemoEvent(2, null);
        } else {
            beforeFinish.run();
            beforeFinish = null;
        }

        finished = true;
        finish();
        activity.overridePendingTransition(0, 0);
        return true;
    }

}
