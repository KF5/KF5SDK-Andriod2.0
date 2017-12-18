package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.im.widget.MaskImage;

/**
 * author:chosen
 * date:2016/10/26 10:47
 * email:812219713@qq.com
 */

class ImageReceiveHolder extends AbstractHolder {

    private CircleImageView imageView;

    private MaskImage maskImage;

    private TextView tvDate;

    ImageReceiveHolder(View convertView) {
        super(convertView.getContext());
        imageView = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_image_head_img);
        maskImage = (MaskImage) convertView.findViewById(R.id.kf5_message_item_with_image_content_img);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(final IMMessage message, int position, IMMessage previousMessage, final BaseAdapter baseAdapter) {

        try {
            loadImageData(position, message, maskImage);

//            loadImage(imageView, R.drawable.kf5_agent);
            loadHeadImg(imageView,message.getUserId(),R.drawable.kf5_agent);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
