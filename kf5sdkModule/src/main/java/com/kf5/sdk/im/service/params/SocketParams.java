package com.kf5.sdk.im.service.params;


import com.kf5.sdk.system.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String TIMESTAMP = "timestamp";

    private static final String ID = "id";

    private static final String AI_ANSWER = "ai_answer";

    private static final String POST_MESSAGE = "post_message";

    private static final String ROBOT = "robot";

    private static final String CHAT_QUESTION = "chat.question";

    private static final String DATA = "data";

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
     * @param agentArray
     * @param force
     * @return
     */
    public static String getAgentsAssignParams(String agentArray, int force) {

        JSONObject obj1 = new JSONObject();
        try {
            obj1.put(ACTION, ASSIGN_AGENT);
            JSONObject queryObj = new JSONObject();
            queryObj.put(AGENT_IDS, agentArray);
            queryObj.put(FORCE, force);
            obj1.put(PARAMS, queryObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.printf(obj1.toString());
        return obj1.toString();
    }

    /**
     * 执行获取历史消息Request
     *
     * @param from_id
     * @param num
     * @return
     */
    public static String getHistoryMessagesParams(int from_id, int num) {

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
        return obj1.toString();
    }

    /*
     * 获取发送消息时的必要参数
     */
    public static String getSendMessagesParams(String content, String timeStamp) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, SEND_MESSAGE);
            JSONObject bodyObj = new JSONObject();
            bodyObj.put(TYPE, CHAT_MSG);
            bodyObj.put(MSG, content);
            bodyObj.put(TIMESTAMP, timeStamp);
            jsonObject.put(PARAMS, bodyObj);
        } catch (JSONException e) {
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
    public static String getUploadParams(String token, String timeStamp) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, SEND_MESSAGE);
            JSONObject paramObj = new JSONObject();
            paramObj.put(TYPE, CHAT_UPLOAD);
            paramObj.put(UPLOAD_TOKEN, token);
            paramObj.put(TIMESTAMP, timeStamp);
            jsonObject.put(PARAMS, paramObj);
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
    public static String getAIMessageParams(String msg, String tag, JSONArray category_ids, JSONArray forum_ids) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, AI_MESSAGE);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(MSG, msg);
            paramsObj.put(TIMESTAMP, tag);
            paramsObj.put("question_category_ids", category_ids);
            paramsObj.put("forum_category_ids", forum_ids);
            jsonObject.put(PARAMS, paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    /**
     * 机器人分词Action
     *
     * @param id
     * @param timeStamp
     * @return
     */
    public static String getAIAnswerParams(int id, String timeStamp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, AI_ANSWER);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(ID, id);
            paramsObj.put(TIMESTAMP, timeStamp);
            jsonObject.put(PARAMS, paramsObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    /**
     * 排队状态时发送临时消息的指令
     *
     * @param msg       消息内容
     * @param timeStamp 时间戳
     * @return
     */
    public static String getQueueMessageParams(String msg, String timeStamp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, SEND_MESSAGE);
            JSONObject paramsObj = new JSONObject();
            paramsObj.put(TYPE, CHAT_MSG);
            paramsObj.put(MSG, msg);
            paramsObj.put(TIMESTAMP, timeStamp);
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


    /**
     * 获取撤回消息列表
     * （新增该接口主要是为了解决一下场景：sdk已经收到当前消息，sdk断开im链接，客服端撤回已发送消息，由于sdk本地已经存在当前消息，所以需要单独对撤回消息处理）
     *
     * @return
     */
    public static String getRecallMessageListString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ACTION, "recalled_message");
            jsonObject.put(PARAMS, new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
