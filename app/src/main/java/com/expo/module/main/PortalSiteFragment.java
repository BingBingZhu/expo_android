package com.expo.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.PortalSiteContract;
import com.expo.entity.PortalSite;
import com.expo.module.webview.WebActivity;
import com.expo.network.Http;
import com.expo.utils.LanguageUtil;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PortalSiteFragment extends BaseFragment<PortalSiteContract.Presenter> implements PortalSiteContract.View {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.title)
    View mTitleView;
    PortalSiteAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.fragment_portal_site;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTitleView.setPadding(mTitleView.getPaddingLeft(), mTitleView.getPaddingTop() + StatusBarUtils.getStatusBarHeight(getContext()), mTitleView.getPaddingRight(), mTitleView.getPaddingBottom());
        mAdapter = new PortalSiteAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpaceDecoration(0, getResources().getDimensionPixelSize(R.dimen.dms_24)));
        mPresenter.loadPortalSites();
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void setPortalSites(List<PortalSite> list) {
        mAdapter.portalSites.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PortalSite portalSite = (PortalSite) v.getTag();
            String url = portalSite.getLinkUrl();
            if (portalSite.getCaption().indexOf("一带一路") >= 0)
                url = mPresenter.loadCommonUrl() + "?type=1";
            if (portalSite.getCaption().indexOf("植物之最") >= 0)
                url = mPresenter.loadCommonUrl() + "?type=2";
            WebActivity.startActivity(getContext(), url, LanguageUtil.chooseTest(portalSite.getCaption(), portalSite.getCaptionEn()));
        }
    };

    class PortalSiteAdapter extends RecyclerView.Adapter<PortalSiteHolder> {

        List<PortalSite> portalSites = new ArrayList<>();

        @NonNull
        @Override
        public PortalSiteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.layout_portal_site, null);
            view.setOnClickListener(clickListener);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new PortalSiteHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PortalSiteHolder portalSiteHolder, int i) {
            PortalSite portalSite = portalSites.get(i);
            portalSiteHolder.itemView.setTag(portalSite);
            Http.loadImage(((ImageView) portalSiteHolder.itemView), portalSite.getPicUrl());
        }

        @Override
        public int getItemCount() {
            return portalSites == null ? 0 : portalSites.size();
        }
    }

    class PortalSiteHolder extends RecyclerView.ViewHolder {

        public PortalSiteHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
