package com.chosen.imageviewer.tool.file;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * @author Chosen
 * @create 2018/12/17 11:42
 * @email 812219713@qq.com
 */
public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private String path;
    private ScanListener listener;

    public SingleMediaScanner(Context context, String path, ScanListener l) {
        this.path = path;
        this.listener = l;
        this.mMs = new MediaScannerConnection(context, this);
        this.mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(path, null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
        if (listener != null) {
            listener.onScanFinish();
        }
    }

    public interface ScanListener {

        void onScanFinish();
    }
}
