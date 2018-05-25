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
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.Utils;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
