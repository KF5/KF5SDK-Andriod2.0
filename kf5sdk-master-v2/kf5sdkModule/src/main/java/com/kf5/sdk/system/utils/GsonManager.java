package com.kf5.sdk.system.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.entity.Field;

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

    private static final String CHAT = "chat";

    private static final String VISITOR = "visitor";

    public List<IMMessage> getIMMessageList(String jsonArrayString) {
        Type type = new TypeToken<ArrayList<IMMessage>>() {
        }.getType();
        List<IMMessage> list = sGson.fromJson(jsonArrayString, type);
        List<IMMessage> targetList = new ArrayList<>();
        for (IMMessage message : list) {
            String messageType = message.getType();
            String content = message.getMessage();
            if (!messageType.startsWith(CHAT) && !messageType.startsWith(VISITOR)) {
                targetList.add(message);
            } else if (TextUtils.equals(Field.CHAT_SYSTEM, messageType) && !TextUtils.isEmpty(content) && content.contains("已发送满意度评价")) {
                targetList.add(message);
            } else if (TextUtils.equals(Field.CHAT_MSG, messageType) && message.getId() <= 0 && !BaseChatActivity.robotEnable) {
                targetList.add(message);
            }
        }
        list.removeAll(targetList);
        return list;
    }
}
