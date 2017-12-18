package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;

/**
 * author:chosen
 * date:2017/3/22 11:09
 * email:812219713@qq.com
 */

public class CustomSendHolder extends AbstractHolder {

    private TextView contentText;
    private CircleImageView headImg;
    private TextView tvDate;


    public CustomSendHolder(View convertView) {
        super(convertView.getContext());
        headImg = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_head_img);
        contentText = (TextView) convertView.findViewById(R.id.kf5_message_item_with_custom_content);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {

        try {
            loadImage(headImg, R.drawable.kf5_end_user);
            loadCustomData(message, contentText);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}