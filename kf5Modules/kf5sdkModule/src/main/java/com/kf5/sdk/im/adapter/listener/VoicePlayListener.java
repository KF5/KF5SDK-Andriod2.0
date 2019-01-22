package com.kf5.sdk.im.adapter.listener;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * author:chosen
 * date:2016/10/26 15:14
 * email:812219713@qq.com
 */

public class VoicePlayListener implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {


    private MediaPlayer mediaPlayer;

    private static VoicePlayListener voicePlayListener;

    private Object tag;

    private VoicePlayListener() {
    }

    public static VoicePlayListener getInstance() {

        if (voicePlayListener == null) {
            synchronized (VoicePlayListener.class) {
                if (voicePlayListener == null) {
                    voicePlayListener = new VoicePlayListener();
                }
            }
        }
        return voicePlayListener;
    }


    public void startPlay(String path, OnMediaPlayListener mediaPlayListener, Object tag) {

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        }
        mMediaPlayListener = mediaPlayListener;
        this.tag = tag;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        if (mMediaPlayListener != null) {
            mMediaPlayListener.onPrepared();
        }
    }

    public void onDestroy() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            tag = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            if (mMediaPlayListener != null) {
                mMediaPlayListener.onRelease();
            }
            tag = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getTag() {
        return tag;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        tag = null;
        if (mMediaPlayListener != null) {
            mMediaPlayListener.onCompletion();
        }
    }

    private OnMediaPlayListener mMediaPlayListener;

    public interface OnMediaPlayListener {

        void onPrepared();

        void onCompletion();

        void onRelease();
    }
}

