package com.kf5.sdk.system.entity;

/**
 * author:chosen
 * date:2016/10/14 18:03
 * email:812219713@qq.com
 */

public class KF5User {

    public String userAgent;

    public String appid;

    public String email;

    public String phone;

    public String helpAddress;

    public String userToken;

    public String userName;

    public String sdkName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSdkName() {
        return sdkName;
    }

    public void setSdkName(String sdkName) {
        this.sdkName = sdkName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getHelpAddress() {
        return helpAddress;
    }

    public void setHelpAddress(String helpAddress) {
        this.helpAddress = helpAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
