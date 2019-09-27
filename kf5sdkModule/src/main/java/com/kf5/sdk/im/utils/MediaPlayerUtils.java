package com.kf5.sdk.im.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * @author Chosen
 * @create 2019/1/15 15:14
 * @email 812219713@qq.com
 */
public class MediaPlayerUtils {

    public static int getLocalVoiceDuration(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) {
            return 0;
        }
        MediaPlayer mediaPlayer = null;
        if (new File(url).exists()) {
            try {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(url));
                return mediaPlayer.getDuration();
            } finally {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
            }
        }
        return 0;
    }
}
