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

class SystemHolder extends AbstractHolder {

    private TextView textView;

//    private TextView tvDate;

    SystemHolder(View convertView) {
        super(convertView.getContext());
        textView = (TextView) convertView.findViewById(R.id.kf5_message_item_system);
//        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {
        try {
            textView.setText(message.getMessage());
//            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
