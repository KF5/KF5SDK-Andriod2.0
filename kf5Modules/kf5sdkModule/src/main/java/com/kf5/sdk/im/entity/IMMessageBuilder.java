package com.kf5.sdk.im.entity;

import android.content.Context;
import android.support.annotation.Nullable;

import com.kf5.sdk.im.utils.ImageUtils;
import com.kf5.sdk.im.utils.MediaPlayerUtils;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.SafeJson;
import com.kf5.sdk.system.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author:chosen
 * date:2016/11/4 10:48
 * email:812219713@qq.com
 * 消息实体工厂提供类
 */

public class IMMessageBuilder {

    /**
     * 发送文本消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildSendTextMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_MSG, Field.VISITOR, Status.SENDING);
    }


    /**
     * 卡片消息
     *
     * @param content
     * @return
     */
    public static IMMessage buildCardMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_CARD, null, Status.SUCCESS);
    }

    /**
     * 系统消息
     *
     * @param content
     * @return
     */
    public static IMMessage buildSystemMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_SYSTEM, "", Status.SUCCESS);
    }

    /**
     * 发送的图片消息实体
     *
     * @param path
     * @return
     */
    public static IMMessage buildSendImageMessage(String path, String token) {
        return buildCommonUploadMessage(null, token, path, Field.VISITOR, UploadType.IMAGE);
    }

    /**
     * 发送图片消息集合
     *
     * @param fileList
     * @return
     */
    public static List<IMMessage> buildSendImageList(List<File> fileList) {
        List<IMMessage> messageList = new ArrayList<>();
        for (File file : fileList) {
            messageList.add(buildSendImageMessage(file.getAbsolutePath(), null));
        }
        return messageList;
    }

    /**
     * 发送图片消息集合实体
     *
     * @param path
     * @return
     */
    public static List<IMMessage> buildSendImageMessageList(String... path) {
        List<IMMessage> messageList = new ArrayList<>();
        for (String s : path) {
            messageList.add(buildSendImageMessage(s, null));
        }
        return messageList;
    }

    /**
     * 发送排队的消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildSendQueueMessage(String content) {
        return buildCommonIMMessage(content, Field.QUEUE_WAITING, "", Status.SUCCESS);
    }

    /**
     * 发送语音消息实体
     *
     * @param path
     * @param token
     * @return
     */
    public static IMMessage buildSendVoiceMessage(Context context, String path, String token) {
        return buildCommonUploadMessage(context, token, path, Field.VISITOR, UploadType.VOICE);
    }

    /**
     * 视频实体
     *
     * @param path
     * @param token
     * @return
     */
    public static IMMessage buildSendVideoMessage(String path, String token) {
        return buildCommonUploadMessage(null, token, path, Field.VISITOR, UploadType.VIDEO);
    }

    /**
     * 发送的机器人消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildSendAIMessage(String content) {
//        String result;
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(Field.CONTENT, content);
//            result = jsonObject.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            result = content;
//        }
        return buildCommonIMMessage(content, Field.AI_SEND, Field.VISITOR, Status.SENDING);
    }


    /**
     * 接收的机器人消息实体
     *
     * @param jsonObject
     * @return
     */
    public static IMMessage buildReceiveAIMessage(JSONObject jsonObject) {
        IMMessage aiMessage = new IMMessage();
        aiMessage.setMessageId(SafeJson.safeInt(jsonObject, Field.ID));
        aiMessage.setTimeStamp(SafeJson.safeGet(jsonObject, Field.TIMESTAMP));
        aiMessage.setChatId(SafeJson.safeInt(jsonObject, Field.CHAT_ID));
        aiMessage.setCompanyId(SafeJson.safeInt(jsonObject, Field.COMPANY_ID));
        aiMessage.setRecalledStatus(SafeJson.safeInt(jsonObject, Field.RECALLED));
        aiMessage.setCreated(SafeJson.safeInt(jsonObject, Field.CREATED));
        aiMessage.setName(SafeJson.safeGet(jsonObject, Field.NAME));
        aiMessage.setRole(SafeJson.safeGet(jsonObject, Field.ROLE));
        aiMessage.setUserId(SafeJson.safeInt(jsonObject, Field.USER_ID));
        aiMessage.setIsRead(SafeJson.safeInt(jsonObject, Field.IS_READ));
        aiMessage.setType(SafeJson.safeGet(jsonObject, Field.TYPE));
        aiMessage.setMessage(SafeJson.safeGet(jsonObject, Field.MSG));
        aiMessage.setReply_timeout(SafeJson.safeInt(jsonObject, Field.REPLY_TIMEOUT));
        aiMessage.setStatus(Status.SUCCESS);
//        aiMessage.setRole(Field.ROBOT);
//        aiMessage.setType(Field.AI_RECEIVE);
//        aiMessage.setStatus(Status.SUCCESS);
//        aiMessage.setCreated(System.currentTimeMillis() / 1000);
//        aiMessage.setMessage(jsonString);
//        aiMessage.setTimeStamp(timeStamp);
//        aiMessage.setType(Field.CHAT_MSG);
//        aiMessage.setMessageId(id);
        return aiMessage;
    }

    /**
     * 接收的文本消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildReceiveTextMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_MSG, Field.AGENT, Status.SUCCESS);
    }


    /**
     * 将消息实体封装到集合中
     *
     * @param message
     * @return
     */
    public static List<IMMessage> addIMMessageToList(IMMessage message) {
        List<IMMessage> list = new ArrayList<>();
        Collections.addAll(list, message);
        return list;
    }


    private static IMMessage buildCommonUploadMessage(@Nullable Context context, String content, String path, String role, UploadType uploadType) {
        IMMessage imMessage = buildCommonIMMessage(content, Field.CHAT_UPLOAD, role, Status.SENDING);
        imMessage.setUpload(buildCommonUpload(context, path, uploadType));
        return imMessage;
    }


    /**
     * @param content
     * @param type
     * @param role
     * @param status
     * @return
     */
    private static IMMessage buildCommonIMMessage(String content, String type, String role, Status status) {
        IMMessage imMessage = new IMMessage();
        imMessage.setMessage(content);
        imMessage.setStatus(status);
        imMessage.setIsRead(0);
        imMessage.setRole(role);
        imMessage.setType(type);
        long time = System.currentTimeMillis();
        imMessage.setTimeStamp(String.valueOf(time));
        imMessage.setCreated(time / 1000);
        return imMessage;
    }

    private static Upload buildCommonUpload(@Nullable Context context, String path, UploadType uploadType) {
        File file = new File(path);
        Upload upload = new Upload();
        upload.setLocalPath(path);
        upload.setName(file.getName());
        switch (uploadType) {
            case IMAGE: {
                upload.setType(Utils.getFileType(file.getName()));
                ImageUtils.ImageSize imageSize = ImageUtils.getSizeOfImage(path);
                if (imageSize != null && imageSize.width > 0 && imageSize.height > 0) {
                    upload.setWidth(imageSize.width);
                    upload.setHeight(imageSize.height);
                }
            }
            break;
            case VOICE: {
                upload.setType(Field.AMR);
                if (context != null)
                    upload.setVoiceDuration(MediaPlayerUtils.getLocalVoiceDuration(context, path));
            }

            break;
            case VIDEO: {
                upload.setType("mp4");
                ImageUtils.ImageSize imageSize = ImageUtils.getSizeOfVideo(path);
                if (imageSize != null && imageSize.width > 0 && imageSize.height > 0) {
                    upload.setWidth(imageSize.width);
                    upload.setHeight(imageSize.height);
                }
            }

            break;
        }
        return upload;
    }


    private enum UploadType {
        VOICE, IMAGE, VIDEO
    }
}
