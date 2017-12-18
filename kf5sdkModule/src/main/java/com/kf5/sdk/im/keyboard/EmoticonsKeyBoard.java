package com.kf5.sdk.im.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.keyboard.adapter.PageSetAdapter;
import com.kf5.sdk.im.keyboard.data.PageSetEntity;
import com.kf5.sdk.im.keyboard.utils.EmoticonsKeyboardUtils;
import com.kf5.sdk.im.keyboard.utils.ViewUtils;
import com.kf5.sdk.im.keyboard.widgets.AIView;
import com.kf5.sdk.im.keyboard.widgets.AutoHeightLayout;
import com.kf5.sdk.im.keyboard.widgets.EmoticonsEditText;
import com.kf5.sdk.im.keyboard.widgets.EmoticonsFuncView;
import com.kf5.sdk.im.keyboard.widgets.EmoticonsIndicatorView;
import com.kf5.sdk.im.keyboard.widgets.EmoticonsToolBarView;
import com.kf5.sdk.im.keyboard.widgets.FuncLayout;
import com.kf5.sdk.im.keyboard.widgets.IMView;
import com.kf5.sdk.im.keyboard.widgets.QueueView;
import com.kf5.sdk.im.widget.AudioRecordButton;

import java.util.ArrayList;

/**
 * author:chosen
 * date:2016/10/31 15:09
 * email:812219713@qq.com
 */

