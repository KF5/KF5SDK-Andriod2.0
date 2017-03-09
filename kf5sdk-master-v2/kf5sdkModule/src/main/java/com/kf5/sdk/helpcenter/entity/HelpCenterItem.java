package com.kf5.sdk.helpcenter.entity;

/**
 * author:chosen
 * date:2016/10/19 10:52
 * email:812219713@qq.com
 */

public class HelpCenterItem {

    private int id;

    private String title;

    private String content;

    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
