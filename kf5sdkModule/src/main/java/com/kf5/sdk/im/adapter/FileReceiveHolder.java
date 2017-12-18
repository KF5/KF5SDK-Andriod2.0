package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;

/**
 * author:chosen
 * date:2016/10/25 18:31
 * email:812219713@qq.com
 */

class FileReceiveHolder extends AbstractHolder {

    public CircleImageView imageView;

    public TextView tvFileName;

    public TextView tvDate;


    FileReceiveHolder(View convertView) {
        super(convertView.getContext());
        imageView = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_text_head_img);
        tvFileName = (TextView) convertView.findViewById(R.id.kf5_message_item_with_text);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage) {
        try {
//            loadImage(imageView, R.drawable.kf5_agent);
             loadHeadImg(imageView,message.getUserId(),R.drawable.kf5_agent);
            loadFileData(message, tvFileName, null, null);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
