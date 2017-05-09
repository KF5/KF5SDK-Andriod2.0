package com.kf5.sdk.ticket.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.ticket.adapter.CheckAdapter;
import com.kf5.sdk.ticket.entity.CheckItem;
import com.kf5.sdk.ticket.mvp.presenter.RatingPresenter;
import com.kf5.sdk.ticket.mvp.usecase.TicketUseCaseManager;
import com.kf5.sdk.ticket.mvp.view.IRatingView;
import com.kf5.sdk.ticket.receiver.RatingReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RatingActivity extends BaseActivity<RatingPresenter, IRatingView> implements IRatingView, View.OnClickListener {

    private ListView mListView;

    private List<CheckItem> mDatas;

    private CheckAdapter mAdapter;

    private EditText mETContent;

    private ImageView mBackImg;

    private TextView mTVSubmit;

    private int mTicketId;

    private int mRatingStatus;

    @Override
    public Loader<RatingPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<RatingPresenter>() {
            @Override
            public RatingPresenter create() {
                return new RatingPresenter(TicketUseCaseManager.provideRatingCase());
            }
        });
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mETContent = (EditText) findViewById(R.id.kf5_rating_comment);
        mBackImg = (ImageView) findViewById(R.id.kf5_return_img);
        mBackImg.setOnClickListener(this);
        mTVSubmit = (TextView) findViewById(R.id.kf5_right_text_view);
        mTVSubmit.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.kf5_rating_lv);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mTVSubmit.isEnabled()) {
                    mTVSubmit.setEnabled(true);
                }
                CheckItem checkItem = mDatas.get(position);
                if (!checkItem.isSelected()) {
                    for (CheckItem item : mDatas) {
                        item.setSelected(false);
                    }
                    checkItem.setSelected(true);
                    mAdapter.notifyDataSetChanged();
                    mRatingStatus = position + 1;
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_activity_rating;
    }


    @Override
    protected void setData() {
        super.setData();
        Intent intent = getIntent();
        String mRatingContent = intent.getStringExtra(Field.RATING_CONTENT);
        mRatingStatus = intent.getIntExtra(Field.RATING, 0);
        if (mRatingStatus < 1 || mRatingStatus > 5) {
            mTVSubmit.setEnabled(false);
        }
        mTicketId = intent.getIntExtra(Field.ID, 0);
        if (!TextUtils.isEmpty(mRatingContent)) {
            mETContent.setText(mRatingContent);
        }

        mDatas = new ArrayList<>();
        List<String> mRatingList = Arrays.asList(getResources().getStringArray(R.array.kf5_rating_status));
        for (int i = 0; i < mRatingList.size(); i++) {
            String content = mRatingList.get(i);
            if (mRatingStatus == i + 1) {
                mDatas.add(new CheckItem(content, true));
            } else {
                mDatas.add(new CheckItem(content, false));
            }
        }
        mAdapter = new CheckAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.kf5_return_img) {
            finish();
        } else if (id == R.id.kf5_right_text_view) {
            Map<String, String> dataMap = new ArrayMap<>();
            dataMap.put(Field.CONTENT, mETContent.getText().toString());
            dataMap.put(Field.TICKET_ID, String.valueOf(mTicketId));
            dataMap.put(Field.RATING, String.valueOf(mRatingStatus));
            showDialog = true;
            presenter.rating(dataMap);
        }
    }

    @Override
    public void onLoadRatingData(final int resultCode, final String message) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == 0) {
                    Intent intent = new Intent(RatingReceiver.RATING_FILTER);
                    intent.putExtra(Field.RATING, mRatingStatus);
                    intent.putExtra(Field.RATING_CONTENT, mETContent.getText().toString());
                    sendBroadcast(intent);
                    finish();
                }
                showToast(message);
            }
        });
    }

    @Override
    public void showError(int resultCode, String msg) {
        super.showError(resultCode, msg);
        showToast(msg);
    }
}
