package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/26 15:22
 * email:812219713@qq.com
 */

public class MessageAdapter extends CommonAdapter<IMMessage> {

    private static final int MESSAGE_TYPE_RECEIVE_TXT = 0;
    private static final int MESSAGE_TYPE_SEND_TXT = 1;
    private static final int MESSAGE_TYPE_SEND_IMAGE = 2;
    private static final int MESSAGE_TYPE_RECEIVE_IMAGE = 3;
    private static final int MESSAGE_TYPE_SEND_VOICE = 4;
    private static final int MESSAGE_TYPE_RECEIVE_VOICE = 5;
    private static final int MESSAGE_TYPE_SEND_FILE = 6;
    private static final int MESSAGE_TYPE_RECEIVE_FILE = 7;
    private static final int MESSAGE_TYPE_SYSTEM = 8;
    private static final int MESSAGE_TYPE_AI_MESSAGE_SEND = 9;
    private static final int MESSAGE_TYPE_AI_MESSAGE_RECEIVE = 10;
    private static final int MESSAGE_TYPE_QUEUE_WAITING = 11;
    private static final int MESSAGE_TYPE_RECEIVE_CUSTOM = 12;
    private static final int MESSAGE_TYPE_SEND_CUSTOM = 13;
    private static final int MESSAGE_TYPE_CARD = 14;
    private static final int MESSAGE_TYPE_MESSAGE_RECALLED = 15;
    private static final int MESSAGE_TYPE_VIDEO_SEND = 16;
    private static final int MESSAGE_TYPE_VIDEO_RECEIVE = 17;

    protected Map<Integer, String> urlMap = new ArrayMap<>();

    public MessageAdapter(Context context, List<IMMessage> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage message = getItem(position);
        String role = message.getRole();
        String type = message.getType();
        if (message.getRecalledStatus() == 1) {
            return MESSAGE_TYPE_MESSAGE_RECALLED;
        } else {
            if (TextUtils.equals(type, Field.CHAT_SYSTEM)) {
                return MESSAGE_TYPE_SYSTEM;
            } else if (TextUtils.equals(type, Field.QUEUE_WAITING)) {
                return MESSAGE_TYPE_QUEUE_WAITING;
            } else if (TextUtils.equals(type, Field.CHAT_CARD)) {
                return MESSAGE_TYPE_CARD;
            } else {
                if (TextUtils.equals(Field.VISITOR, role)) {
                    switch (type) {
                        case Field.CHAT_MSG:
                            return MESSAGE_TYPE_SEND_TXT;
                        case Field.CHAT_UPLOAD:
                            return getUploadViewType(message, false);
                        case Field.AI_SEND:
                            return MESSAGE_TYPE_AI_MESSAGE_SEND;
                        case Field.CHAT_CUSTOM:
                            return MESSAGE_TYPE_SEND_CUSTOM;
                    }
                } else if (TextUtils.equals(Field.AGENT, role)) {
                    switch (type) {
                        case Field.CHAT_MSG:
                            return MESSAGE_TYPE_RECEIVE_TXT;
                        case Field.CHAT_UPLOAD:
                            return getUploadViewType(message, true);
                        case Field.AI_RECEIVE:
                            return MESSAGE_TYPE_AI_MESSAGE_RECEIVE;
                        case Field.CHAT_CUSTOM:
                            return MESSAGE_TYPE_RECEIVE_CUSTOM;
                    }
                } else if (TextUtils.equals(Field.ROBOT, role)) {
                    switch (type) {
                        case Field.CHAT_UPLOAD:
                            return getUploadViewType(message, true);
                        default:
                            return MESSAGE_TYPE_AI_MESSAGE_RECEIVE;
                    }
                }
            }
        }
        return -1;
    }


