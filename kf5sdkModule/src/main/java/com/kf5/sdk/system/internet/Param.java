package com.kf5.sdk.system.internet;

/**
 * author:chosen
 * date:2016/10/14 17:36
 * email:812219713@qq.com
 */

class Param {

    public String key;

    public String value;

    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
