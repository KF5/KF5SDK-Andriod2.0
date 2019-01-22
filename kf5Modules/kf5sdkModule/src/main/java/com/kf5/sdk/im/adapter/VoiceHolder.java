package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.VoicePlayListener;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.utils.MediaPlayerUtils;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.ToastUtil;
import com.kf5.sdk.system.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Chosen
 * @create 2019/1/8 11:58
 * @email 812219713@qq.com
 */
class VoiceHolder extends BaseArrowHolder {

    private TextView tvVoiceDuration;
    private LinearLayout voiceContainer;
    private AppCompatImageView imgPlayLevel;

    @Nullable
    private BaseSendHolder sendHolder;
    @Nullable
    private ProgressBar receiveProgressBar;

    VoiceHolder(MessageAdapter messageAdapter, View convertView) {
        super(messageAdapter, convertView);
        tvVoiceDuration = convertView.findViewById(R.id.kf5_message_item_with_voice);
        voiceContainer = convertView.findViewById(R.id.kf_message_voice_container);
        imgPlayLevel = convertView.findViewById(R.id.kf5_message_item_with_voice_play_img);
    }

    @Override
    void initData(int position, boolean isReceive) {
        super.initData(position, isReceive);
        if (!isReceive) {
            sendHolder = new BaseSendHolder(messageAdapter, convertView);
        } else {
            receiveProgressBar = convertView.findViewById(R.id.kf5_progressBar);
        }
    }

    @Override
    protected void setUpUI() {
        super.setUpUI();
        if (sendHolder != null) {
            sendHolder.setUpSendMessageUI(message, MessageType.VOICE, position);
        }
        setVoiceUI();
        imgPlayLevel.setImageResource(isReceive ? R.drawable.kf5_drawable_voice_play_left_3 : R.drawable.kf5_drawable_voice_play_right_3);
        voiceContainer.setOnClickListener(new VoiceClickListener());
    }

    private void setVoiceUI() {

        /**
         * 目前的语音文件走http下载，收到语音消息的时候，期间会有一个下载过程，所以收到的语音文件下载过程中
         * 有菊花显示，如果为收到的语音消息，则绑定到收到语音消息布局的菊花，否则则为发送布局的菊花。
         */
        View progressBar = null;
        if (isReceive) {
            progressBar = receiveProgressBar;
        } else {
            progressBar = sendHolder != null ? sendHolder.progressBar : null;
        }
        //根据发送状态设置UI
        //处理语音文件的显示
        Upload upload = message.getUpload();
        if (upload != null) {
            if (progressBar != null)
                progressBar.setVisibility(View.VISIBLE);
            if (upload.getVoiceDuration() > 0) {
                setVoiceDuration(progressBar, upload.getVoiceDuration());
            } else {
                String url = upload.getLocalPath();
                if (!TextUtils.isEmpty(url) && new File(url).exists()) {
                    int duration = MediaPlayerUtils.getLocalVoiceDuration(context,url);
                    setVoiceDuration(progressBar, duration);
                    upload.setVoiceDuration(duration);
                } else if (!TextUtils.isEmpty(upload.getUrl())) {
                    getDurationAsync(upload, progressBar, upload.getUrl());
                } else {
                    LogUtil.printf("语音文件本地和远程地址都为空");
                }
            }
        }
    }

    private void getDurationAsync(final Upload upload, final View progressBar, String url) {
        try {
            tvVoiceDuration.setText("");
            final MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int duration = mp.getDuration();
                    setVoiceDuration(progressBar, duration);
                    upload.setVoiceDuration(duration);
                    IMSQLManager.updateVoiceDurationByMessageId(context, message.getMessageId(), duration);
                    mp.release();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setVoiceDuration(View progressBar, int duration) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        voiceContainer.measure(w, h);
        int width = voiceContainer.getMeasuredWidth();
        int length = duration / 1000 + 1;
        tvVoiceDuration.setText(length + "''");
        double maxWidth = Utils.getDisplayWidth(context) / 3 * 2;
        double percentWidth = (maxWidth - width) / 60;
        int layoutWidth = ((int) (width + percentWidth * length));
        if (layoutWidth > Utils.dip2px(context, 48)) {
            ViewGroup.LayoutParams layoutParams = voiceContainer.getLayoutParams();
            layoutParams.width = (int) (width + percentWidth * length);
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            voiceContainer.setLayoutParams(layoutParams);
        }
        if (progressBar != null) {
            //特殊处理，如果正在转换，即使本地文件存在，依然显示菊花；由于引用了发送布局的progressbar，应该在以下三个发送状态中都显示progressbar
            Status sendStatus = message.getStatus();
            if (sendStatus == Status.SENDING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class VoiceClickListener implements View.OnClickListener {

        private MediaPlayAnimCallback mCallback;

        VoiceClickListener() {
            this.mCallback = new MediaPlayAnimCallback();
            if (VoicePlayListener.getInstance().getTag() == message && message != null) {
                mCallback.onPrepared();
            }
        }

        @Override
        public void onClick(View v) {
            try {
                if (message != null) {
                    Upload upload = message.getUpload();
                    if (upload != null) {
                        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        if (audioManager != null) {
                            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                            if (currentVolume == 0) {
                                ToastUtil.showToast(context, "音量太小，请调整音量");
                            }
                        }

                        String localPath = upload.getLocalPath();
                        if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
                            VoicePlayListener.getInstance().startPlay(localPath, mCallback, message);
                        } else if (!TextUtils.isEmpty(upload.getUrl())) {
                            VoicePlayListener.getInstance().startPlay(upload.getUrl(), mCallback, message);
                        } else {
                            Toast.makeText(context, "录音文件不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private static MediaPlayAnimCallback lastAnimCallback;


    private final class MediaPlayAnimCallback implements VoicePlayListener.OnMediaPlayListener {

        @Override
        public void onPrepared() {
            if (lastAnimCallback != null) {
                lastAnimCallback.reset();
            }
            lastAnimCallback = this;
            imgPlayLevel.setImageResource(isReceive ? R.drawable.kf5_drawable_voice_play_left : R.drawable.kf5_drawable_voice_play_right);
            if (imgPlayLevel.getDrawable() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) imgPlayLevel.getDrawable();
                animationDrawable.start();
            }
        }

        @Override
        public void onCompletion() {
            reset();
        }

        @Override
        public void onRelease() {
            reset();
        }

        private void reset() {
            if (imgPlayLevel.getDrawable() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = ((AnimationDrawable) imgPlayLevel.getDrawable());
                animationDrawable.stop();
            }
            imgPlayLevel.setImageResource(isReceive ? R.drawable.kf5_drawable_voice_play_left_3 : R.drawable.kf5_drawable_voice_play_right_3);
            lastAnimCallback = null;
        }
    }

}
