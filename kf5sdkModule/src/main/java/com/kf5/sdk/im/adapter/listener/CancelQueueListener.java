package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.base.BaseClickListener;
import com.kf5.sdk.system.widget.DialogBox;

/**
 * author:chosen
 * date:2016/10/26 15:02
 * email:812219713@qq.com
 */

public class CancelQueueListener extends BaseClickListener {


    public CancelQueueListener(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        try {
            if (context instanceof BaseChatActivity) {
                new DialogBox(context)
                        .setMessage(context.getString(R.string.kf5_cancel_queue_leave_message_hint))
                        .setLeftButton(context.getString(R.string.kf5_cancel), null)
                        .setRightButton(context.getString(R.string.kf5_leave_message), new DialogBox.onClickListener() {
                            @Override
                            public void onClick(DialogBox dialog) {
                                dialog.dismiss();
                                BaseChatActivity chatActivity = (BaseChatActivity) context;
                                chatActivity.cancelQueueWaiting();
                            }
                        }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
