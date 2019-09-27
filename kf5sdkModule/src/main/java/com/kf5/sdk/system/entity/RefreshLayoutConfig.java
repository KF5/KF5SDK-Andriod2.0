package com.kf5.sdk.system.entity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.kf5.sdk.system.manager.RefreshLayoutManager;
import com.kf5.sdk.system.utils.Utils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @author Chosen
 * @create 2019/4/8 10:47
 * @email 812219713@qq.com
 */
public final class RefreshLayoutConfig {

    private Activity activity;
    private ListView listView;
    private RefreshLayout refreshLayout;

    private RefreshLayoutConfig() {

    }

    public static RefreshLayoutConfig start() {
        return new RefreshLayoutConfig();
    }

    public RefreshLayoutConfig with(Activity activity) {
        this.activity = activity;
        return this;
    }

    public RefreshLayoutConfig withListView(ListView listView) {
        this.listView = listView;
        return this;
    }

    public RefreshLayoutConfig listViewItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        listView.setOnItemClickListener(onItemClickListener);
        return this;
    }

    public RefreshLayoutConfig listViewItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        listView.setOnItemLongClickListener(onItemLongClickListener);
        return this;
    }

    public RefreshLayoutConfig listViewItemScrollListener(AbsListView.OnScrollListener scrollListener) {
        listView.setOnScrollListener(scrollListener);
        return this;
    }

    public RefreshLayoutConfig listViewDivider(Drawable drawable) {
        listView.setDivider(drawable);
        return this;
    }

    public RefreshLayoutConfig listViewDividerHeight(int dpHeight) {
        listView.setDividerHeight(Utils.dip2px(activity, dpHeight));
        return this;
    }

    public RefreshLayoutConfig listViewWithHeaderView(View headerView) {
        listView.addHeaderView(headerView);
        return this;
    }

    public RefreshLayoutConfig listViewHeaderDividersEnabled(boolean enabled) {
        listView.setHeaderDividersEnabled(enabled);
        return this;
    }

    public void commitWithSetAdapter(BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public RefreshLayoutConfig withRefreshLayout(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        return this;
    }

    public RefreshLayoutConfig refreshLayoutEnableRefreshAndLoadMore(boolean refreshAble, boolean loadMoreAble) {
        RefreshLayoutManager.configRefreshLayoutWithRefreshAndLoadMore(activity, refreshLayout, refreshAble, loadMoreAble);
        return this;
    }

    public RefreshLayoutConfig refreshLayoutAutoLoadMore(boolean autoLoadMore) {
        refreshLayout.setEnableAutoLoadMore(autoLoadMore);
        return this;
    }

    public RefreshLayoutConfig refreshLayoutOnRefreshListener(OnRefreshListener refreshListener) {
        refreshLayout.setOnRefreshListener(refreshListener);
        return this;
    }

    public RefreshLayoutConfig refreshLayoutOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        refreshLayout.setOnLoadMoreListener(loadMoreListener);
        return this;
    }

    public RefreshLayoutConfig refreshLayoutEmptyLayoutResource(Bitmap bitmap, String noDataString) {
        RefreshLayoutManager.configEmptyLayoutResource(activity, bitmap, noDataString);
        return this;
    }


}
