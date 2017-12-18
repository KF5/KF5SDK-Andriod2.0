package com.kf5.sdk.helpcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.adapter.HelpCenterAdapter;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.helpcenter.entity.HelpCenterRequestType;
import com.kf5.sdk.helpcenter.mvp.presenter.HelpCenterPresenter;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpUseCaseManager;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterView;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.RefreshListView;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 帮助中心之文档分区
 */
public class HelpCenterActivity extends BaseActivity<HelpCenterPresenter, IHelpCenterView> implements
        IHelpCenterView, View.OnClickListener, View.OnKeyListener,
        RefreshListView.onScrollState, RefreshListView.onScrollChange,
        RefreshListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private RefreshListView mListView;

    private LinearLayout mReminderLayout;

    private RelativeLayout mSearchLayout;

    private ImageView mImageDelete, mBackImage;

    private EditText mEditText;

    private TextView mTitleView, mRightView;

    private HelpCenterAdapter mHelpCenterAdapter;

    private List<HelpCenterItem> listItem = new ArrayList<>();

    private boolean isSearch = false;

    private int nextPage = 1;

    private int lastItem;

    private Timer mTimer;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onLoadFinished(Loader<HelpCenterPresenter> loader, HelpCenterPresenter data) {
        super.onLoadFinished(loader, data);
        showDialog = true;
        presenter.getCategoriesList(HelpCenterRequestType.DEFAULT);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mReminderLayout = (LinearLayout) findViewById(R.id.kf5_serch_reminder_layout);
        mReminderLayout.setOnClickListener(this);
        mSearchLayout = (RelativeLayout) findViewById(R.id.kf5_search_layout_content);
        mEditText = (EditText) findViewById(R.id.kf5_search_content_edittext);
        mEditText.setOnKeyListener(this);
        mImageDelete = (ImageView) findViewById(R.id.kf5_img_delete_content);
        mImageDelete.setOnClickListener(this);
        mListView = (RefreshListView) findViewById(R.id.kf5_help_center_listview);
        mListView.setOnScrollState(this);
        mListView.setOnScrollChange(this);
        mListView.addFooterView();
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mHelpCenterAdapter = new HelpCenterAdapter(mActivity, listItem);
        mListView.setAdapter(mHelpCenterAdapter);
        mTitleView = (TextView) findViewById(R.id.kf5_title);
        mTitleView.setText(R.string.kf5_article_category);
        mRightView = (TextView) findViewById(R.id.kf5_right_text_view);
        mRightView.setOnClickListener(this);
        mTimer = new Timer();
        mBackImage = (ImageView) findViewById(R.id.kf5_return_img);
        mBackImage.setOnClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_activity_help_center;
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
                mListView.onRefreshComplete();
                mListView.setFooterViewInvisible();
                showToast(msg);
            }
        });
    }

    @Override
    public String getSearchKey() {
        return mEditText.getText().toString();
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
                if (_nextPage == 1 || _nextPage == -100 || _nextPage == 0)
                    listItem.clear();
                nextPage = _nextPage;
                listItem.addAll(list);
                mListView.onRefreshComplete();
                mListView.setFooterViewInvisible();
                mHelpCenterAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.kf5_serch_reminder_layout) {
            mEditText.setText("");
            mReminderLayout.setVisibility(View.GONE);
            mSearchLayout.setVisibility(View.VISIBLE);
            mEditText.requestFocus();
            Utils.showSoftInput(mActivity, mEditText);
        } else if (id == R.id.kf5_img_delete_content) {
            mEditText.setText("");
        } else if (id == R.id.kf5_right_text_view) {
            startActivity(new Intent(mActivity, LookFeedBackActivity.class));
        } else if (id == R.id.kf5_return_img) {
            finish();
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        int id = view.getId();
        if (id == R.id.kf5_search_content_edittext) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (!TextUtils.isEmpty(mEditText.getText()) && !TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    Utils.hideSoftInput(mActivity, mEditText);
                    mReminderLayout.setVisibility(View.VISIBLE);
                    mSearchLayout.setVisibility(View.GONE);
                    mListView.setRefresh(false);
                    isSearch = true;
                    showDialog = true;
                    nextPage = 1;
                    presenter.searchDocument(HelpCenterRequestType.SEARCH);
                } else {
                    showToast(getString(R.string.kf5_content_not_null));
                }
            }
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Utils.hideSoftInput(mActivity, mEditText);
        if (lastItem == listItem.size() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (nextPage != -100 && nextPage != 1) {
                mListView.setFooterViewLoadingData();
                showDialog = false;
                if (isSearch) presenter.searchDocument(HelpCenterRequestType.SEARCH);
                else presenter.getCategoriesList(HelpCenterRequestType.DEFAULT);
            } else {
                mListView.setFooterViewNoData();
                if (mTimer != null) {
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
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount - 2;
    }

    @Override
    public void onRefresh() {
        nextPage = 1;
        isSearch = false;
        showDialog = false;
        presenter.getCategoriesList(HelpCenterRequestType.DEFAULT);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        try {
            if (position == 0 || position == mHelpCenterAdapter.getCount() + 1)
                return;
            Intent intent = new Intent();
            HelpCenterItem item = mHelpCenterAdapter.getItem(position - 1);
            intent.putExtra(Field.ID, item.getId());
            intent.putExtra(Field.TITLE, item.getTitle());
            if (!isSearch) {
                intent.setClass(mActivity, HelpCenterTypeActivity.class);
            } else {
                intent.setClass(mActivity, HelpCenterTypeDetailsActivity.class);
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

