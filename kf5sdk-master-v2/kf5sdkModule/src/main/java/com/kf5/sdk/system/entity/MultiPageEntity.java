package com.kf5.sdk.system.entity;

/**
 * author:chosen
 * date:2017/3/6 13:58
 * email:812219713@qq.com
 */

public class MultiPageEntity {

    private int count = -1;

    private int next_page = 1;

    private int previous_page = -1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNext_page() {
        return next_page;
    }

    public void setNext_page(int next_page) {
        this.next_page = next_page;
    }

    public int getPrevious_page() {
        return previous_page;
    }

    public void setPrevious_page(int previous_page) {
        this.previous_page = previous_page;
    }
}
