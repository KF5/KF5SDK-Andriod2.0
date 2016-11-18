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

public class IMMessageManager {

    /**
     * 发送文本消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildSendTextMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_MSG, MessageType.TEXT, Status.SENDING, false);
    }

    /**
     * 系统消息
     *
     * @param content
     * @return
     */
    public static IMMessage buildSystemMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_SYSTEM, MessageType.SYSTEM, Status.SENDING, false);
    }

    /**
     * 发送的图片消息实体
     *
     * @param path
     * @return
     */
    public static IMMessage buildSendImageMessage(String path) {
        return buildCommonUploadMessage(path, MessageType.IMAGE, false);
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
            messageList.add(buildSendImageMessage(file.getAbsolutePath()));
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
            messageList.add(buildSendImageMessage(s));
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
        return buildCommonIMMessage(content, Field.CHAT_MSG, MessageType.QUEUE_WAITING, Status.SENDING, false);
    }

    /**
     * 发送语音消息实体
     *
     * @param path
     * @return
     */
    public static IMMessage buildSendVoiceMessage(String path) {
        return buildCommonUploadMessage(path, MessageType.VOICE, false);
    }

    /**
     * 发送的机器人消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildSendAIMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_MSG, MessageType.AI_MESSAGE, Status.SENDING, false);
    }

    /**
     * 接收的文本消息实体
     *
     * @param content
     * @return
     */
    public static IMMessage buildReceiveTextMessage(String content) {
        return buildCommonIMMessage(content, Field.CHAT_MSG, MessageType.TEXT, Status.SENDING, true);
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


    private static IMMessage buildCommonUploadMessage(String path, MessageType messageType, boolean isCom) {
        IMMessage imMessage = buildCommonIMMessage("", Field.CHAT_UPLOAD, messageType, Status.SENDING, isCom);
        imMessage.setUpload(buildCommonUpload(path));
        return imMessage;
    }


    /**
     * @param content
     * @param type
     * @param messageType
     * @param status
     * @param isCom
     * @return
     */
    private static IMMessage buildCommonIMMessage(String content, String type, MessageType messageType, Status status, boolean isCom) {
        IMMessage imMessage = new IMMessage();
        imMessage.setCom(isCom);
        imMessage.setMessage(content);
        imMessage.setStatus(status);
        imMessage.setIsRead(0);
        imMessage.setMessageType(messageType);
        imMessage.setType(type);
        long time = System.currentTimeMillis();
        imMessage.setTimeStamp(String.valueOf(time));
        imMessage.setCreated(time / 1000);
//        imMessage.setTag(String.valueOf(time));
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
