package com.kf5.sdk.im.entity;

/**
 * author:chosen
 * date:2017/3/7 16:46
 * email:812219713@qq.com
 */

public class TimeOut {

    private boolean enable;

    private String msg;

    private int seconds;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
