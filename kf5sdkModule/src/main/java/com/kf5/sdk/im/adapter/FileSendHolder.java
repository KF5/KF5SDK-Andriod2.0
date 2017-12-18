package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;

/**
 * author:chosen
 * date:2016/10/26 10:45
 * email:812219713@qq.com
 */

class FileSendHolder extends AbstractHolder {

    public CircleImageView imageView;

    public TextView tvFileName;

    public TextView tvDate;

    private ProgressBar progressBar;

    private RelativeLayout failLayout;

    FileSendHolder(View convertView) {
        super(convertView.getContext());
        imageView = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_text_head_img);
        tvFileName = (TextView) convertView.findViewById(R.id.kf5_message_item_with_text);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        progressBar = (ProgressBar) convertView.findViewById(R.id.kf5_progressbar);
        failLayout = (RelativeLayout) convertView.findViewById(R.id.kf5_progress_layout);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {
        try {
            loadImage(imageView, R.drawable.kf5_end_user);
            loadFileData(message, tvFileName, progressBar, failLayout);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
