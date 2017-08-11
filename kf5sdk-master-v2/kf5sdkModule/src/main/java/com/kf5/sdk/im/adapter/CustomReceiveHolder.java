package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;

/**
 * author:chosen
 * date:2017/3/22 10:38
 * email:812219713@qq.com
 */

public class CustomReceiveHolder extends AbstractHolder {

    private TextView contentText;

    private CircleImageView headImg;

    private TextView tvDate;


    public CustomReceiveHolder(View itemView) {
        super(itemView.getContext());
        headImg = (CircleImageView) itemView.findViewById(R.id.kf5_message_item_head_img);
        contentText = (TextView) itemView.findViewById(R.id.kf5_message_item_with_custom_content);
        tvDate = (TextView) itemView.findViewById(R.id.kf5_tvDate);
        itemView.setTag(this);

    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {
        try {
//            loadImage(headImg, R.drawable.kf5_agent);
            loadHeadImg(headImg,message.getUserId(),R.drawable.kf5_agent);
            loadCustomData(message, contentText);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