    private int getUploadViewType(IMMessage message, boolean isReceive) {
        Upload upload = message.getUpload();
        String uploadType = upload.getType();
        if (Utils.isImage(uploadType)) {
            //图片
            return isReceive ? MESSAGE_TYPE_RECEIVE_IMAGE : MESSAGE_TYPE_SEND_IMAGE;
        } else if (Utils.isAMR(uploadType)) {
            //语音
            return isReceive ? MESSAGE_TYPE_RECEIVE_VOICE : MESSAGE_TYPE_SEND_VOICE;
        } else if (Utils.isMP4(uploadType)) {
            //视频
            return isReceive ? MESSAGE_TYPE_VIDEO_RECEIVE : MESSAGE_TYPE_VIDEO_SEND;
        } else {
            //附件
            return isReceive ? MESSAGE_TYPE_RECEIVE_FILE : MESSAGE_TYPE_SEND_FILE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 18;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        IMMessage message = getItem(position);
        switch (type) {
            case MESSAGE_TYPE_RECEIVE_TXT:
            case MESSAGE_TYPE_SEND_TXT:
                return messageWithText(position, convertView, parent, type == MESSAGE_TYPE_RECEIVE_TXT, TextHolder.TextMessageType.TEXT);
            case MESSAGE_TYPE_RECEIVE_IMAGE:
            case MESSAGE_TYPE_SEND_IMAGE:
            case MESSAGE_TYPE_VIDEO_RECEIVE:
            case MESSAGE_TYPE_VIDEO_SEND:
                return messageWithImage(position, convertView, parent, (type == MESSAGE_TYPE_RECEIVE_IMAGE || type == MESSAGE_TYPE_VIDEO_RECEIVE), (type == MESSAGE_TYPE_VIDEO_RECEIVE || type == MESSAGE_TYPE_VIDEO_SEND));
            case MESSAGE_TYPE_RECEIVE_FILE:
            case MESSAGE_TYPE_SEND_FILE:
                return messageWithText(position, convertView, parent, type == MESSAGE_TYPE_RECEIVE_FILE, TextHolder.TextMessageType.FILE);
            case MESSAGE_TYPE_RECEIVE_VOICE:
            case MESSAGE_TYPE_SEND_VOICE:
                return messageWithVoice(position, convertView, parent, type == MESSAGE_TYPE_RECEIVE_VOICE);
            case MESSAGE_TYPE_AI_MESSAGE_RECEIVE:
            case MESSAGE_TYPE_AI_MESSAGE_SEND:
                return messageWithText(position, convertView, parent, type == MESSAGE_TYPE_AI_MESSAGE_RECEIVE, TextHolder.TextMessageType.AI_MESSAGE);
            case MESSAGE_TYPE_SYSTEM:
                return getMessageViewWithSystem(position, convertView, parent, message);
            case MESSAGE_TYPE_QUEUE_WAITING:
                return getMessageViewWithQueueWaiting(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_CUSTOM:
            case MESSAGE_TYPE_SEND_CUSTOM:
                return messageWithText(position, convertView, parent, type == MESSAGE_TYPE_RECEIVE_CUSTOM, TextHolder.TextMessageType.CUSTOM);
            case MESSAGE_TYPE_CARD:
                return getCardMessageView(position, convertView, parent, message);
            case MESSAGE_TYPE_MESSAGE_RECALLED:
                return getMessageRecalledView(position, convertView, parent, message);
            default:
                return getDefaultView(position, convertView, parent);
        }

    }

    @Override
    public List<IMMessage> getDataList() {
        return super.getDataList();
    }

    private View messageWithText(int position, View convertView, ViewGroup parent, boolean isReceive, TextHolder.TextMessageType textMessageType) {
        TextHolder holder;
        final int layoutId = isReceive ? R.layout.kf5_message_item_with_text_left : R.layout.kf5_message_item_with_text_right;
        if (convertView == null) {
            convertView = inflateLayout(layoutId, parent);
            holder = new TextHolder(this, convertView);
            convertView.setTag(holder);
        } else {
            holder = ((TextHolder) convertView.getTag());
        }
        holder.initData(position, isReceive, textMessageType);
        holder.setUpUI();
        return convertView;
    }

    private View messageWithVoice(int position, View convertView, ViewGroup parent, boolean isReceive) {
        VoiceHolder holder;
        final int layoutId = isReceive ? R.layout.kf5_message_item_with_voice_left : R.layout.kf5_message_item_with_voice_right;
        if (convertView == null) {
            convertView = inflateLayout(layoutId, parent);
            holder = new VoiceHolder(this, convertView);
            convertView.setTag(holder);
        } else {
            holder = ((VoiceHolder) convertView.getTag());
        }
        holder.initData(position, isReceive);
        holder.setUpUI();
        return convertView;
    }

    private View messageWithImage(int position, View convertView, ViewGroup parent, boolean isReceive, boolean isVideo) {
        final ImageHolder holder;
        int layoutId = isVideo ? (isReceive ? R.layout.kf5_message_item_with_video_left : R.layout.kf5_message_item_with_video_right)
                : (isReceive ? R.layout.kf5_message_item_with_image_left : R.layout.kf5_message_item_with_image_right);
        if (convertView == null) {
            convertView = inflateLayout(layoutId, parent);
            holder = new ImageHolder(this, convertView);
            convertView.setTag(holder);
        } else {
            holder = ((ImageHolder) convertView.getTag());
        }
        holder.initData(position, isReceive, isVideo);
        holder.setUpUI();
        return convertView;
    }


    public View getMessageRecalledView(int position, View convertView, ViewGroup parent, IMMessage message) {

        MessageRecalledHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_system, parent);
            holder = new MessageRecalledHolder(convertView);
        } else {
            holder = (MessageRecalledHolder) convertView.getTag();
        }
        holder.bindData(mContext.getString(R.string.kf5_message_recalled));
        return convertView;
    }

    /**
     * 卡片消息
     *
     * @param position
     * @param convertView
     * @param parent
     * @param message
     * @return
     */
    private View getCardMessageView(int position, View convertView, ViewGroup parent, IMMessage message) {

        CardHolder holder;
        if (convertView == null) {
            holder = new CardHolder(convertView = inflateLayout(R.layout.kf5_message_item_with_card, parent));
        } else {
            holder = (CardHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
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

    private View getMessageViewWithSystem(int position, View convertView, ViewGroup parent, IMMessage message) {

        SystemHolder holder;
        if (convertView == null) {
            convertView = inflateLayout(R.layout.kf5_message_item_with_system, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_queue, parent);
            queueHolder = new QueueHolder(convertView);
        } else {
            queueHolder = (QueueHolder) convertView.getTag();
        }
        if (queueHolder != null)
            queueHolder.bindData(message, position);
        return convertView;
    }

}
