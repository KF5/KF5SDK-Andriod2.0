package com.kf5.sdk.im.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageResendListener;
import com.kf5.sdk.im.adapter.listener.MessageTextLongListener;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.expression.utils.ExpressionCommonUtils;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.Utils;

/**
 * author:chosen
 * date:2016/10/26 15:07
 * email:812219713@qq.com
 */

class TextSendHolder extends BaseContext {

    private TextView contentText;

    private CircleImageView headImg;

    private ProgressBar progressBar;

    private RelativeLayout failLayout;

    private TextView tvDate;


    TextSendHolder(View convertView) {
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
            ImageLoaderManager.getInstance(context).displayImage(R.drawable.kf5_end_user, headImg);
//            CustomTextView.stripUnderlines(context, contentText, message.getMessage(), Linkify.ALL);
            ExpressionCommonUtils.spannableEmoticonFilter(contentText, message.getMessage());
            contentText.setOnLongClickListener(new MessageTextLongListener(context, message, position));
            if (message.getStatus() == Status.SENDING) {
                progressBar.setVisibility(View.VISIBLE);
                failLayout.setBackgroundColor(Color.TRANSPARENT);

            } else if (message.getStatus() == Status.SUCCESS) {
                progressBar.setVisibility(View.GONE);
                failLayout.setBackgroundColor(Color.TRANSPARENT);
            } else if (message.getStatus() == Status.FAILED) {
                progressBar.setVisibility(View.GONE);
                failLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.kf5_message_send_failed_img_drawable));
                failLayout.setOnClickListener(new MessageResendListener(context, message));
            }
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
