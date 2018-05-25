package com.kf5.sdk.im.adapter.listener;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.MD5Utils;

import java.io.File;

/**
 * author:chosen
 * date:2016/10/26 15:14
 * email:812219713@qq.com
 */

public class MessageAdapterItemClickListener implements View.OnClickListener {

    private IMMessage imMessage;

//    private int position;

    private ImageView mImageView;

    private MediaPlayAnimCallback mCallback;

    public MessageAdapterItemClickListener(IMMessage object, int position, ImageView imageView, VoiceType voiceType) {
        super();
        this.imMessage = object;
//        this.position = position;
        this.mImageView = imageView;
        mCallback = new MediaPlayAnimCallback(voiceType);
        if (VoicePlayListener.getInstance().getTag() == object && object != null) {
            mCallback.onPrepared();
        }
    }

    @Override
    public void onClick(View v) {

        try {
            Upload upload = imMessage.getUpload();
            String url = upload.getUrl();
            File localFile = new File(FilePath.SAVE_RECORDER + MD5Utils.GetMD5Code(url) + ".amr");
            if (localFile.exists()) {
                VoicePlayListener.getInstance().startPlay(localFile.getAbsolutePath(), mCallback, imMessage);
            } else {
                File file = new File(FilePath.SAVE_RECORDER + upload.getName());
                if (file.exists()) {
                    VoicePlayListener.getInstance().startPlay(file.getAbsolutePath(), mCallback, imMessage);
                } else if (!TextUtils.isEmpty(url)) {
                    VoicePlayListener.getInstance().startPlay(url, mCallback, imMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MediaPlayAnimCallback lastAnimCallback;

    private final class MediaPlayAnimCallback implements VoicePlayListener.OnMediaPlayListener {

        final VoiceType voiceType;

        MediaPlayAnimCallback(VoiceType voiceType) {
            this.voiceType = voiceType;
        }

        @Override
        public void onPrepared() {
            if (lastAnimCallback != null) {
                lastAnimCallback.reset();
            }
            lastAnimCallback = this;
            if (voiceType == VoiceType.RIGHT) {
                mImageView.setImageResource(R.drawable.kf5_voice_play_right_drawable);
            } else {
                mImageView.setImageResource(R.drawable.kf5_voice_play_left_drawable);
            }
            AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getDrawable();
            animationDrawable.start();
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
            if (mImageView.getDrawable() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) mImageView.getDrawable();
                animationDrawable.stop();
            }
            if (voiceType == VoiceType.RIGHT) {
                mImageView.setImageResource(R.drawable.kf5_voice_play_right_src_3);
            } else {
                mImageView.setImageResource(R.drawable.kf5_voice_play_left_src_3);
            }
            lastAnimCallback = null;
        }
    }


    public enum VoiceType {
        LEFT, RIGHT
    }
}
