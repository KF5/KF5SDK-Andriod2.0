package com.kf5.sdk.im.service.params;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import static com.kf5.sdk.system.entity.Field.CHAT_UPLOAD;

/**
 * author:chosen
 * date:2016/10/28 14:49
 * email:812219713@qq.com
 */

public class SocketParams {

    private static final String ACTION = "action";

    private static final String ASSIGN_AGENT = "assign_agent";

    private static final String AGENT_IDS = "agent_ids";

    private static final String FORCE = "force";

    private static final String PARAMS = "params";

    private static final String INIT = "init";

    private static final String HISTORY_MSG = "history_msg";

    private static final String FROM_ID = "from_id";

    private static final String ORDER = "order";

    private static final String NUM = "num";

    private static final String SEND_MESSAGE = "send_message";

    private static final String TYPE = "type";

    private static final String CHAT_MSG = "chat.msg";

    private static final String MSG = "msg";

    private static final String V = "v";

    private static final String UPLOAD_TOKEN = "upload_token";

    private static final String RATING = "rating";

    private static final String CHAT_RATING = "chat_rating";

    private static final String AI_MESSAGE = "ai_message";

    private static final String CANCEL_QUEUE = "cancel_queue";

    private static final String METADATA_PUT = "metadata_put";

    private static final String METADATA = "metadata";


    public static String getSettingParams() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, INIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 分配客服
     *
     * @param ids
     * @param force
     * @return
     */
    public static String getAgentsAssignParams(int[] ids, boolean force) {

        JSONObject obj1 = new JSONObject();
        try {
            obj1.put(ACTION, ASSIGN_AGENT);
            JSONObject queryObj = new JSONObject();
            if (ids != null) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < ids.length; i++) {
                    jsonArray.add(ids[i]);
                }
                queryObj.put(AGENT_IDS, jsonArray);
            }
            queryObj.put(FORCE, force);
            obj1.put(PARAMS, queryObj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return obj1.toString();
    }

    /**
     * 执行获取历史消息Request
     *
     * @param from_id
     * @param num
     * @return
     */
    public static JSONObject getHistoryMessagesParams(int from_id, int num) {

        JSONObject obj1 = new JSONObject();
        try {
            obj1.put(ACTION, HISTORY_MSG);
            JSONObject queryObj = new JSONObject();
            if (from_id > 0) {
                queryObj.put(FROM_ID, from_id);
            }
            if (from_id > 0) {
                queryObj.put(ORDER, "asc");
            } else queryObj.put(ORDER, "desc");
            if (num > 0) {
                queryObj.put(NUM, num);
            }
            obj1.put(PARAMS, queryObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj1;
    }

    /*
    * 获取发送消息时的必要参数
    */
    public static String getSendMessagesParams(String content) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, SEND_MESSAGE);
            JSONObject bodyObj = new JSONObject();
            bodyObj.put(TYPE, CHAT_MSG);
            bodyObj.put(MSG, content);
//            bodyObj.put(V, tag);
            jsonObject.put(PARAMS, bodyObj);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    /**
     * 上传附件
     *
     * @param token
     * @return
     */
    public static String getUploadParams(String token) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, SEND_MESSAGE);
            JSONObject paramObj = new JSONObject();
            paramObj.put(TYPE, CHAT_UPLOAD);
            paramObj.put(UPLOAD_TOKEN, token);
            jsonObject.put(PARAMS, paramObj);
//            paramObj.put(V, tag);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return jsonObject.toString();
    }

    /**
     * 获取发送评价的参数
     *
     * @param rating 评价状态
     * @return
     */
    public static String getRatingParams(int rating) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, CHAT_RATING);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(RATING, rating);
            jsonObject.put(PARAMS, paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 机器人指令
     *
     * @param msg
     * @return
     */
    public static String getAIMessageParams(String msg, String tag) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, AI_MESSAGE);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(MSG, msg);
            paramsObj.put(V, tag);
            jsonObject.put(PARAMS, paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    /**
     * 排队状态时发送临时消息的指令
     *
     * @param msg
     * @return
     */
    public static String getQueueMessageParams(String msg) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, SEND_MESSAGE);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(TYPE, CHAT_MSG);
            paramsObj.put(MSG, msg);
            jsonObject.put(PARAMS, paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    /**
     * 取消排队
     *
     * @return
     */
    public static String getCancelQueueParams() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, CANCEL_QUEUE);
            JSONObject paramsObj = new JSONObject();
            jsonObject.put(PARAMS, paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 设置用户自定义属性
     *
     * @param jsonArray
     * @return
     */
    public static String getMetadataParams(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, METADATA_PUT);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(METADATA, jsonArray);
            jsonObject.put(PARAMS, paramsObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
