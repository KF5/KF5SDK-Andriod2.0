package com.kf5.sdk.im.keyboard.api;

import android.view.View;
import android.view.ViewGroup;

import com.kf5.sdk.im.keyboard.data.PageEntity;

/**
 * author:chosen
 * date:2016/10/31 15:42
 * email:812219713@qq.com
 */

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
