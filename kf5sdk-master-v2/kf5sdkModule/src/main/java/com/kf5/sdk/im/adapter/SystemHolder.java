package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.utils.Utils;

/**
 * author:chosen
 * date:2016/10/26 15:02
 * email:812219713@qq.com
 */

class SystemHolder extends BaseContext {

    private TextView textView;

    private TextView tvDate;

    SystemHolder(View convertView) {
        super(convertView.getContext());
        textView = (TextView) convertView.findViewById(R.id.kf5_message_item_system);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {
        try {
            textView.setText(message.getMessage());
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
