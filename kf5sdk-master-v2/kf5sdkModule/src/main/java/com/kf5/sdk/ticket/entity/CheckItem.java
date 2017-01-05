package com.kf5.sdk.ticket.entity;

/**
 * author:chosen
 * date:2017/1/4 15:24
 * email:812219713@qq.com
 */

public class CheckItem {

    private String content;
    private boolean selected;

    public CheckItem() {
    }

    public CheckItem(String content, boolean selected) {
        this.content = content;
        this.selected = selected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
