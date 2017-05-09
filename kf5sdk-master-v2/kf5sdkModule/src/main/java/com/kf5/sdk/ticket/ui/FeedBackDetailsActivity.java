package com.kf5.sdk.ticket.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.entity.ParamsKey;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5.sdk.ticket.adapter.FeedBackDetailAdapter;
import com.kf5.sdk.ticket.db.KF5SDKtoHelper;
import com.kf5.sdk.ticket.entity.Comment;
import com.kf5.sdk.ticket.entity.Message;
import com.kf5.sdk.ticket.entity.MessageStatus;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.mvp.presenter.TicketDetailPresenter;
import com.kf5.sdk.ticket.mvp.usecase.TicketUseCaseManager;
import com.kf5.sdk.ticket.mvp.view.ITicketDetailView;
import com.kf5.sdk.ticket.receiver.RatingReceiver;
import com.kf5.sdk.ticket.receiver.TicketReceiver;
import com.kf5.sdk.ticket.widgets.FeedBackDetailBottomView;
import com.kf5.sdk.ticket.widgets.api.FeedBackDetailBottomViewListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;


public class FeedBackDetailsActivity extends BaseActivity<TicketDetailPresenter, ITicketDetailView> implements ITicketDetailView, FeedBackDetailBottomViewListener, AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener, View.OnClickListener, RatingReceiver.RatingListener {

    private int nextPage = 1;

    private ListView mListView;

    private FeedBackDetailAdapter mFeedBackDetailAdapter;

    private List<Comment> mCommentList;

    private int lastItem;

    private KF5SDKtoHelper mHelper;

    private BottomLayoutListener layoutListener;

    private FeedBackDetailBottomView mFeedBackDetailBottomView;

    private RelativeLayout mLayoutBottom;

    private EditText mETContent;

    private ImageView mBackImg;

    private TextView mRightView;

    private int ticket_id;

    private int commentPosition;

    private LinearLayout mHeaderView;

    private TextView mHeaderContent;

    private String mRatingContent;

    private int mRatingStatus;

    private RatingReceiver mRatingReceiver;

    private String nickName;


    @Override
    protected void initWidgets() {
        super.initWidgets();
        mLayoutBottom = (RelativeLayout) findViewById(R.id.kf5_bottom_layout);
        mFeedBackDetailBottomView = new FeedBackDetailBottomView(mActivity);
        mFeedBackDetailBottomView.setListener(this);
        mETContent = layoutListener.getEditText();
        mLayoutBottom.addView(mFeedBackDetailBottomView);
        mListView = (ListView) findViewById(R.id.kf5_activity_feed_back_details_listview);
        mListView.setOnScrollListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.addHeaderView(inflateHeaderView());
        mBackImg = (ImageView) findViewById(R.id.kf5_return_img);
        mBackImg.setOnClickListener(this);
        mRightView = (TextView) findViewById(R.id.kf5_right_text_view);
        mRightView.setOnClickListener(this);
    }

