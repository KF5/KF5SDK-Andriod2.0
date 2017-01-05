package com.kf5.sdk.im.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.MessageAdapter;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.IMMessageManager;
import com.kf5.sdk.im.expression.utils.ExpressionCommonUtils;
import com.kf5.sdk.im.keyboard.EmoticonsKeyBoard;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.im.keyboard.widgets.AppsView;
import com.kf5.sdk.im.keyboard.widgets.EmoticonsEditText;
import com.kf5.sdk.im.keyboard.widgets.FuncLayout;
import com.kf5.sdk.im.mvp.presenter.IMPresenter;
import com.kf5.sdk.im.mvp.usecase.IMCaseManager;
import com.kf5.sdk.im.mvp.view.IIMView;
import com.kf5.sdk.im.widget.AudioRecordButton;
import com.kf5.sdk.im.widget.RatingDialog;
import com.kf5.sdk.system.api.KF5SystemAPI;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.image.ImageSelectorActivity;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5.sdk.ticket.ui.FeedBackActivity;
import com.kf5.sdk.ticket.ui.LookFeedBackActivity;
import com.kf5Engine.image.ImageCompressManager;
import com.kf5Engine.image.OnCompressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/28 18:31
 * email:812219713@qq.com
 */

public abstract class BaseChatActivity extends BaseActivity<IMPresenter, IIMView> implements IIMView, View.OnClickListener,
        FuncLayout.OnFuncKeyBoardListener, AbsListView.OnScrollListener, RatingDialog.OnRatingItemClickListener,
        AudioRecordButton.AudioFinishRecorderListener, View.OnLongClickListener {

    protected EmoticonsKeyBoard mXhsEmoticonsKeyBoard;

    private ListView mListView;

    private ImageView mImageView;

    protected List<IMMessage> mIMMessageList = new ArrayList<>();

    protected MessageAdapter mAdapter;

    private TextView mTextViewTitle;

    protected static final int OK = IMPresenter.RESULT_OK;

    protected RatingDialog mRatingDialog;

    private ImageCompressManager mImageCompressManager;

    private TextView mTextViewRight;

    protected EditText mEditTextAI, mEditTextQueue, mEditTextIM;

    private static final String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},

    READ_PHONE_STATE_PERMISSION = {Manifest.permission.READ_PHONE_STATE},

    VOICE_RECORDER_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},

    WRITE_EXTERNAL_STORAGE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private long dbMessageCount;

    private int getMessageFromDBTime = 1;

    protected boolean isAgentOnline = false;


    @Override
    public Loader<IMPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<IMPresenter>() {
            @Override
            public IMPresenter create() {
                return new IMPresenter(IMCaseManager.provideIMCase());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mXhsEmoticonsKeyBoard.reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoaderManager.getInstance(mActivity).clearMemory();
        mXhsEmoticonsKeyBoard.getAudioRecordButton().releaseResource();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_kf5_chat;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mXhsEmoticonsKeyBoard = (EmoticonsKeyBoard) findViewById(R.id.ek_bar);
        mListView = (ListView) findViewById(R.id.lv_chat);
        mListView.addHeaderView(LayoutInflater.from(mActivity).inflate(R.layout.kf5_list_view_footer_or_head_view, null));
        mImageView = (ImageView) findViewById(R.id.kf5_return_img);
        mImageView.setOnClickListener(this);
        mTextViewTitle = (TextView) findViewById(R.id.kf5_title);
        mTextViewRight = (TextView) findViewById(R.id.kf5_right_text_view);
        mTextViewRight.setOnClickListener(this);
        initData();
    }

    @Override
    public void onLoadFinished(Loader<IMPresenter> loader, IMPresenter data) {
        super.onLoadFinished(loader, data);

        showDialog = true;
        showLoading(null);
        dbMessageCount = presenter.getDBMessageCount();
        KF5SystemAPI.getInstance().systemInit(new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.containsKey(Field.CHAT_URL)) {
                        String url = jsonObject.getString(Field.CHAT_URL);
                        SPUtils.saveChatUrl(url);
                        presenter.connectIPC();
                    } else if (jsonObject.containsKey(Field.MESSAGE)) {
                        hideLoading();
                        showToast(jsonObject.getString(Field.MESSAGE));
                    } else {
                        hideLoading();
                        showToast(getString(R.string.kf5_unknown_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoading();
                    showToast(e.getMessage());
                }
            }

            @Override
            public void onFailure(String result) {
                hideLoading();
                showToast(result);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        try {
            if (presenter.isConnected())
                presenter.disconnect();
            presenter.disconnectIPC();
            IMSQLManager.reset(mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectIPCSuccess() {

        if (hasPermission(READ_PHONE_STATE_PERMISSION))
            bindConnect();
        else {
            applyPermissions(READ_PHONE_STATE_BY_CHAT_ACTIVITY, METHOD_REQUEST_PERMISSION, READ_PHONE_STATE_PERMISSION);
        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (EmoticonsKeyboardUtils.isFullScreen(this)) {
            return mXhsEmoticonsKeyBoard.dispatchKeyEventInFullScreen(event) || super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    private void initData() {
        initEmoticonsKeyBoardBar();
        initListView();
        mAdapter = new MessageAdapter(mActivity, mIMMessageList);
        mListView.setAdapter(mAdapter);
    }

    private void bindConnect() {

        refreshListAndNotifyData(presenter.getLastMessages(dbMessageCount));
        Bundle bundle = new Bundle();
        Map<String, String> map = new ArrayMap<>();
        map.put("appid", SPUtils.getAppid());
        map.put("platform", "Android");
        map.put("token", SPUtils.getUserToken());
        map.put("version", "2.0");
        map.put("uuid", Utils.getUUID(mActivity));
        bundle.putString("query", com.kf5.sdk.im.utils.Utils.getMapAppend(map));
        bundle.putString("url", SPUtils.getChatUrl());
        presenter.initParams(bundle);
        presenter.connect();
    }

    /**
     * 设置标题的可见性
     *
     * @param visibility
     */
    protected void setTitleVisibility(int visibility) {
        mTextViewTitle.setVisibility(visibility);
    }

    /**
     * 设置标题的内容
     *
     * @param text
     */
    protected void setTitleText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTextViewTitle != null && mTextViewTitle.isShown())
                    mTextViewTitle.setText(text);
            }
        });

    }


    /**
     * 通知更新
     */
    protected void refreshData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                scrollToBottom();
            }
        });
    }


    /**
     * @param message
     */
    public void removeMessage(IMMessage message) {
        mIMMessageList.remove(message);
    }


    /**
     * 取消排队
     */
    public void cancelQueueWaiting() {
        startActivity(new Intent(mActivity, FeedBackActivity.class));
        presenter.sendCancelQueue();
    }

    /**
     * 刷新数据
     *
     * @param messages
     */
    protected void refreshListAndNotifyData(List<IMMessage> messages) {

        mIMMessageList.addAll(messages);
        refreshData();
    }

    private void initListView() {
        mListView.setOnScrollListener(this);
    }

    private void initEmoticonsKeyBoardBar() {

        ExpressionCommonUtils.initEmoticonsEditText(mXhsEmoticonsKeyBoard.getETChat());
        mXhsEmoticonsKeyBoard.setAdapter(ExpressionCommonUtils.getCommonAdapter(this, ExpressionCommonUtils.getCommonEmoticonClickListener(mXhsEmoticonsKeyBoard.getETChat())));
        mXhsEmoticonsKeyBoard.addOnFuncKeyBoardListener(this);
        mXhsEmoticonsKeyBoard.getETChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });

        mXhsEmoticonsKeyBoard.getBtnSend().setOnClickListener(this);
        AppsView appsView = new AppsView(this);
        appsView.getTextViewCamera().setOnClickListener(this);
        appsView.getTextViewAlbum().setOnClickListener(this);
        mXhsEmoticonsKeyBoard.addFuncView(appsView);
        mXhsEmoticonsKeyBoard.getAISendView().setOnClickListener(this);
        mXhsEmoticonsKeyBoard.getAIToAgentBtnView().setOnClickListener(this);
        mXhsEmoticonsKeyBoard.getQueueSendView().setOnClickListener(this);
        mXhsEmoticonsKeyBoard.getAudioRecordButton().setAudioFinishRecorderListener(this);
        mXhsEmoticonsKeyBoard.getAudioRecordButton().setOnLongClickListener(this);
        mEditTextAI = mXhsEmoticonsKeyBoard.getAiEditText();
        mEditTextIM = mXhsEmoticonsKeyBoard.getETChat();
        mEditTextQueue = mXhsEmoticonsKeyBoard.getQueueEditText();
    }


    /**
     * 发送文本消息
     *
     * @param content
     */
    public void onSendTextMessage(String content) {
        IMMessage message = IMMessageManager.buildSendTextMessage(content);
        presenter.sendTextMessage(message);
        refreshListAndNotifyData(IMMessageManager.addIMMessageToList(message));
    }


    /**
     * 发送机器人消息
     *
     * @param content
     */
    public void onSendAITextMessage(String content) {
        IMMessage message = IMMessageManager.buildSendAIMessage(content);
        presenter.sendAIMessage(message);
        refreshListAndNotifyData(IMMessageManager.addIMMessageToList(message));
    }


    /**
     * 排队的queue消息
     *
     * @param content
     */
    public void onSendQueueTextMessage(String content) {
        IMMessage imMessage = IMMessageManager.buildSendTextMessage(content);
        presenter.sendQueueMessage(imMessage);
        refreshListAndNotifyData(IMMessageManager.addIMMessageToList(imMessage));
    }


    /**
     * 显示客服不在线对话框
     */
    protected void showNoAgentOnlineReminderDialog() {
        new DialogBox(mActivity)
                .setMessage(getString(R.string.kf5_no_agent_online_leaving_message))
                .setLeftButton(getString(R.string.kf5_cancel), null)
                .setRightButton(getString(R.string.kf5_leave_message), new DialogBox.onClickListener() {
                    @Override
                    public void onClick(DialogBox dialog) {
                        dialog.dismiss();
                        startActivity(new Intent(mActivity, FeedBackActivity.class));
                    }
                }).show();
    }


    /**
     * 发送图片消息
     *
     * @param sourceFileList 原图片集合
     */
    public void onSendImageMessage(final List<File> sourceFileList) {

        if (mImageCompressManager == null)
            mImageCompressManager = new ImageCompressManager();
        mImageCompressManager.load(sourceFileList)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        List<IMMessage> messageList = IMMessageManager.buildSendImageList(sourceFileList);
                        refreshListAndNotifyData(messageList);
                        for (int i = 0; i < sourceFileList.size(); i++) {
                            presenter.sendImageMessage(messageList.get(i), fileList.get(i));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    /**
     * 从机器人状态请求分配客服
     */
    public void aiToGetAgents() {
        mXhsEmoticonsKeyBoard.showAIViewToQueueView();
        presenter.getAgents(null, false);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mXhsEmoticonsKeyBoard.getBtnSend().getId()) {
            if (isAgentOnline) {
                onSendTextMessage(mEditTextIM.getText().toString());
                mEditTextIM.setText("");
            } else showNoAgentOnlineReminderDialog();
        } else if (id == R.id.kf5_return_img) {
            finish();
        } else if (id == R.id.kf5_textview_choice_from_camera) {
            if (hasPermission(CAMERA_PERMISSION)) {
                if (isAgentOnline)
                    takePictureFromCamera();
                else showNoAgentOnlineReminderDialog();
            } else applyPermissions(CAMERA_STATE, METHOD_REQUEST_PERMISSION, CAMERA_PERMISSION);
        } else if (id == R.id.kf5_textview_choice_from_image) {
            if (hasPermission(WRITE_EXTERNAL_STORAGE_PERMISSION)) {
                if (isAgentOnline) {
                    getPictureFromGallery(1);
                } else {
                    showNoAgentOnlineReminderDialog();
                }
            } else
                applyPermissions(WRITE_EXTERNAL_STORAGE, METHOD_REQUEST_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION);
        } else if (id == R.id.kf5_right_text_view) {
            startActivity(new Intent(mActivity, LookFeedBackActivity.class));
        } else if (id == R.id.kf5_queue_send_message) {
            onSendQueueTextMessage(mEditTextQueue.getText().toString());
            mEditTextQueue.setText("");
        } else if (id == R.id.kf5_ai_textview_send_message) {
            onSendAITextMessage(mEditTextAI.getText().toString());
            mEditTextAI.setText("");
        } else if (id == R.id.kf5_ai_to_agent_btn) {
            aiToGetAgents();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_FLING:
                Glide.with(mActivity).pauseRequests();
                break;
            case SCROLL_STATE_IDLE:
                try {
                    if (mListView.getFirstVisiblePosition() == 0) {
                        getMessageFromDBTime++;
                        View itemView = mListView.getChildAt(0);
                        if ((dbMessageCount - getMessageFromDBTime * 18) > -18) {
                            List<IMMessage> dbMessages = presenter.getLastMessages(dbMessageCount - getMessageFromDBTime * 18);
                            if (dbMessages.size() < 1) {
                                if (itemView != null && itemView.isShown())
                                    itemView.setVisibility(View.INVISIBLE);
                            } else {
                                if (itemView != null && !itemView.isShown())
                                    itemView.setVisibility(View.VISIBLE);
                                mIMMessageList.addAll(0, dbMessages);
                                mAdapter.notifyDataSetChanged();
                                mListView.setSelection(dbMessages.size());
                            }
                        } else {
                            if (itemView != null && itemView.isShown())
                                itemView.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                mXhsEmoticonsKeyBoard.reset();
                break;
            default:
                Glide.with(mActivity).resumeRequests();
                break;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onRecordFinished(float seconds, String filePath) {
        onSendVoiceMessage(filePath);
    }

    /**
     * 弹起
     *
     * @param height
     */
    @Override
    public void onFuncPop(int height) {
        scrollToBottom();
    }

    /**
     * 关闭
     */
    @Override
    public void onFuncHide() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case READ_PHONE_STATE_BY_CHAT_ACTIVITY:
                //绑定连接
                if (hasPermission(READ_PHONE_STATE_PERMISSION))
                    bindConnect();
                else
                    finish();
                break;
            case CAMERA_STATE:
                if (hasPermission(CAMERA_PERMISSION))
                    takePictureFromCamera();
                break;
            case WRITE_EXTERNAL_STORAGE:
                if (hasPermission(WRITE_EXTERNAL_STORAGE_PERMISSION))
                    getPictureFromGallery(1);
                break;
            default:
                if (resultCode == Activity.RESULT_OK) {
                    switch (requestCode) {
                        //相机
                        case CAMERA:
                            if (picFile != null) {
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri uri = Uri.fromFile(picFile);
                                intent.setData(uri);
                                sendBroadcast(intent);
                                onSendImageMessage(Collections.singletonList(picFile));
                            }
                            break;
                        //相册
                        case GALLERY:
                            if (data != null) {
                                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                                if (pathList != null) {
                                    List<File> files = new ArrayList<>();
                                    for (String path : pathList) {
                                        files.add(new File(path));
                                    }
                                    onSendImageMessage(files);
                                }
                            }
                            break;
                    }
                }
                break;
        }

    }

    private void scrollToBottom() {
        mListView.requestLayout();
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(mListView.getBottom());
            }
        });
    }


    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {

        int id = v.getId();
        if (id == R.id.kf5_btn_voice) {
            if (hasPermission(VOICE_RECORDER_PERMISSION)) {
                if (isAgentOnline) {
                    mXhsEmoticonsKeyBoard.getAudioRecordButton().prepareRecordAudio();
                    return true;
                } else {
                    showNoAgentOnlineReminderDialog();
                    return false;
                }
            } else {
                applyPermissions(VOICE_RECORDER, METHOD_REQUEST_PERMISSION, VOICE_RECORDER_PERMISSION);
                return false;
            }
        }
        return false;
    }


    /**
     * 发送语音文件
     *
     * @param filePath
     */
    public void onSendVoiceMessage(String filePath) {
        IMMessage message = IMMessageManager.buildSendVoiceMessage(filePath);
        presenter.sendVoiceMessage(message, new File(filePath));
        refreshListAndNotifyData(IMMessageManager.addIMMessageToList(message));
    }

}
