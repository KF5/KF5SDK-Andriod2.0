package com.chosen.album.internal.entity;

/**
 * @author Chosen
 * @create 2019/1/9 16:31
 * @email 812219713@qq.com
 */
public class CaptureStrategy {

    public final boolean isPublic;
    public final String authority;
    public final String directory;

    public CaptureStrategy(boolean isPublic, String authority) {
        this(isPublic, authority, null);
    }

    public CaptureStrategy(boolean isPublic, String authority, String directory) {
        this.isPublic = isPublic;
        this.authority = authority;
        this.directory = directory;
    }

}
