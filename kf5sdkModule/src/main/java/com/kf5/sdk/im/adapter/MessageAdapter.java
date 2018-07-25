package com.kf5.sdk.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.kf5.sdk.R;
import com.kf5.sdk.im.api.FileDownLoadCallBack;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.Utils;

import java.io.File;
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

    private Map<String, IMMessage> downloadMap = new ArrayMap<>();

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
                            Upload upload = message.getUpload();
                            String uploadType = upload.getType();
                            if (Utils.isImage(uploadType)) {
                                //图片
                                return MESSAGE_TYPE_SEND_IMAGE;
                            } else if (Utils.isAMR(uploadType)) {
                                //语音
                                return MESSAGE_TYPE_SEND_VOICE;
                            } else {
                                //附件
                                return MESSAGE_TYPE_SEND_FILE;
                            }
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
                            Upload upload = message.getUpload();
                            String uploadType = upload.getType();
                            if (Utils.isImage(uploadType)) {
                                //图片
                                return MESSAGE_TYPE_RECEIVE_IMAGE;
                            } else if (Utils.isAMR(uploadType)) {
                                //语音
                                return MESSAGE_TYPE_RECEIVE_VOICE;
                            } else {
                                //附件
                                return MESSAGE_TYPE_RECEIVE_FILE;
                            }
                        case Field.AI_RECEIVE:
                            return MESSAGE_TYPE_AI_MESSAGE_RECEIVE;
                        case Field.CHAT_CUSTOM:
                            return MESSAGE_TYPE_RECEIVE_CUSTOM;
                    }
                } else if (TextUtils.equals(Field.ROBOT, role)) {
                    return MESSAGE_TYPE_AI_MESSAGE_RECEIVE;
                }
            }
        }

        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 16;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        IMMessage message = getItem(position);
        switch (type) {
            case MESSAGE_TYPE_RECEIVE_TXT:
                return messageWithTextReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SEND_TXT:
                return messageWithTextSend(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_IMAGE:
                return messageWithImageReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SEND_IMAGE:
                return messageWithImageSend(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_FILE:
                return messageWithFileReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SEND_FILE:
                return messageWithFileSend(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_VOICE:
                return messageWithVoiceReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_SEND_VOICE:
                return messageWithVoiceSend(position, convertView, parent, message);
            case MESSAGE_TYPE_AI_MESSAGE_RECEIVE:
                return messageWithAIMessageReceive(position, convertView, parent, message);
            case MESSAGE_TYPE_AI_MESSAGE_SEND:
                return messageWithAIMessageSend(position, convertView, parent, message);
            case MESSAGE_TYPE_SYSTEM:
                return getMessageViewWithSystem(position, convertView, parent, message);
            case MESSAGE_TYPE_QUEUE_WAITING:
                return getMessageViewWithQueueWaiting(position, convertView, parent, message);
            case MESSAGE_TYPE_RECEIVE_CUSTOM:
                return getCustomReceiveView(position, convertView, parent, message);
            case MESSAGE_TYPE_SEND_CUSTOM:
                return getCustomSendView(position, convertView, parent, message);
            case MESSAGE_TYPE_CARD:
                return getCardMessageView(position, convertView, parent, message);
            case MESSAGE_TYPE_MESSAGE_RECALLED:
                return getMessageRecalledView(position, convertView, parent, message);
            default:
                return getDefaultView(position, convertView, parent);
        }

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


    /**
     * 富文本消息接收者
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View getCustomReceiveView(int position, View convertView, ViewGroup parent, IMMessage message) {

        CustomReceiveHolder holder;
        if (convertView == null) {
            holder = new CustomReceiveHolder(convertView = inflateLayout(R.layout.kf5_message_item_with_custom_left, parent));
        } else {
            holder = (CustomReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
        return convertView;
    }


    /**
     * 富文本消息发送者
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View getCustomSendView(int position, View convertView, ViewGroup parent, IMMessage message) {

        CustomSendHolder holder;

        if (convertView == null) {
            holder = new CustomSendHolder(convertView = inflateLayout(R.layout.kf5_message_item_with_custom_right, parent));
        } else {
            holder = (CustomSendHolder) convertView.getTag();
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_right, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_voice_left, parent);
            holder = new VoiceReceiveHolder(convertView);
        } else {
            holder = (VoiceReceiveHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1), downloadMap, callBack);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_voice_right, parent);
            holder = new VoiceSendHolder(convertView);
        } else {
            holder = (VoiceSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1), downloadMap, callBack);

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
            convertView = inflateLayout(R.layout.kf5_message_item_with_image_left, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_image_right, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_left, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_left, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_right, parent);
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
            convertView = inflateLayout(R.layout.kf5_message_item_with_text_right, parent);
            holder = new TextSendHolder(convertView);
        } else {
            holder = (TextSendHolder) convertView.getTag();
        }
        holder.bindData(message, position, getItem(position - 1));
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

    private FileDownLoadCallBack callBack = new FileDownLoadCallBack() {

        @Override
        public void onResult(Status status, String result, String fileName) {
            if (downloadMap.containsKey(fileName)) {
                IMMessage message = downloadMap.get(fileName);
                downloadMap.remove(fileName);
                if (Status.SUCCESS == status) {
                    File file = new File(FilePath.SAVE_RECORDER, fileName);
                    if (file.exists()) {
                        Upload upload = message.getUpload();
                        if (upload != null) {
                            upload.setLocalPath(file.getAbsolutePath());
                            IMSQLManager.updateLocalPathByTimeStamp(mContext, file.getAbsolutePath(), message.getTimeStamp());
                        }
                    }
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


        }
    };


}
