package com.kf5.sdk.im.keyboard.api;

import android.view.ViewGroup;

import com.kf5.sdk.im.keyboard.adapter.EmoticonsAdapter;

/**
 * author:chosen
 * date:2016/11/1 16:01
 * email:812219713@qq.com
 */

public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);

}
