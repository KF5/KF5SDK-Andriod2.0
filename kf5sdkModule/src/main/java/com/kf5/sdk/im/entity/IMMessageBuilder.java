package com.kf5.sdk.im.entity;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.Utils;

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
        return buildCommonUploadMessage(token, path, Field.VISITOR);
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
    public static IMMessage buildSendVoiceMessage(String path, String token) {
        return buildCommonUploadMessage(token, path, Field.VISITOR);
    }

    /**
     * 发送的机器人消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildSendAIMessage(String content) {
        return buildCommonIMMessage(content, Field.AI_SEND, Field.VISITOR, Status.SENDING);
    }


    /**
     * 接收的机器人消息实体
     *
     * @param content
     * @param timeStamp
     * @return
     */
    public static IMMessage buildReceiveAIMessage(String content, String timeStamp, int id) {
        IMMessage aiMessage = new IMMessage();
        aiMessage.setRole(Field.ROBOT);
        aiMessage.setType(Field.AI_RECEIVE);
        aiMessage.setStatus(Status.SUCCESS);
        aiMessage.setCreated(System.currentTimeMillis() / 1000);
        aiMessage.setMessage(content);
        aiMessage.setTimeStamp(timeStamp);
        aiMessage.setType(Field.CHAT_MSG);
        aiMessage.setId(id);
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


    private static IMMessage buildCommonUploadMessage(String content, String path, String role) {
        IMMessage imMessage = buildCommonIMMessage(content, Field.CHAT_UPLOAD, role, Status.SENDING);
        imMessage.setUpload(buildCommonUpload(path));
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

    private static Upload buildCommonUpload(String path) {
        File file = new File(path);
        Upload upload = new Upload();
        upload.setLocalPath(path);
        upload.setName(file.getName());
        upload.setType(Utils.getFileType(file.getName()));
        return upload;
    }

}
