package com.kf5.sdk.im.entity;

import com.google.gson.annotations.SerializedName;

/**
 * author:chosen
 * date:2016/11/3 17:08
 * email:812219713@qq.com
 */

public class Chat {

    @SerializedName("current_agent")
    private Agent mAgent;
    @SerializedName("chatting")
    private boolean chatting;
    @SerializedName("robot_name")
    private String robotName;
    @SerializedName("robot_photo")
    private String robotPhoto;
    @SerializedName("status")
    private String status;
    @SerializedName("welcome_msg")
    private String welcomeMsg;
    @SerializedName("robot_enable")
    private boolean robotEnable;
    @SerializedName("version")
    private int version;

    private TimeOut timeout;

    public TimeOut getTimeout() {
        return timeout;
    }

    public void setTimeout(TimeOut timeout) {
        this.timeout = timeout;
    }

    public Agent getAgent() {
        return mAgent;
    }

    public void setAgent(Agent agent) {
        mAgent = agent;
    }

    public boolean isChatting() {
        return chatting;
    }

    public void setChatting(boolean chatting) {
        this.chatting = chatting;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotPhoto() {
        return robotPhoto;
    }

    public void setRobotPhoto(String robotPhoto) {
        this.robotPhoto = robotPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWelcomeMsg() {
        return welcomeMsg;
    }

    public void setWelcomeMsg(String welcomeMsg) {
        this.welcomeMsg = welcomeMsg;
    }

    public boolean isRobotEnable() {
        return robotEnable;
    }

    public void setRobotEnable(boolean robotEnable) {
        this.robotEnable = robotEnable;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
