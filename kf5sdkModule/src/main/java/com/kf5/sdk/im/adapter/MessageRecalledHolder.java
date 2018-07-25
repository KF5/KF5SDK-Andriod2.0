package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2018/7/13 17:11
 * email:812219713@qq.com
 */

class MessageRecalledHolder extends AbstractHolder {

    private TextView textView;

    MessageRecalledHolder(View convertView) {
        super(convertView.getContext());
        textView = (TextView) convertView.findViewById(R.id.kf5_message_item_system);
        convertView.setTag(this);
    }

    public void bindData(String message) {
        try {
            textView.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
