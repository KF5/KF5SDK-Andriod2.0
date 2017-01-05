package com.kf5.sdk.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.kf5.sdk.R;
import com.kf5.sdk.im.api.FileDownLoadCallBack;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.system.base.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/26 15:22
 * email:812219713@qq.com
 */

public class MessageAdapter extends CommonAdapter<IMMessage> {

    private static final int MESSAGE_TYPE_RECEIVE_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_RECEIVE_IMAGE = 3;
    private static final int MESSAGE_TYPE_SENT_VOICE = 4;
    private static final int MESSAGE_TYPE_RECEIVE_VOICE = 5;
    private static final int MESSAGE_TYPE_SENT_FILE = 6;
    private static final int MESSAGE_TYPE_RECEIVE_FILE = 7;
    private static final int MESSAGE_TYPE_SYSTEM = 8;
    private static final int MESSAGE_TYPE_AI_MESSAGE_SEND = 9;
    private static final int MESSAGE_TYPE_AI_MESSAGE_RECEIVER = 10;
    private static final int MESSAGE_TYPE_QUEUE_WAITING = 11;
    private List<String> listName = new ArrayList<>();

    public MessageAdapter(Context context, List<IMMessage> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {

        IMMessage message = getItem(position);
        switch (message.getMessageType()) {
            case TEXT:
                return message.isCom() ? MESSAGE_TYPE_RECEIVE_TXT : MESSAGE_TYPE_SENT_TXT;
            case IMAGE:
                return message.isCom() ? MESSAGE_TYPE_RECEIVE_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
            case VOICE:
                return message.isCom() ? MESSAGE_TYPE_RECEIVE_VOICE : MESSAGE_TYPE_SENT_VOICE;
            case FILE:
                return message.isCom() ? MESSAGE_TYPE_RECEIVE_FILE : MESSAGE_TYPE_SENT_FILE;
            case AI_MESSAGE:
                return message.isCom() ? MESSAGE_TYPE_AI_MESSAGE_RECEIVER : MESSAGE_TYPE_AI_MESSAGE_SEND;
            case QUEUE_WAITING:
                return MESSAGE_TYPE_QUEUE_WAITING;
            case SYSTEM:
                return MESSAGE_TYPE_SYSTEM;
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 12;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        IMMessage message = getItem(position);
        switch (type) {
            case MESSAGE_TYPE_RECEIVE_TXT:
                return messageWithTextReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SENT_TXT:
                return messageWithTextSend(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_IMAGE:
                return messageWithImageReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SENT_IMAGE:
                return messageWithImageSend(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_FILE:
                return messageWithFileReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SENT_FILE:
                return messageWithFileSend(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_VOICE:
                return messageWithVoiceReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SENT_VOICE:
                return messageWithVoiceSend(position, convertView, parent, message);
            case MESSAGE_TYPE_AI_MESSAGE_RECEIVER:
                return messageWithAIMessageReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_AI_MESSAGE_SEND:
                return messageWithAIMessageSend(position, convertView, parent, message);
            case MESSAGE_TYPE_SYSTEM:
                return getMessageViewWithSystem(position, convertView, parent, message);
            case MESSAGE_TYPE_QUEUE_WAITING:
                return getMessageViewWithQueueWaiting(position, convertView, parent, message);
            default:
                return getDefaultView(position, convertView, parent);
        }

    }

    private View getDefaultView(int position, View convertView, ViewGroup parent) {

        DefaultHolder holder;
        if (convertView == null) {
            holder = new DefaultHolder();
            convertView = inflateLayout(R.layout.kf5_message_with_default, parent);
            convertView.setTag(holder);
        } else {
            holder = (DefaultHolder) convertView.getTag();
        }
        return convertView;

    }


    /**
     * 消息类型之文件消息接收
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithFileReceive(int position, View convertView, ViewGroup parent, final IMMessage message) {

        FileReceiveHolder holder = null;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_left, parent);
            holder = new FileReceiveHolder(convertView);
        } else {
            holder = (FileReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;

    }

    /**
     * 消息类型之文件消息发送
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithFileSend(int position, View convertView, ViewGroup parent, final IMMessage message) {

        FileSendHolder holder = null;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_right,parent);
            holder = new FileSendHolder(convertView);
        } else {
            holder = (FileSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;

    }


    /**
     * 消息类型之语音接收消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithVoiceReceive(int position, View convertView, ViewGroup parent, IMMessage message) {

        VoiceReceiveHolder holder = null;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_voice_left,parent);
            holder = new VoiceReceiveHolder(convertView);
        } else {
            holder = (VoiceReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1), listName, callBack);
        return convertView;

    }


    /**
     * 消息类型之语音发送消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithVoiceSend(int position, View convertView, ViewGroup parent, IMMessage message) {

        VoiceSendHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_voice_right,parent);
            holder = new VoiceSendHolder(convertView);
        } else {
            holder = (VoiceSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1), listName, callBack);

        return convertView;

    }


    /**
     * 消息类型之接收图片消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithImageReceive(int position, View convertView, ViewGroup parent, IMMessage message) {

        final ImageReceiveHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_image_left,parent);
            holder = new ImageReceiveHolder(convertView);
        } else {
            holder = (ImageReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1), this);
        return convertView;

    }

    /**
     * 消息类型之发送的图片消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithImageSend(int position, View convertView, ViewGroup parent, IMMessage message) {

        final ImageSendHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_image_right,parent);
            holder = new ImageSendHolder(convertView);
        } else {
            holder = (ImageSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1), this);
        return convertView;
    }


    /**
     * 消息类型之接收的文本消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithTextReceive(int position, View convertView, ViewGroup parent, IMMessage message) {

        TextReceiveHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_left,parent);
            holder = new TextReceiveHolder(convertView);
        } else {
            holder = (TextReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
    }


    /**
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithAIMessageReceive(int position, View convertView, ViewGroup parent, IMMessage message) {

        AIMessageReceiveHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_left,parent);
            holder = new AIMessageReceiveHolder(convertView);
        } else {
            holder = (AIMessageReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithAIMessageSend(int position, View convertView, ViewGroup parent, IMMessage message) {

        AIMessageSendHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_right,parent);
            holder = new AIMessageSendHolder(convertView);
        } else {
            holder = (AIMessageSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
    }


    /**
     * 消息类型之发送的文本消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View messageWithTextSend(int position, View convertView, ViewGroup parent, IMMessage message) {

        TextSendHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_right,parent);
            holder = new TextSendHolder(convertView);
        } else {
            holder = (TextSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
    }


    public View getMessageViewWithSystem(int position, View convertView, ViewGroup parent, IMMessage message) {

        SystemHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_system,parent);
            holder = new SystemHolder(convertView);
        } else {
            holder = (SystemHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
    }

    private View getMessageViewWithQueueWaiting(int position, View convertView, ViewGroup parent, IMMessage message) {

        QueueHolder queueHolder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_queue,parent);
            queueHolder = new QueueHolder(convertView);
        } else {
            queueHolder = (QueueHolder) convertView.getTag();
        }
        if (queueHolder != null)
            queueHolder.bindData(message, position);
        return convertView;
    }

    private FileDownLoadCallBack callBack = new FileDownLoadCallBack() {

        @Override
        public void onResult(String result) {

            if (!TextUtils.isEmpty(result) && listName != null && listName.contains(result)) {
                listName.remove(result);
                if (mContext != null && mContext instanceof Activity) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetInvalidated();
                        }
                    });
                }
            }
        }
    };


}
