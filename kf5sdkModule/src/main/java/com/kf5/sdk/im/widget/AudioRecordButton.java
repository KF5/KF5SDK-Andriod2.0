package com.kf5.sdk.im.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.kf5.sdk.R;
import com.kf5.sdk.im.utils.AudioManager;
import com.kf5.sdk.system.utils.FilePath;

/**
 * author:chosen
 * date:2016/10/17 15:12
 * email:812219713@qq.com
 */

public final class AudioRecordButton extends AppCompatButton implements AudioManager.AudioStageListener {

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;


    private static final int DISTANCE_Y_CANCEL = 50;

    private int mCurrentState = STATE_NORMAL;
    // 已经开始录音
    private boolean isRecording = false;

    private boolean isWantToCancel = false;

    private DialogManager mDialogManager;

    private AudioManager mAudioManager;

    private float mTime = 0;
    // 是否触发了onlongclick，准备好了
    private boolean mReady;

    /**
     * 先实现两个参数的构造方法，布局会默认引用这个构造方法， 用一个 构造参数的构造方法来引用这个方法 * @param context
     */

    public AudioRecordButton(Context context) {
        this(context, null);
    }

    public AudioRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(getContext());

        // 这里没有判断储存卡是否存在，有空要判断
        String dir = FilePath.SAVE_RECORDER;
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStageListener(this);
    }

    public void prepareRecordAudio() {
        mReady = true;
        if (mAudioManager.mListener == null) {
            mAudioManager.setOnAudioStageListener(this);
        }
        mAudioManager.prepareAudio();
    }

    /**
     * 录音完成后的回调，回调给activity，可以获得mtime和文件的路径
     *
     * @author nickming
     */
    public interface AudioFinishRecorderListener {

        void onRecordFinished(float seconds, String filePath);
    }

    private AudioFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mListener = listener;
    }

    // 获取音量大小的runnable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mhandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    // 准备三个常量
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int MSG_DIALOG_DIMISS = 0X112;

    private Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示应该是在audio end prepare之后回调
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunnable).start();

                    // 需要开启一个线程来变换音量
                    break;
                case MSG_VOICE_CHANGE:

                    int restTime = (int) (60 - mTime);
                    if (!isWantToCancel) {
                        mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                        if (restTime < 10) {
                            mDialogManager.recording(new StringBuffer().append("还可以说").append((int) (60 - mTime)).append("s").toString());
                        }
                    }
                    if (restTime == 0) {

                        if (mCurrentState == STATE_RECORDING) {

                            mDialogManager.dimissDialog();

                            mAudioManager.release();// release释放一个mediarecorder

                            if (mListener != null) {// 并且callbackActivity，保存录音

                                mListener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath());
                            }
                        } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                            // cancel
                            mAudioManager.cancel();
                            mDialogManager.dimissDialog();
                        }
                        reset();// 恢复标志位

                    }


                    break;
                case MSG_DIALOG_DIMISS:

                    mDialogManager.dimissDialog();

                    break;

            }
        }

        ;
    };

    // 在这里面发送一个handler的消息
    @Override
    public void wellPrepared() {
        mhandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    /**
     * 直接复写这个监听函数
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {
                    // 根据x，y来判断用户是否想要取消
                    if (wantToCancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }

                }

                break;
            case MotionEvent.ACTION_UP:
                // 首先判断是否有触发onlongclick事件，没有的话直接返回reset
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                // 如果按的时间太短，还没准备好或者时间录制太短，就离开了，则显示这个dialog
                if (!isRecording || mTime < 0.99999f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mhandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 500);// 持续500ms
                } else if (mCurrentState == STATE_RECORDING) {//正常录制结束

                    mDialogManager.dimissDialog();

                    mAudioManager.release();// release释放一个mediarecorder

                    if (mListener != null) {// 并且callbackActivity，保存录音

                        mListener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath());
                    }

                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    // cancel
                    mAudioManager.cancel();
                    mDialogManager.dimissDialog();
                }
                reset();// 恢复标志位

                break;

        }

        return super.onTouchEvent(event);
    }

    /**
     * 回复标志位以及状态
     */
    private void reset() {
        // TODO Auto-generated method stub
        isWantToCancel = false;
        isRecording = false;
        changeState(STATE_NORMAL);
        mReady = false;
        mTime = 0;
    }

    private boolean wantToCancel(int x, int y) {
        // TODO Auto-generated method stub

        if (x < 0 || x > getWidth()) {// 判断是否在左边，右边，上边，下边
            isWantToCancel = true;
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            isWantToCancel = true;
            return true;
        }
        isWantToCancel = false;
        return false;
    }

    public void releaseResource() {
        if (mAudioManager != null) {
            mAudioManager.resetAudioManager();
        }
    }

    private void changeState(int state) {

        if (mCurrentState != state) {
            mCurrentState = state;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.kf5_button_record_normal);
                    setText(R.string.kf5_hold_to_speak);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.kf5_button_recording);
                    setText(R.string.kf5_release_to_cancel);
                    if (isRecording) {
                        mDialogManager.recording("");
                        // 复写dialog.recording();
                    }
                    break;

                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.kf5_button_recording);
                    setText(R.string.kf5_leave_to_cancel);
                    // dialog want to cancel
                    mDialogManager.wantToCancel();
                    break;

            }
        }

    }

    @Override
    public boolean onPreDraw() {
        // TODO Auto-generated method stub
        return true;
    }
}
