package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageTextLongListener;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.Utils;

/**
 * author:chosen
 * date:2016/10/25 17:48
 * email:812219713@qq.com
 */

class AIMessageReceiveHolder extends BaseContext {

    private TextView contentText;

    private CircleImageView headImg;

    private TextView tvDate;

    AIMessageReceiveHolder(View convertView) {
        super(convertView.getContext());
        headImg = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_text_head_img);
        contentText = (TextView) convertView.findViewById(R.id.kf5_message_item_with_text);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {

        try {
//            ImageLoaderManager.getInstance().displayImageWithUrl("drawable://" + getDrawableID("kf5_agent"), headImg);
            ImageLoaderManager.getInstance(context).displayImage(R.drawable.kf5_agent, headImg);
            CustomTextView.setTextWithAIMessage(context, contentText, message.getMessage());
            contentText.setOnLongClickListener(new MessageTextLongListener(context, message, position));
            if (position == 0) {
                if (message.getCreated() < 1) {
                    tvDate.setText(Utils.getAllTime(System.currentTimeMillis()));
                } else {
                    tvDate.setText(Utils.getAllTime(message.getCreated()));
                }
                tvDate.setVisibility(View.VISIBLE);
            } else {
                if (previousMessage != null && (message.getCreated() - previousMessage.getCreated()) > 2 * 60) {
                    tvDate.setText(Utils.getAllTime(message.getCreated()));
                    tvDate.setVisibility(View.VISIBLE);
                } else {
                    tvDate.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
