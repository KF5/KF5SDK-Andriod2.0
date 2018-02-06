package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.utils.LogUtil;

/**
 * author:chosen
 * date:2016/10/25 18:20
 * email:812219713@qq.com
 */

class AIMessageSendHolder extends AbstractHolder {

    private TextView contentText;

    private CircleImageView headImg;

    private ProgressBar progressBar;

    private RelativeLayout failLayout;

    private TextView tvDate;


    AIMessageSendHolder(View convertView) {
        super(convertView.getContext());
        headImg = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_text_head_img);
        contentText = (TextView) convertView.findViewById(R.id.kf5_message_item_with_text);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        progressBar = (ProgressBar) convertView.findViewById(R.id.kf5_progressbar);
        failLayout = (RelativeLayout) convertView.findViewById(R.id.kf5_progress_layout);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {

        try {
            loadImage(headImg, R.drawable.kf5_end_user);
//            loadAIData(message, contentText, position);
            loadTextData(message, contentText, position);
            dealMessageStatus(message, previousMessage, position, tvDate, progressBar, failLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

