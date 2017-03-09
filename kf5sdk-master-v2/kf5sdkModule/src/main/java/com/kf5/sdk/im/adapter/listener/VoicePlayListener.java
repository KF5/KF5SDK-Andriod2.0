package com.kf5.sdk.im.adapter.listener;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * author:chosen
 * date:2016/10/26 15:14
 * email:812219713@qq.com
 */

public class VoicePlayListener implements MediaPlayer.OnPreparedListener {


    private MediaPlayer mediaPlayer;


    private static VoicePlayListener voicePlayListener;

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


    public void startPlay(String path) {

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
        }

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
    }

    public void onDestroy() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

