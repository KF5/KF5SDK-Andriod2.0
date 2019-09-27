package com.chosen.album.listener;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author Chosen
 * @create 2019/1/9 16:30
 * @email 812219713@qq.com
 */
public interface OnSelectedListener {
    /**
     * @param uriList  the selected item {@link Uri} list.
     * @param pathList the selected item file path list.
     */
    void onSelected(@NonNull List<Uri> uriList, @NonNull List<String> pathList);
}
