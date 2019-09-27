package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;

/**
 * author:chosen
 * date:2016/10/26 15:02
 * email:812219713@qq.com
 */

class SystemHolder {

    private TextView textView;

    SystemHolder(View convertView) {
        textView = (TextView) convertView.findViewById(R.id.kf5_message_item_system);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {
        try {
            textView.setText(message.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
