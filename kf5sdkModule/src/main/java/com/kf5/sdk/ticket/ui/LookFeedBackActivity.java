package com.kf5.sdk.ticket.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.BaseMVPActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.RefreshLayoutConfig;
import com.kf5.sdk.system.entity.TitleBarProperty;
import com.kf5.sdk.system.manager.RefreshLayoutManager;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.utils.ClickUtils;
import com.kf5.sdk.ticket.adapter.FeedBackAdapter;
import com.kf5.sdk.ticket.db.KF5SDKtoHelper;
import com.kf5.sdk.ticket.entity.Message;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.mvp.presenter.TicketListPresenter;
import com.kf5.sdk.ticket.mvp.usecase.TicketUseCaseManager;
import com.kf5.sdk.ticket.mvp.view.ITicketListView;
import com.kf5.sdk.ticket.receiver.TicketReceiver;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LookFeedBackActivity extends BaseMVPActivity<TicketListPresenter, ITicketListView> implements ITicketListView, AdapterView.OnItemClickListener, TicketReceiver.RefreshTicketListener, View.OnClickListener {

    private ListView mListView;

    private FeedBackAdapter mAdapter;

    private List<Requester> mList;

    private int nextPage = 1;

    private KF5SDKtoHelper mKF5SDKtoHelper;

    private TicketReceiver mTicketReceiver;

    private RefreshLayout refreshLayout;

    private boolean isRefresh;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKF5SDKtoHelper != null) {
            mKF5SDKtoHelper.close();
            mKF5SDKtoHelper = null;
        }
        unregisterReceiver(mTicketReceiver);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mListView = (ListView) findViewById(R.id.kf5_listView);
        refreshLayout = (RefreshLayout) findViewById(R.id.kf5_refreshLayout);
        RefreshLayoutConfig.start()
                .with(this)
                .withListView(mListView)
                .listViewDivider(getResources().getDrawable(R.drawable.kf5_divider_inset_left_16))
                .listViewDividerHeight(1)
                .listViewItemClickListener(this)
                .withRefreshLayout(refreshLayout)
                .refreshLayoutEnableRefreshAndLoadMore(true, true)
                .refreshLayoutAutoLoadMore(false)
                .refreshLayoutOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        isRefresh = true;
                        showDialog = false;
                        nextPage = 1;
                        presenter.getTicketList();
                    }
                })
                .refreshLayoutOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        presenter.getTicketList();
                    }
                })
                .refreshLayoutEmptyLayoutResource(null, getString(R.string.kf5_no_feedback));
    }

    @Override
    protected void setData() {
        super.setData();
        mTicketReceiver = new TicketReceiver();
        mTicketReceiver.setRefreshTicketListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TicketReceiver.TICKET_FILTER);
        intentFilter.addAction(TicketReceiver.UPDATE_LIST_ITEM_DATA);
        registerReceiver(mTicketReceiver, intentFilter);
        mKF5SDKtoHelper = new KF5SDKtoHelper(mActivity);
        mList = new ArrayList<>();
        mAdapter = new FeedBackAdapter(mActivity, mList, mKF5SDKtoHelper);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_layout_refresh_listview;
    }

    @Override
    protected TitleBarProperty getTitleBarProperty() {
        return new TitleBarProperty.Builder()
                .setTitleContent(getString(R.string.kf5_feedback_list))
                .setRightViewVisible(true)
                .setRightViewClick(true)
                .setRightViewContent(getString(R.string.kf5_contact_us))
                .build();
    }

    @Override
    public Loader<TicketListPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<TicketListPresenter>() {
            @Override
            public TicketListPresenter create() {
                return new TicketListPresenter(TicketUseCaseManager.provideTicketListCase());
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<TicketListPresenter> loader, TicketListPresenter data) {
        super.onLoadFinished(loader, data);
        showDialog = true;
        presenter.getTicketList();
    }


    @Override
    public void showError(int resultCode, String msg) {
        super.showError(resultCode, msg);
        RefreshLayoutManager.finishRefreshAndLoadMore(refreshLayout, false);
    }

    @Override
    public Map<String, String> getCustomMap() {
        Map<String, String> map = new ArrayMap<>();
        map.put(Field.PAGE, String.valueOf(nextPage));
        map.put(Field.PER_PAGE, String.valueOf(Field.PAGE_SIZE));
        return map;
    }

    @Override
    public void loadResultData(final int _nextPage, final List<Requester> mRequesterList) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  if (isRefresh) {
                                      isRefresh = false;
                                      mList.clear();
                                  }
                                  if (showDialog) {
                                      showDialog = false;
                                  }
                                  mList.addAll(mRequesterList);
                                  nextPage = _nextPage;
                                  mAdapter.notifyDataSetChanged();
                                  RefreshLayoutManager.finishRefreshAndLoadMore(refreshLayout, _nextPage > 1);
                                  RefreshLayoutManager.setEmptyViewVisibility(mList, findViewById(R.id.kf5_empty_layout));
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      }

        );
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try {
            if (ClickUtils.isInvalidClick(view)) {
                return;
            }
            Intent intent = new Intent();
            Requester requester = mAdapter.getItem(position);
            intent.putExtra(Field.ID, requester.getId());
            intent.putExtra(Field.TITLE, requester.getTitle());
            intent.putExtra(Field.STATUS, requester.getStatus());
            intent.setClass(mActivity, FeedBackDetailsActivity.class);
            View view2 = mListView.getChildAt(position - mListView.getFirstVisiblePosition());
            ImageView statusView;
            if (view2 != null) {
                statusView = view2.findViewById(R.id.kf5_look_feed_back_listitem_update);
                if (statusView.getVisibility() == View.VISIBLE) {
                    statusView.setVisibility(View.INVISIBLE);
                    Message updateMessage = new Message();
                    updateMessage.setId(String.valueOf(requester.getId()));
                    updateMessage.setLastCommentId(String.valueOf(requester.getLast_comment_id()));
                    updateMessage.setRead(false);
                    mKF5SDKtoHelper.updateDataByID(updateMessage);
                }
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refreshTicket() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(0);
                refreshLayout.autoRefresh();
            }
        }, 1_000);
    }

    @Override
    public void updateCommentId(int id, int last_comment_id) {
        for (Requester requester : mList) {
            if (requester.getId() == id && last_comment_id != 0 && requester.getLast_comment_id() != last_comment_id) {
                requester.setLast_comment_id(last_comment_id);
                break;
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (ClickUtils.isInvalidClick(view)) {
            return;
        }
        int id = view.getId();
        if (id == R.id.kf5_right_text_view) {
            startActivity(new Intent(mActivity, FeedBackActivity.class));
        }
    }
}
