package com.kf5.sdk.helpcenter.mvp.view;

import com.kf5.sdk.helpcenter.entity.Post;
import com.kf5.sdk.system.mvp.view.MvpView;

/**
 * author:chosen
 * date:2016/10/19 16:20
 * email:812219713@qq.com
 */

public interface IHelpCenterDetailView extends MvpView {

    void onLoadResult(Post post);

    int getPostId();
}
