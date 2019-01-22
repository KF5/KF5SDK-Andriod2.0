package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;

import java.io.File;
import java.util.Collections;

/**
 * @author Chosen
 * @create 2019/1/8 11:20
 * @email 812219713@qq.com
 */
class BaseSendHolder {

    private RelativeLayout progressLayout;
    ProgressBar progressBar;

    private final MessageAdapter messageAdapter;
    private final Context context;

    BaseSendHolder(MessageAdapter messageAdapter, View convertView) {
        this.messageAdapter = messageAdapter;
        this.progressLayout = convertView.findViewById(R.id.kf5_progress_layout);
        this.progressBar = convertView.findViewById(R.id.kf5_progressBar);
        this.context = convertView.getContext();
    }

    void setUpSendMessageUI(IMMessage message, MessageType messageType, int position) {
        //根据发送状态
        final Status sendStatus = message.getStatus();
        switch (sendStatus) {
            case SENDING:
                progressBar.setVisibility(View.VISIBLE);
                progressLayout.setBackgroundColor(Color.TRANSPARENT);
                break;
            case SUCCESS:
                progressBar.setVisibility(View.GONE);
                progressLayout.setBackgroundColor(Color.TRANSPARENT);
                break;
            case FAILED:
                progressBar.setVisibility(View.GONE);
                progressLayout.setBackgroundResource(R.drawable.kf5_message_send_failed_img_drawable);
                progressLayout.setOnClickListener(new MessageResendListener(context, message));
                break;
        }
    }

    private class MessageResendListener implements View.OnClickListener {

        private IMMessage mIMMessage;
        private Context context;

        public MessageResendListener(Context context, IMMessage imMessage) {
            this.context = context;
            this.mIMMessage = imMessage;
        }

        @Override
        public void onClick(View v) {
            new DialogBox(context)
                    .setMessage(context.getString(R.string.kf5_resend_message_hint))
                    .setLeftButton(context.getString(R.string.kf5_cancel), null)
                    .setRightButton(context.getString(R.string.kf5_resend), new DialogBox.onClickListener() {
                        @Override
                        public void onClick(DialogBox dialog) {
                            dialog.dismiss();
                            if (context instanceof BaseChatActivity) {
                                if (mIMMessage != null) {
                                    BaseChatActivity chatActivity = (BaseChatActivity) context;
                                    String type = mIMMessage.getType();
                                    switch (type) {
                                        case Field.AI_SEND: {
                                            IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                            String content = mIMMessage.getMessage();
                                            chatActivity.removeMessage(mIMMessage);
                                            chatActivity.onSendAITextMessage(content);
                                        }
                                        break;
                                        case Field.CHAT_MSG: {
                                            IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                            String content = mIMMessage.getMessage();
                                            chatActivity.removeMessage(mIMMessage);
                                            chatActivity.onSendTextMessage(content);
                                        }
                                        break;
                                        case Field.CHAT_UPLOAD: {
                                            Upload upload = mIMMessage.getUpload();
                                            if (upload != null) {
                                                String uploadType = upload.getType();
                                                if (Utils.isImage(uploadType)) {
                                                    //图片
                                                    IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                                    String url = upload.getLocalPath();
                                                    if (!TextUtils.isEmpty(url)) {
                                                        chatActivity.removeMessage(mIMMessage);
                                                        chatActivity.onSendImageMessage(Collections.singletonList(new File(url)));
                                                    }
                                                } else if (Utils.isAMR(uploadType)) {
                                                    //语音
                                                    IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                                    String url = upload.getLocalPath();
                                                    String token = mIMMessage.getMessage();
                                                    if (!TextUtils.isEmpty(url)) {
                                                        chatActivity.removeMessage(mIMMessage);
                                                        chatActivity.onSendVoiceMessage(url, token);
                                                    }
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }).show();
        }

    }
}