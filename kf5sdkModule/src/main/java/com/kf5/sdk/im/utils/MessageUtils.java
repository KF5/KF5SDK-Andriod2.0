package com.kf5.sdk.im.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.util.Linkify;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.clickspan.CustomLinkMovementMethod;
import com.kf5.sdk.im.entity.CustomField;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.SafeJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author:chosen
 * date:2017/3/29 15:31
 * email:812219713@qq.com
 */

public class MessageUtils {

    private static final String TAG_SPACE = "&#160";
    private static final String TAG_SOLID_CIRCLE = "●";
    private static final String TAG_PREFIX = TAG_SOLID_CIRCLE + TAG_SPACE + TAG_SPACE;
    private static final String TAG_BR = "<br/>";
    private static final String RecommendedCategories = "recommended categories";
    private static final String HotCategories = "hot categories";


    public static final String HREF_PREFIX_QUESTION = "chosenQuestionTo://";
    public static final String HREF_PREFIX_DOCUMENT = "chosenDocumentTo://";
    public static final String HREF_PREFIX_CUSTOM = "chosenVideoChatTo://";
    public static final String HREF_PREFIX_CATEGORY = "chosenCategoryTo://";

    private MessageUtils() {

    }


    public static String makeCustomMessageContent(Context context, String message) {
        try {
            JSONObject jsonObject = SafeJson.parseObj(message);
            if (SafeJson.isContainKey(jsonObject, CustomField.TYPE)) {
                String type = SafeJson.safeGet(jsonObject, CustomField.TYPE);
                if (TextUtils.equals(CustomField.VIDEO, type)) {
                    if (SafeJson.isContainKey(jsonObject, CustomField.VISITOR_URL)) {
                        String url = SafeJson.safeGet(jsonObject, CustomField.VISITOR_URL);
                        return "<a href=\""
                                + HREF_PREFIX_CUSTOM
                                + url
                                + "\">"
                                + context.getString(R.string.kf5_invite_video_chat)
                                + "</a>";
                    }
                } else if (TextUtils.equals(CustomField.CATEGORIES, type)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (jsonObject.has(Field.TITLE)) {
                        String title = jsonObject.getString(Field.TITLE);
                        if (TextUtils.equals(RecommendedCategories, title)) {
                            stringBuilder.append(context.getString(R.string.kf5_recommended_categories));
                        } else if (TextUtils.equals(HotCategories, title)) {
                            stringBuilder.append(context.getString(R.string.kf5_hot_categories));
                        }
                    }
                    dealCategories(jsonObject, stringBuilder);
                    return stringBuilder.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    /**
     * 解析机器人消息中的文档内容
     *
     * @param jsonArray
     * @param stringBuilder
     * @throws JSONException
     */
    private static void dealDocuments(JSONArray jsonArray, StringBuilder stringBuilder) throws JSONException {
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append(TAG_BR);
            }
            for (int i = 0; i < size; i++) {
                int id = 0;
                JSONObject itemObj = jsonArray.getJSONObject(i);
                if (itemObj.has(Field.ID)) {
                    id = itemObj.getInt(Field.ID);
                } else if (itemObj.has(Field.POST_ID)) {
                    id = itemObj.getInt(Field.POST_ID);
                }
                stringBuilder.append(TAG_PREFIX)
                        .append("<a href=\"")
                        .append(HREF_PREFIX_DOCUMENT)
                        .append(id)
//                        .append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.POST_ID))
                        .append("\">")
                        .append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.TITLE_TAG))
                        .append("</a>");
                if (i != size - 1) {
                    stringBuilder.append(TAG_BR);
                }
            }
        }
    }

    /**
     * 解析机器人消息的分词内容
     *
     * @param jsonArray
     * @param stringBuilder
     * @throws JSONException
     */
    private static void dealQuestions(JSONArray jsonArray, StringBuilder stringBuilder) throws JSONException {
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append(TAG_BR);
            }
            for (int i = 0; i < size; i++) {
                stringBuilder.append(TAG_PREFIX)
                        .append("<a href=\"")
                        .append(HREF_PREFIX_QUESTION)
                        .append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.ID))
                        .append("\">").
                        append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.TITLE_TAG))
                        .append("</a>");
                if (i != size - 1) {
                    stringBuilder.append(TAG_BR);
                }
            }
        }
    }


    private static void dealCategories(JSONObject jsonObject, StringBuilder stringBuilder) throws JSONException {
        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.CATEGORIES);
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append(TAG_BR);
            }
            for (int i = 0; i < size; i++) {
                stringBuilder.append(TAG_PREFIX)
                        .append("<a href=\"")
                        .append(HREF_PREFIX_CATEGORY)
                        .append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.ID))
                        .append("\">").
                        append(SafeJson.safeGet(jsonArray.getJSONObject(i), Field.TITLE_TAG))
                        .append("</a>");
                if (i != size - 1) {
                    stringBuilder.append(TAG_BR);
                }
            }
        }
    }

    public static String decodeAIMessage(String message) {

        try {
            StringBuilder stringBuilder = new StringBuilder();
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.has(Field.CONTENT)) {
                String content = SafeJson.safeGet(jsonObject, Field.CONTENT);
                stringBuilder.append(content);
            }
            if (jsonObject.has(Field.ANSWER)) {
                String answer = jsonObject.getString(Field.ANSWER);
                stringBuilder.append(answer);
            }
            String type = SafeJson.safeGet(jsonObject, Field.TYPE);
            if (TextUtils.equals(Field.QUESTION, type)) {
                dealQuestions(SafeJson.safeArray(jsonObject, Field.QUESTIONS), stringBuilder);
            } else if (TextUtils.equals(Field.DOCUMENT, type)) {
                dealDocuments(SafeJson.safeArray(jsonObject, Field.DOCUMENTS), stringBuilder);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.printf("这里出现了异常", e);
            return message;
        }
    }
}
