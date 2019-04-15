package com.kf5.sdk.helpcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.adapter.HelpCenterAdapter;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.presenter.HelpCenterPresenter;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpUseCaseManager;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterView;
import com.kf5.sdk.system.base.BaseMVPActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.RefreshLayoutConfig;
import com.kf5.sdk.system.manager.RefreshLayoutManager;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.utils.ClickUtils;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * @author Chosen
 * @create 2019/4/2 15:01
 * @email 812219713@qq.com
 */
abstract class BaseHelpCenter extends BaseMVPActivity<HelpCenterPresenter, IHelpCenterView> implements
        IHelpCenterView, AdapterView.OnItemClickListener {

    /**
     * 文档类别区分
     */
    protected enum HelpCenterType {
        Category(R.string.kf5_article_category, HelpCenterRequestType.CATEGORY),
        Forum(R.string.kf5_article_section, HelpCenterRequestType.FORUM),
        Post(R.string.kf5_article_list, HelpCenterRequestType.POST);

        private @StringRes
        int titleRes;
        private HelpCenterRequestType requestType;

        HelpCenterType(int titleRes, HelpCenterRequestType requestType) {
            this.titleRes = titleRes;
            this.requestType = requestType;
        }
    }

    private HelpCenterAdapter mHelpCenterAdapter;

    private List<HelpCenterItem> listItem = new ArrayList<>();

    private boolean isSearch = false;

    private int nextPage = 1;

    private RefreshLayout refreshLayout;

    private HelpCenterType helpCenterType;

    private boolean isRefresh = true;

    private int targetId;

    private static final int SEARCH_REQUEST_CODE = 0x11;

    private String searchKey;

    protected abstract HelpCenterType getHelpCenterType();

    @Override
    public void onLoadFinished(Loader<HelpCenterPresenter> loader, HelpCenterPresenter data) {
        super.onLoadFinished(loader, data);
        showDialog = true;
        presenter.getCommonDataList(helpCenterType.requestType);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        helpCenterType = getHelpCenterType();
        targetId = getIntent().getIntExtra(Field.ID, 0);
        ListView mListView = (ListView) findViewById(R.id.kf5_listView);
        View headerView = LayoutInflater.from(this).inflate(R.layout.kf5_helpcenter_header_layout, null);
        headerView.setOnClickListener(this);
        refreshLayout = ((SmartRefreshLayout) findViewById(R.id.kf5_refreshLayout));
        RefreshLayoutConfig.start()
                .with(this)
                .withListView(mListView)
                .listViewItemClickListener(this)
                .listViewDivider(getResources().getDrawable(R.drawable.kf5_divider_inset_left_16))
                .listViewDividerHeight(1)
                .listViewWithHeaderView(headerView)
                .withRefreshLayout(refreshLayout)
                .refreshLayoutEnableRefreshAndLoadMore(true, true)
                .refreshLayoutAutoLoadMore(false)
                .refreshLayoutOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        isRefresh = true;
                        nextPage = 1;
                        isSearch = false;
                        setTitle();
                        presenter.getCommonDataList(helpCenterType.requestType);
                    }
                })
                .refreshLayoutOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        if (isSearch) {
                            presenter.searchDocument(HelpCenterRequestType.SEARCH);
                        } else {
                            presenter.getCommonDataList(helpCenterType.requestType);
                        }
                    }
                })
                .refreshLayoutEmptyLayoutResource(null, getResources().getString(R.string.kf5_no_data))
                .commitWithSetAdapter(mHelpCenterAdapter = new HelpCenterAdapter(mActivity, listItem));
        setTitle();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_layout_refresh_listview;
    }

    @Override
    public Loader<HelpCenterPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<HelpCenterPresenter>() {
            @Override
            public HelpCenterPresenter create() {
                return new HelpCenterPresenter(HelpUseCaseManager.provideHelpCenterCase());
            }
        });
    }

    @Override
    public void showError(int resultCode, final String msg) {
        super.showError(resultCode, msg);
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if (showDialog) {
                    showDialog = false;
                }
                RefreshLayoutManager.finishRefreshAndLoadMore(refreshLayout, false);
            }
        });
    }

    @Override
    public String getSearchKey() {
        return searchKey;
    }

    @Override
    public int getItemId() {
        return targetId;
    }

    @Override
    public Map<String, String> getCustomMap() {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.PAGE, String.valueOf(nextPage));
        map.put(Field.PER_PAGE, String.valueOf(Field.PAGE_SIZE));
        return map;
    }

    @Override
    public void onLoadHelpCenterList(final int _nextPage, final List<HelpCenterItem> list) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if (isRefresh) {
                    listItem.clear();
                    isRefresh = false;
                }
                if (showDialog) {
                    showDialog = false;
                }
                nextPage = _nextPage;
                listItem.addAll(list);
                mHelpCenterAdapter.notifyDataSetChanged();
                RefreshLayoutManager.setEmptyViewVisibility(listItem, findViewById(R.id.kf5_empty_layout));
                RefreshLayoutManager.finishRefreshAndLoadMore(refreshLayout, _nextPage > 1);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (ClickUtils.isInvalidClick(view)) {
            return;
        }
        int id = view.getId();
        if (id == R.id.kf5_right_text_view) {
            startActivity(new Intent(mActivity, LookFeedBackActivity.class));
        } else if (id == R.id.kf5_help_center_head_view) {
            startActivityForResult(new Intent(this, SearchActivity.class), SEARCH_REQUEST_CODE);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try {
            if (position == 0 || ClickUtils.isInvalidClick(view))
                return;
            Intent intent = new Intent();
            HelpCenterItem item = mHelpCenterAdapter.getItem(position - 1);
            intent.putExtra(Field.ID, item.getId());
            intent.putExtra(Field.TITLE, item.getTitle());
            if (!isSearch) {
                switch (helpCenterType) {
                    case Category:
                        intent.setClass(mActivity, HelpCenterTypeActivity.class);
                        break;
                    case Forum:
                        intent.setClass(mActivity, HelpCenterTypeChildActivity.class);
                        break;
                    case Post:
                        intent.setClass(mActivity, HelpCenterTypeDetailsActivity.class);
                        break;
                }
            } else {
                intent.setClass(mActivity, HelpCenterTypeDetailsActivity.class);
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SEARCH_REQUEST_CODE || resultCode == RESULT_OK) {
            String searchKey = data.getStringExtra(SearchActivity.SEARCH_KEY);
            if (!TextUtils.isEmpty(searchKey)) {
                this.searchKey = searchKey;
                isSearch = true;
                showDialog = true;
                isRefresh = true;
                nextPage = 1;
                setTitle();
                presenter.searchDocument(HelpCenterRequestType.SEARCH);
            } else {
                this.searchKey = "";
                showToast(getString(R.string.kf5_content_not_null));
            }
        }
    }

    private void setTitle() {
        if (isSearch) {
            setTitleContent(getString(R.string.kf5_article_search));
        } else {
            setTitleContent(getString(helpCenterType.titleRes));
        }
    }
}
