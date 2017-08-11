package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;

/**
 * author:chosen
 * date:2016/10/26 15:05
 * email:812219713@qq.com
 */

class TextReceiveHolder extends AbstractHolder {

    private TextView contentText;

    private CircleImageView headImg;

    private TextView tvDate;


    TextReceiveHolder(View convertView) {
        super(convertView.getContext());
        headImg = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_text_head_img);
        contentText = (TextView) convertView.findViewById(R.id.kf5_message_item_with_text);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {

        try {
//            loadImage(headImg, R.drawable.kf5_agent);
            loadHeadImg(headImg,message.getUserId(),R.drawable.kf5_agent);
            loadTextData(message, contentText, position);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
