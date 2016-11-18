package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.CancelQueueListener;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.system.base.BaseContext;

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

}
