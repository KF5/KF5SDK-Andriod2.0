package com.kf5.sdk.ticket.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.widget.RefreshListView;
import com.kf5.sdk.ticket.adapter.FeedBackAdapter;
import com.kf5.sdk.ticket.db.KF5SDKtoHelper;
import com.kf5.sdk.ticket.entity.Message;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.mvp.presenter.TicketListPresenter;
import com.kf5.sdk.ticket.mvp.usecase.TicketUseCaseManager;
import com.kf5.sdk.ticket.mvp.view.ITicketListView;
import com.kf5.sdk.ticket.receiver.TicketReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LookFeedBackActivity extends BaseActivity<TicketListPresenter, ITicketListView> implements ITicketListView, RefreshListView.OnRefreshListener, RefreshListView.onScrollChange, RefreshListView.onScrollState, AdapterView.OnItemClickListener, TicketReceiver.RefreshTicketListener, View.OnClickListener {

    private RefreshListView mListView;

    private FeedBackAdapter mAdapter;

    private List<Requester> mList;

    private int lastItem;

    private Timer mTimer;

    private int nextPage = 1;

    private KF5SDKtoHelper mKF5SDKtoHelper;

    private TextView mTextViewReplace;

    private TicketReceiver mTicketReceiver;

    private ImageView mBackImg;

    private TextView mTextViewConnectUs;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKF5SDKtoHelper != null) {
            mKF5SDKtoHelper.close();
            mKF5SDKtoHelper = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        unregisterReceiver(mTicketReceiver);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mListView = (RefreshListView) findViewById(R.id.kf5_look_feed_back_listview);
        mListView.addFooterView();
        mListView.setOnRefreshListener(this);
        mListView.setOnScrollChange(this);
        mListView.setOnScrollState(this);
        mListView.setOnItemClickListener(this);
        mTextViewReplace = (TextView) findViewById(R.id.kf5_look_feed_back_reminder_tv);
        mBackImg = (ImageView) findViewById(R.id.kf5_return_img);
        mBackImg.setOnClickListener(this);
        mTextViewConnectUs = (TextView) findViewById(R.id.kf5_right_text_view);
        mTextViewConnectUs.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        super.setData();
        mTimer = new Timer();
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
        return R.layout.kf5_activity_look_feed_back;
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
        showToast(msg);
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
                                  mListView.onRefreshComplete();
                                  mListView.setFooterViewInvisible();
                                  if (nextPage == 1 || nextPage == -100)
                                      mList.clear();
                                  mList.addAll(mRequesterList);
                                  if (mList.size() == 0)
                                      mTextViewReplace.setVisibility(View.VISIBLE);
                                  else
                                      mTextViewReplace.setVisibility(View.GONE);
                                  nextPage = _nextPage;
                                  mAdapter.notifyDataSetChanged();
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                      }

        );
    }

    @Override
    public void onRefresh() {
        showDialog = false;
        nextPage = 1;
        presenter.getTicketList();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 2;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (lastItem == mList.size() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (nextPage != -100 && nextPage != 1 && nextPage != 0) {
                showDialog = false;
                mListView.setFooterViewLoadingData();
                presenter.getTicketList();
            } else {
                mListView.setFooterViewNoData();
                if (mTimer != null)
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mListView.setFooterViewInvisibleWithAnim();
                                }
                            });
                        }
                    }, 1000);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        try {
            if (position == 0 || position == mAdapter.getCount() + 1)
                return;
            Intent intent = new Intent();
            Requester requester = mAdapter.getItem(position - 1);
            intent.putExtra(Field.ID, requester.getId());
            intent.putExtra(Field.TITLE, requester.getTitle());
            intent.putExtra(Field.STATUS, requester.getStatus());
            intent.setClass(mActivity, FeedBackDetailsActivity.class);
            View view2 = mListView.getChildAt(position - mListView.getFirstVisiblePosition());
            ImageView statusView;
            if (view2 != null) {
                statusView = (ImageView) view2.findViewById(R.id.kf5_look_feed_back_listitem_update);
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
        mListView.setSelection(0);
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setStateRefreshing();
                mListView.setRefresh(true);
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                }, 500);
            }
        }, 500);
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
        int id = view.getId();
        if (id == R.id.kf5_return_img) {
            finish();
        } else if (id == R.id.kf5_right_text_view) {
            startActivity(new Intent(mActivity, FeedBackActivity.class));
        }
    }
}
