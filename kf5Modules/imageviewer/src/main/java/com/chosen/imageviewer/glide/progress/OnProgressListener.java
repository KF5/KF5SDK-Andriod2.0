package com.chosen.imageviewer.glide.progress;

/**
 * @author Chosen
 * @create 2018/12/17 11:09
 * @email 812219713@qq.com
 */
public interface OnProgressListener {

    void onProgress(String url, boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
