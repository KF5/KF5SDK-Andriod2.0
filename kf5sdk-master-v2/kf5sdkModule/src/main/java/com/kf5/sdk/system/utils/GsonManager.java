package com.kf5.sdk.system.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;

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

    public Chat buildChat(String jsonString) {
        return sGson.fromJson(jsonString, Chat.class);
    }

    public Agent buildAgent(String jsonString) {
        return sGson.fromJson(jsonString, Agent.class);
    }

    public List<IMMessage> getIMMessageList(String jsonArrayString) {
        Type type = new TypeToken<ArrayList<IMMessage>>() {
        }.getType();
        return sGson.fromJson(jsonArrayString, type);
    }
}
