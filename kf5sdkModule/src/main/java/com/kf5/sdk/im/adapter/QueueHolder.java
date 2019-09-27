package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.base.BaseClickListener;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.widget.DialogBox;

/**
 * author:chosen
 * date:2016/10/26 15:01
 * email:812219713@qq.com
 */

class QueueHolder extends BaseContext {

    private TextView tvContent, tvCancelQueue;

    QueueHolder(View view) {
        super(view.getContext());
        tvContent = (TextView) view.findViewById(R.id.kf5_message_item_queue_content);
        tvCancelQueue = (TextView) view.findViewById(R.id.kf5_message_item_cancel_queue);
        view.setTag(this);
    }

    public void bindData(IMMessage message, int position) {
        tvContent.setText(message.getMessage());
        tvCancelQueue.setOnClickListener(new CancelQueueListener(context));
    }

    class CancelQueueListener extends BaseClickListener {


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
}
