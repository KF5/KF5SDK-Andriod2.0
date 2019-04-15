package com.kf5.sdk.ticket.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.widget.ListView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.BaseMVPActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.RefreshLayoutConfig;
import com.kf5.sdk.system.entity.TitleBarProperty;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.ticket.adapter.UserFieldAdapter;
import com.kf5.sdk.ticket.entity.UserField;
import com.kf5.sdk.ticket.mvp.presenter.TicketAttributePresenter;
import com.kf5.sdk.ticket.mvp.usecase.TicketUseCaseManager;
import com.kf5.sdk.ticket.mvp.view.ITicketAttributeView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class OrderAttributeActivity extends BaseMVPActivity<TicketAttributePresenter, ITicketAttributeView> implements ITicketAttributeView {

    private List<UserField> mUserFields;

    private UserFieldAdapter mUserFieldAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_layout_refresh_listview;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mUserFields = new ArrayList<>();
        RefreshLayoutConfig.start()
                .with(this)
                .withListView((ListView) findViewById(R.id.kf5_listView))
                .listViewDivider(getResources().getDrawable(R.drawable.kf5_divider_inset_left_16))
                .listViewDividerHeight(1)
                .withRefreshLayout(((RefreshLayout) findViewById(R.id.kf5_refreshLayout)))
                .refreshLayoutEnableRefreshAndLoadMore(false, false)
                .commitWithSetAdapter(mUserFieldAdapter = new UserFieldAdapter(mActivity, mUserFields));
    }

    @Override
    protected TitleBarProperty getTitleBarProperty() {
        return new TitleBarProperty.Builder()
                .setTitleContent(getString(R.string.kf5_message_detail))
                .setRightViewVisible(false)
                .build();
    }

    @Override
    public Loader<TicketAttributePresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<TicketAttributePresenter>() {
            @Override
            public TicketAttributePresenter create() {
                return new TicketAttributePresenter(TicketUseCaseManager.provideTicketAttributeCase());
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<TicketAttributePresenter> loader, TicketAttributePresenter data) {
        super.onLoadFinished(loader, data);
        showDialog = true;
        presenter.getTicketAttribute();
    }

    @Override
    public int getTicketId() {
        return getIntent().getIntExtra(Field.ID, 0);
    }

    @Override
    public void onLoadTicketAttribute(final List<UserField> list) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                mUserFields.addAll(list);
                mUserFieldAdapter.notifyDataSetChanged();
            }
        });
    }
}
