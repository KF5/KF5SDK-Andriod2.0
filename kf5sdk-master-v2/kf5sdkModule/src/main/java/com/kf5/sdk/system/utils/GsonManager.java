package com.kf5.sdk.system.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kf5.sdk.helpcenter.entity.Attachment;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.ticket.entity.Comment;
import com.kf5.sdk.ticket.entity.Requester;
import com.kf5.sdk.ticket.entity.UserField;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/18 17:16
 * email:812219713@qq.com
 */

public class GsonManager {

    private GsonManager() {

    }

    private static GsonManager sGsonManager;

    private static Gson sGson;

    public static GsonManager getInstance() {
        if (sGsonManager == null) {
            synchronized (GsonManager.class) {
                if (sGsonManager == null) {
                    sGsonManager = new GsonManager();
                    sGson = new Gson();
                }
            }
        }
        return sGsonManager;
    }

    public <T> T buildEntity(Class<T> t, String jsonString) {
        return sGson.fromJson(jsonString, t);
    }

    public <T> List<T> buildListEntity(Type type, String jsonArrayString) {
        return sGson.fromJson(jsonArrayString, type);
    }

    /**
     * 解析帮助中心集合
     *
     * @param jsonArrayString
     * @return
     */
    public List<HelpCenterItem> getHelpCenterItemList(String jsonArrayString) {

        Type type = new TypeToken<ArrayList<HelpCenterItem>>() {
        }.getType();
        return sGson.fromJson(jsonArrayString, type);
    }

    /**
     * 工单列表
     *
     * @param jsonArrayString
     * @return
     */
    public List<Requester> getRequesterList(String jsonArrayString) {
        Type type = new TypeToken<ArrayList<Requester>>() {
        }.getType();
        return sGson.fromJson(jsonArrayString, type);
    }


    public List<UserField> getUserFieldList(String jsonArrayString) {
        Type type = new TypeToken<ArrayList<UserField>>() {
        }.getType();
        return sGson.fromJson(jsonArrayString, type);
    }

    public List<Attachment> getAttachmentList(String jsonArrayString) {
        Type type = new TypeToken<ArrayList<Attachment>>() {
        }.getType();
        return sGson.fromJson(jsonArrayString, type);
    }

    public Comment buildComment(String jsonString) {
        return sGson.fromJson(jsonString, Comment.class);
    }

    public Chat buildChat(String jsonString) {
        return sGson.fromJson(jsonString, Chat.class);
    }


    public Agent buildAgent(String jsonString) {
        return sGson.fromJson(jsonString, Agent.class);
    }

    public Requester buildRequester(String jsonString) {
        return sGson.fromJson(jsonString, Requester.class);
    }

    public List<IMMessage> getIMMessageList(String jsonArrayString) {
        Type type = new TypeToken<ArrayList<IMMessage>>() {
        }.getType();
        return sGson.fromJson(jsonArrayString, type);
    }
}
