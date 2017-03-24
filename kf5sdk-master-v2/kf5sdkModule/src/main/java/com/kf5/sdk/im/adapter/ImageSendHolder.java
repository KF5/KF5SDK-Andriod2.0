package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.im.widget.MaskImage;

/**
 * author:chosen
 * date:2016/10/26 14:56
 * email:812219713@qq.com
 */

class ImageSendHolder extends AbstractHolder {

    private CircleImageView imageView;

    private MaskImage maskImage;

    private ProgressBar progressBar;

    private RelativeLayout relativeLayout;

    private TextView tvDate;


    ImageSendHolder(View convertView) {
        super(convertView.getContext());
        imageView = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_image_head_img);
        maskImage = (MaskImage) convertView.findViewById(R.id.kf5_message_item_with_image_content_img);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        progressBar = (ProgressBar) convertView.findViewById(R.id.kf5_progressbar);
        relativeLayout = (RelativeLayout) convertView.findViewById(R.id.kf5_progress_layout);
        convertView.setTag(this);
    }


    public void bindData(final IMMessage message, int position, IMMessage previousMessage, final BaseAdapter baseAdapter) {
        try {
            loadImageData(position, message, maskImage);
            loadImage(imageView, R.drawable.kf5_end_user);
            dealMessageStatus(message, previousMessage, position, tvDate, progressBar, relativeLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
