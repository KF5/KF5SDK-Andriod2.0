package com.chosen.videoplayer;

import android.view.Surface;

/**
 * @author Chosen
 * @create 2018/11/26 14:33
 * @email 812219713@qq.com
 */
public abstract class JZMediaInterface {

    public JZDataSource jzDataSource;

    public abstract void start();

    public abstract void prepare();

    public abstract void pause();

    public abstract boolean isPlaying();

    public abstract void seekTo(long time);

    public abstract void release();

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract void setSurface(Surface surface);

    public abstract void setVolume(float leftVolume, float rightVolume);

    public abstract void setSpeed(float speed);
}
