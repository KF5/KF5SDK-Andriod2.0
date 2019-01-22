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


    public static final String HREF_PREFIX_QUESTION = "chosenQuestionTo://";
    public static final String HREF_PREFIX_DOCUMENT = "chosenDocumentTo://";
    public static final String HREF_PREFIX_CUSTOM = "chosenVideoChatTo://";

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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    public static String dealAIMessage(String message) {

        try {
            StringBuilder stringBuilder = new StringBuilder();
            new JSONObject(message);
            JSONObject jsonObject = SafeJson.parseObj(message);
            String content = SafeJson.safeGet(jsonObject, Field.CONTENT);
            if (content.contains("{{") && content.contains("}}")) {
                content = content.replaceAll("\\{\\{", "<a href=\"" + Field.GET_AGENT + "\">");
                content = content.replaceAll("\\}\\}", "</a>");
            }
            stringBuilder.append(content);

            String type = SafeJson.safeGet(jsonObject, Field.TYPE);
            if (TextUtils.equals(Field.QUESTION, type)) {
                dealQuestions(jsonObject, stringBuilder);
            } else if (TextUtils.equals(Field.DOCUMENT, type)) {
                dealDocuments(jsonObject, stringBuilder);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                JSONArray jsonArray = new JSONArray(message);
                StringBuilder sb = new StringBuilder();
                sb.append("已为您找到以下内容：")
                        .append("<br/>");
                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject itemObj = jsonArray.getJSONObject(i);
                    sb.append("● ")
                            .append("<a href=\"")
                            .append(SafeJson.safeGet(itemObj, Field.ID))
                            .append("\">")
                            .append(SafeJson.safeGet(itemObj, Field.TITLE_TAG))
                            .append("</a>");
                    if (i != size - 1) {
                        sb.append("<br/>");
                    }
                }
                return sb.toString();
            } catch (JSONException e1) {
                e1.printStackTrace();
                return message;
            }
        }
    }

    /**
     * 解析机器人消息中的文档内容
     *
     * @param jsonObject
     * @param stringBuilder
     * @throws JSONException
     */
    private static void dealDocuments(JSONObject jsonObject, StringBuilder stringBuilder) throws JSONException {

        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.DOCUMENTS);
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append("<br/>");
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
                    stringBuilder.append("<br/>");
                }
            }
        }
    }

    /**
     * 解析机器人消息的分词内容
     *
     * @param jsonObject
     * @param stringBuilder
     * @throws JSONException
     */
    private static void dealQuestions(JSONObject jsonObject, StringBuilder stringBuilder) throws JSONException {
        JSONArray jsonArray = SafeJson.safeArray(jsonObject, Field.QUESTIONS);
        if (jsonArray != null) {
            int size = jsonArray.length();
            if (size > 0) {
                stringBuilder.append("<br/>");
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
                    stringBuilder.append("<br/>");
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
                dealQuestions(jsonObject, stringBuilder);
            } else if (TextUtils.equals(Field.DOCUMENT, type)) {
                dealDocuments(jsonObject, stringBuilder);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.printf("这里出现了异常", e);
            return message;
        }
    }
}
