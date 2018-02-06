package com.kf5.sdk.system.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kf5.sdk.im.entity.Agent;
import com.kf5.sdk.im.entity.Chat;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.SelectAgentGroupItem;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.entity.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        setSyncContent(list);
        return list;
    }

    public List<SelectAgentGroupItem> getSelectAgentGroupItemList(JSONArray optionsArray) throws JSONException {
        List<SelectAgentGroupItem> items = new ArrayList<>();
        for (int i = 0; i < optionsArray.length(); i++) {
            JSONObject itemObj = optionsArray.getJSONObject(i);
            SelectAgentGroupItem item = new SelectAgentGroupItem();
            item.setTitle(SafeJson.safeGet(itemObj, Field.TITLE));
            item.setDescription(SafeJson.safeGet(itemObj, Field.DESCRIPTION));
            item.setGroupId(SafeJson.safeInt(itemObj, Field.GROUP_ID));
            item.setAgentIds(SafeJson.safeArray(itemObj, Field.AGENT_IDS).toString());
            items.add(item);
        }
        return items;
    }


    private static void setSyncContent(List<IMMessage> list) {
        for (IMMessage message : list) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Field.TIMESTAMP, message.getTimeStamp());
                jsonObject.put(Field.ID, message.getId());
                String type = message.getType();
                if (TextUtils.equals(Field.CHAT_DOCUMENT, type)) {
                    jsonObject.put(Field.CONTENT, "机器人客服为您找到以下内容：");
                    jsonObject.put(Field.TYPE, Field.DOCUMENT);
                    JSONArray jsonArray = new JSONArray(message.getMessage());
                    jsonObject.put(Field.DOCUMENTS, jsonArray);
                    message.setMessage(jsonObject.toString());
                } else if (TextUtils.equals(Field.CHAT_QUESTION, type)) {
                    jsonObject.put(Field.CONTENT, "机器人客服为您找到以下内容：");
                    jsonObject.put(Field.TYPE, Field.QUESTION);
                    JSONArray jsonArray = new JSONArray(message.getMessage());
                    jsonObject.put(Field.QUESTIONS, jsonArray);
                    message.setMessage(jsonObject.toString());
                } else if (TextUtils.equals(Field.CHAT_ANSWER, type)) {
                    jsonObject.put(Field.CONTENT, message.getMessage());
                    jsonObject.put(Field.TYPE, Field.ANSWER);
                    message.setMessage(jsonObject.toString());
                } else if (TextUtils.equals(Field.CHAT_MSG, type) && TextUtils.equals(Field.ROBOT, message.getRole())) {
                    jsonObject.put(Field.CONTENT, message.getMessage());
                    jsonObject.put(Field.TYPE, Field.CHAT_MSG);
                    message.setMessage(jsonObject.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}