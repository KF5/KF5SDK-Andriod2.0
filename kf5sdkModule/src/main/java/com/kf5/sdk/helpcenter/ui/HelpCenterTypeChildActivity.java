package com.kf5.sdk.helpcenter.ui;


import com.kf5.sdk.R;
import com.kf5.sdk.system.entity.TitleBarProperty;

/**
 * 帮助中心之文档列表
 */
public class HelpCenterTypeChildActivity extends BaseHelpCenter {

    @Override
    protected HelpCenterType getHelpCenterType() {
        return HelpCenterType.Post;
    }

    @Override
    protected TitleBarProperty getTitleBarProperty() {
        return new TitleBarProperty.Builder()
                .setTitleContent(getResources().getString(R.string.kf5_article_list))
                .setRightViewVisible(true)
                .setRightViewClick(true)
                .setRightViewContent(getString(R.string.kf5_contact_us))
                .build();
    }
}
