package com.kf5.sdk.helpcenter.entity;

import com.kf5.sdk.system.entity.MultiPageEntity;

import java.util.List;

/**
 * author:chosen
 * date:2017/3/6 14:34
 * email:812219713@qq.com
 */

public class HelpCenterItemPost extends MultiPageEntity {

    private List<HelpCenterItem> posts;

    public List<HelpCenterItem> getPosts() {
        return posts;
    }

    public void setPosts(List<HelpCenterItem> posts) {
        this.posts = posts;
    }
}
