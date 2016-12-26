package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.base.BaseClickListener;
import com.kf5.sdk.system.widget.DialogBox;

import java.io.File;
import java.util.Collections;

/**
 * author:chosen
 * date:2016/10/26 15:09
 * email:812219713@qq.com
 */

public class MessageResendListener extends BaseClickListener {

    private IMMessage mIMMessage;

    public MessageResendListener(Context context, IMMessage imMessage) {
        super(context);
        this.mIMMessage = imMessage;
    }

    @Override
    public void onClick(View v) {
        try {
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
                                    switch (mIMMessage.getMessageType()) {
                                        case AI_MESSAGE: {
                                            IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                            String content = mIMMessage.getMessage();
                                            chatActivity.removeMessage(mIMMessage);
                                            chatActivity.onSendAITextMessage(content);
                                        }
                                        break;
                                        case TEXT: {
                                            IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                            String content = mIMMessage.getMessage();
                                            chatActivity.removeMessage(mIMMessage);
                                            chatActivity.onSendTextMessage(content);
                                        }
                                        break;
                                        case IMAGE: {
                                            IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                            Upload upload = mIMMessage.getUpload();
                                            if (upload != null) {
                                                String url = upload.getLocalPath();
                                                if (!TextUtils.isEmpty(url)) {
                                                    chatActivity.removeMessage(mIMMessage);
                                                    chatActivity.onSendImageMessage(Collections.singletonList(new File(url)));
                                                }
                                            }
                                        }

                                        break;
                                        case VOICE: {
                                            IMSQLManager.deleteMessageByTimeStamp(context, mIMMessage.getTimeStamp());
                                            Upload upload = mIMMessage.getUpload();
                                            if (upload != null) {
                                                String url = upload.getLocalPath();
                                                if (!TextUtils.isEmpty(url)) {
                                                    chatActivity.removeMessage(mIMMessage);
                                                    chatActivity.onSendVoiceMessage(url);
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