    private View inflateHeaderView() {
        mHeaderView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.kf5_rating_header, null);
        mHeaderView.setOnClickListener(this);
        mHeaderContent = (TextView) mHeaderView.findViewById(R.id.kf5_rating_status);
        return mHeaderView;
    }


    @Override
    protected void setData() {
        super.setData();
        mHelper = new KF5SDKtoHelper(mActivity);
        mHelper.openDatabase();
        mListView.setAdapter(mFeedBackDetailAdapter = new FeedBackDetailAdapter(mActivity, mCommentList = new ArrayList<>()));
        mRatingReceiver = new RatingReceiver();
        mRatingReceiver.setRatingListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RatingReceiver.RATING_FILTER);
        registerReceiver(mRatingReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        unregisterReceiver(mRatingReceiver);
    }

    public void setLayoutListener(BottomLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    @Override
    public Loader<TicketDetailPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<TicketDetailPresenter>() {
            @Override
            public TicketDetailPresenter create() {
                return new TicketDetailPresenter(TicketUseCaseManager.provideTicketDetailCase());
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<TicketDetailPresenter> loader, TicketDetailPresenter data) {
        super.onLoadFinished(loader, data);
        showDialog = true;
        presenter.getTicketDetail();
    }

    @Override
    public void showError(int resultCode, String msg) {
        super.showError(resultCode, msg);
        showToast(msg);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_activity_feed_back_details;
    }

    @Override
    public Map<String, String> getTicketDetailMap() {
        Map<String, String> map = new ArrayMap<>();
        map.put(ParamsKey.PAGE, String.valueOf(nextPage));
        map.put(ParamsKey.PER_PAGE, String.valueOf(Field.PAGE_SIZE));
        return map;
    }

    @Override
    public int getTicketId() {
        ticket_id = getIntent().getIntExtra(Field.ID, 0);
        return ticket_id;
    }


    @Override
    public void loadTicketDetail(final int _nextPage, final Requester requester, final List<Comment> commentList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mCommentList.addAll(commentList);
                    if (requester != null) {
                        if (requester.getStatus() == 4) {
                            mFeedBackDetailBottomView.setTvReplaceVisible();
                        }
                        if (!requester.isRatingFlag()) {
                            mListView.removeHeaderView(mHeaderView);
                        } else {
                            mHeaderView.setVisibility(View.VISIBLE);
                        }
                        if (requester.getRating() >= 1 && requester.getRating() <= 5) {
                            List<String> mRatingList = Arrays.asList(getResources().getStringArray(R.array.kf5_rating_status));
                            mHeaderContent.setText(mRatingList.get(requester.getRating() - 1));
                            mHeaderContent.setBackgroundResource(R.drawable.kf5_rating_status_bg);
                            mRatingStatus = requester.getRating();
                        }
                        mRatingContent = requester.getRatingContent();
                        nickName = commentList.get(0).getAuthorName();
                    }
                    mFeedBackDetailAdapter.notifyDataSetChanged();
                    nextPage = _nextPage;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public List<File> getFileList() {
        return layoutListener.getFileList();
    }

    @Override
    public void replyTicketSuccess(final Requester requester) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                try {
                    Comment comment = mCommentList.get(commentPosition);
                    comment.setMessageStatus(MessageStatus.SUCCESS);
                    mFeedBackDetailAdapter.notifyDataSetChanged();
                    if (layoutListener != null)
                        layoutListener.onSubmitDataSuccess();
                    Message updateMessage = new Message();
                    updateMessage.setId(String.valueOf(requester.getId()));
                    updateMessage.setLastCommentId(String.valueOf(requester.getLast_comment_id()));
                    updateMessage.setRead(false);
                    mHelper.updateDataByID(updateMessage);

                    //通知更新
                    Intent intent = new Intent();
                    intent.setAction(TicketReceiver.UPDATE_LIST_ITEM_DATA);
                    intent.putExtra(Field.ID, requester.getId());
                    intent.putExtra(Field.LAST_COMMENT_ID, requester.getLast_comment_id());
                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void replyTicketError(final String msg) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                showToast(msg);
                Comment comment = mCommentList.get(commentPosition);
                comment.setMessageStatus(MessageStatus.FAILED);
                mFeedBackDetailAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void submitData() {

        Comment comment = new Comment();
        comment.setContent(mETContent.getText().toString());
        comment.setCreatedAt(System.currentTimeMillis() / 1000);
        comment.setMessageStatus(MessageStatus.SENDING);
//        comment.setAuthorName(!TextUtils.isEmpty(SPUtils.getUserName()) ? SPUtils.getUserName() : "Android SDK");
        comment.setAuthorName(nickName);
        List<Attachment> attachments = new ArrayList<>();
        for (int i = 0; i < getFileList().size(); i++) {
            Attachment attachment = new Attachment();
            attachment.setContent_url(getFileList().get(i).getAbsolutePath());
            attachment.setName(getFileList().get(i).getName());
            attachments.add(attachment);
        }
        comment.setAttachmentList(attachments);
        mCommentList.add(comment);
        commentPosition = mCommentList.indexOf(comment);
        mListView.setSelection(mCommentList.size() - 1);
        showDialog = false;
        String content = mETContent.getText().toString();
        Map<String, String> map = new ArrayMap<>();
        map.put(ParamsKey.CONTENT, content);
        map.put(ParamsKey.TICKET_ID, String.valueOf(ticket_id));
        mETContent.setText("");
        presenter.replayTicket(map);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //滑动过程中不加载图片
        if (scrollState == SCROLL_STATE_FLING) {
            ImageLoaderManager.getInstance(mActivity).pause();
        } else {
            ImageLoaderManager.getInstance(mActivity).onResume();
        }
        Utils.hideSoftInput(mActivity, mETContent);
        if (lastItem == mCommentList.size() && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (nextPage != -100 && nextPage != 1) {
                //发送请求
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        lastItem = firstVisibleItem + visibleItemCount;
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
        final Comment comment = mFeedBackDetailAdapter.getItem(i);
        if (comment.getMessageStatus() == MessageStatus.FAILED) {
            new DialogBox(mActivity)
                    .setMessage(getString(R.string.kf5_resend_message_hint))
                    .setLeftButton(getString(R.string.kf5_cancel), null)
                    .setRightButton(getString(R.string.kf5_resend), new DialogBox.onClickListener() {
                        @Override
                        public void onClick(DialogBox dialog) {
                            try {
                                dialog.dismiss();
                                Map<String, String> map = new ArrayMap<>();
                                map.put(ParamsKey.CONTENT, comment.getContent());
                                map.put(ParamsKey.TICKET_ID, String.valueOf(ticket_id));
                                presenter.replayTicket(map);
                                comment.setMessageStatus(MessageStatus.SENDING);
                                mFeedBackDetailAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (layoutListener != null)
            layoutListener.onFeedBackDetailsActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.kf5_return_img) {
            finish();
        } else if (id == R.id.kf5_right_text_view) {
            Intent intent = new Intent(mActivity, OrderAttributeActivity.class);
            intent.putExtra(Field.ID, getTicketId());
            startActivity(intent);
        } else if (id == R.id.kf5_rating_header) {
            Intent intent = new Intent(mActivity, RatingActivity.class);
            intent.putExtra(Field.ID, getTicketId());
            intent.putExtra(Field.RATING, mRatingStatus);
            intent.putExtra(Field.RATING_CONTENT, mRatingContent);
            startActivity(intent);
        }
    }

    @Override
    public void ratingSuccess(int rating, String content) {
        if (rating >= 1 && rating <= 5) {
            mRatingStatus = rating;
            List<String> mRatingList = Arrays.asList(getResources().getStringArray(R.array.kf5_rating_status));
            mHeaderContent.setText(mRatingList.get(mRatingStatus - 1));
            mHeaderContent.setBackgroundResource(R.drawable.kf5_rating_status_bg);
        }
        mRatingContent = content;
    }

    public interface BottomLayoutListener {

        void onFeedBackDetailsActivityResult(int requestCode, int resultCode, Intent data);

        void onSubmitDataSuccess();

        EditText getEditText();

        List<File> getFileList();
    }

}