public class EmoticonsKeyBoard extends AutoHeightLayout implements EmoticonsFuncView.OnEmoticonsPageViewListener,
        EmoticonsToolBarView.OnToolBarItemClickListener, EmoticonsEditText.OnBackKeyClickListener, FuncLayout.OnFuncChangeListener, IMView.IMViewListener {

    public static final int FUNC_TYPE_EMOTION = -1;

    public static final int FUNC_TYPE_APPS = -2;

    private LayoutInflater mInflater;

    private FuncLayout mLayoutKVML;

    private EmoticonsFuncView mEmoticonsFuncView;

    private EmoticonsIndicatorView mEmoticonsIndicatorView;

    private EmoticonsToolBarView mEmoticonsToolBarView;

    private boolean mDispatchKeyEventPreImeLock = false;

    private QueueView mTemporaryMessageLayout;

    private AIView mAILayout;

    private IMView mIMLayout;


    public EmoticonsKeyBoard(Context context) {
        this(context, null);
    }

    public EmoticonsKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.kf5_view_keyboard_xhs, this);
        initView();
        initFuncView();
    }

    private void initView() {

        mLayoutKVML = (FuncLayout) findViewById(R.id.kf5_ly_kvml);
        mAILayout = (AIView) findViewById(R.id.kf5_ai_layout);
        mTemporaryMessageLayout = (QueueView) findViewById(R.id.kf5_queue_layout);
        mIMLayout = (IMView) findViewById(R.id.kf5_im_layout);
        mIMLayout.getETChat().setOnBackKeyClickListener(this);
        mIMLayout.setIMViewListener(this);
    }


    private void initFuncView() {
        initEmoticonFuncView();
    }

    private View inflateFunc() {
        return mInflater.inflate(R.layout.kf5_fun_emoticon, null);
    }


    private void initEmoticonFuncView() {

        View keyboardView = inflateFunc();
        mLayoutKVML.addFuncView(FUNC_TYPE_EMOTION, keyboardView);
        mEmoticonsFuncView = (EmoticonsFuncView) findViewById(R.id.kf5_view_efv);
        mEmoticonsIndicatorView = (EmoticonsIndicatorView) findViewById(R.id.kf5_view_eiv);
        mEmoticonsToolBarView = (EmoticonsToolBarView) findViewById(R.id.kf5_view_etv);
        mEmoticonsFuncView.setOnIndicatorListener(this);
        mEmoticonsToolBarView.setOnToolBarItemClickListener(this);
        mLayoutKVML.setOnFuncChangeListener(this);
    }

    /**
     * 显示机器人View
     */
    public void showAIView() {
        reset();
//        ViewUtils.toggleTargetViewVisible(mAILayout, mIMLayout, mTemporaryMessageLayout);
        ViewUtils.toggleTargetViewVisible(mAILayout, mIMLayout, mTemporaryMessageLayout);
    }

    /**
     * 显示发送临时消息的view
     */
    public void showTemporaryMessageView() {
        reset();
        ViewUtils.toggleTargetViewVisible(mTemporaryMessageLayout, mIMLayout, mAILayout);

    }

    /**
     * 显示IMView
     */
    public void showIMView() {
//        ViewUtils.toggleTargetViewVisible(mIMLayout, mAILayout, mTemporaryMessageLayout);
        ViewUtils.toggleTargetViewVisible(mIMLayout, mAILayout, mTemporaryMessageLayout);
//        String text = mTemporaryMessageLayout.getEditText().getText().toString();
//        mIMLayout.toggleText(text);
//        if (!TextUtils.isEmpty(text)) {
//            mTemporaryMessageLayout.getEditText().setText("");
//        }
    }


    public void setAdapter(PageSetAdapter pageSetAdapter) {
        if (pageSetAdapter != null) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null)
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    mEmoticonsToolBarView.addToolItemView(pageSetEntity);
                }
        }
        mEmoticonsFuncView.setAdapter(pageSetAdapter);
    }

    public void addFuncView(View view) {
        mLayoutKVML.addFuncView(FUNC_TYPE_APPS, view);
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(this);
        mLayoutKVML.hideAllFuncView();
//        mBtnFace.setImageResource(R.drawable.kf5_chat_by_emoji);
//        mRlInput.setBackgroundResource(R.drawable.kf5_input_bg_gray);
        mIMLayout.getLayoutInput().setBackgroundResource(R.drawable.kf5_input_bg_gray);
    }

    private void toggleFuncView(int key) {
        mLayoutKVML.toggleFuncView(key, isSoftKeyboardPop(), mAILayout.getEmojiconEditText());
    }

    private void setFuncViewHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLayoutKVML.getLayoutParams();
        params.height = height;
        mLayoutKVML.setLayoutParams(params);
    }

    public void addOnFuncKeyBoardListener(FuncLayout.OnFuncKeyBoardListener listener) {
        mLayoutKVML.addOnKeyBoardListener(listener);
    }


    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        mLayoutKVML.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        mLayoutKVML.setVisibility(true);
        onFuncChange(mLayoutKVML.DEF_KEY);

        mIMLayout.getLayoutInput().setBackgroundResource(R.drawable.kf5_input_bg_green);
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        mIMLayout.getLayoutInput().setBackgroundResource(R.drawable.kf5_input_bg_gray);
        if (mLayoutKVML.isOnlyShowSoftKeyboard())
            reset();
        else
            onFuncChange(mLayoutKVML.getCurrentFuncKey());
    }

    @Override
    public void onBackKeyClick() {
        if (mLayoutKVML.isShown()) {
            mDispatchKeyEventPreImeLock = true;
            reset();
        }
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        mEmoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }

    /**
     * 相对于当前表情集的位置
     *
     * @param position
     * @param pageSetEntity
     */
    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    /**
     * @param oldPosition   相对于当前表情集的起点位置
     * @param newPosition   相对于当前表情集的重点位置
     * @param pageSetEntity
     */
    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        mEmoticonsFuncView.setCurrentPageSet(pageSetEntity);
    }

    @Override
    public void onFuncChange(int key) {
        if (FUNC_TYPE_EMOTION == key) {
//            mBtnFace.setImageResource(R.drawable.kf5_btn_send_pressed_bg);
        } else {
//            mBtnFace.setImageResource(R.drawable.kf5_btn_send_normal_bg);
        }
//        checkVoice();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (mLayoutKVML.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()))
            return false;
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()))
            return;
        super.requestChildFocus(child, focused);
    }

    @Override
    public void onReset() {
        reset();
    }

    @Override
    public void onToggleFuncView(int key) {
        toggleFuncView(key);
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {

        if (event == null) return false;
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()) && mLayoutKVML.isShown()) {
                    reset();
                    return true;
                }
            default:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    boolean isFocused;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        isFocused = mIMLayout.getETChat().getShowSoftInputOnFocus();
                    } else {
                        isFocused = mIMLayout.getETChat().isFocused();
                    }
                    if (isFocused)
                        mIMLayout.getETChat().onKeyDown(event.getKeyCode(), event);
                }
        }
        return false;
    }

    public EmoticonsEditText getETChat() {
        return mIMLayout.getETChat();
    }

    public Button getBtnVoice() {
        return mIMLayout.getButtonVoice();
    }

    public TextView getBtnSend() {
        return mIMLayout.getTVSend();
    }

    public AudioRecordButton getAudioRecordButton() {
        return mIMLayout.getButtonVoice();
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return mEmoticonsFuncView;
    }

    public EmoticonsIndicatorView getEmoticonsIndicatorView() {
        return mEmoticonsIndicatorView;
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return mEmoticonsToolBarView;
    }

    public EditText getAiEditText() {
        return mAILayout.getEmojiconEditText();
    }

    public TextView getAISendView() {
        return mAILayout.getTextViewSend();
    }

    public TextView getAIToAgentBtnView() {
        return mAILayout.getTextViewAIToAgent();
    }

    public EditText getTemporaryMessageEditText() {
        return mTemporaryMessageLayout.getEditText();
    }

    public TextView getTemporaryMessageView() {
        return mTemporaryMessageLayout.getTextViewSend();
    }


    public AIView getAILayout() {
        return mAILayout;
    }

    public IMView getIMLayout() {
        return mIMLayout;
    }

//    public QueueView getQueueLayout() {
//        return mTemporaryMessageLayout;
//    }
}
