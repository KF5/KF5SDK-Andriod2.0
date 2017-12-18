package com.kf5.sdk.ticket.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.ticket.adapter.UserFieldAdapter;
import com.kf5.sdk.ticket.entity.UserField;
import com.kf5.sdk.ticket.mvp.presenter.TicketAttributePresenter;
import com.kf5.sdk.ticket.mvp.usecase.TicketUseCaseManager;
import com.kf5.sdk.ticket.mvp.view.ITicketAttributeView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class OrderAttributeActivity extends BaseActivity<TicketAttributePresenter, ITicketAttributeView> implements ITicketAttributeView, View.OnClickListener {

    private ListView mListView;

    private List<UserField> mUserFields;

    private UserFieldAdapter mUserFieldAdapter;

    private ImageView mBackImg;


    @Override
    protected int getLayoutID() {
        return R.layout.kf5_activity_order_attribute;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mBackImg = (ImageView) findViewById(R.id.kf5_return_img);
        mBackImg.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.kf5_activity_order_attr_list_view);
        mUserFields = new ArrayList<>();
        mUserFieldAdapter = new UserFieldAdapter(mActivity, mUserFields);
        mListView.setAdapter(mUserFieldAdapter);
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

    @Override
    public void showError(int resultCode, String msg) {
        super.showError(resultCode, msg);
        showToast(msg);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.kf5_return_img) {
            finish();
        }
    }
}
