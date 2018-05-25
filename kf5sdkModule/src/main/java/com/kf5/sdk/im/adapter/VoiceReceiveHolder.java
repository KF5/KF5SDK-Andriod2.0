package com.kf5.sdk.im.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageAdapterItemClickListener;
import com.kf5.sdk.im.api.FileDownLoadCallBack;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.utils.LogUtil;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/26 15:11
 * email:812219713@qq.com
 */

class VoiceReceiveHolder extends AbstractHolder {

    private TextView textViewLength;

    private CircleImageView headImg;

    private ProgressBar progressBar;

    private TextView tvDate;

    private ImageView mImageView;

    VoiceReceiveHolder(View convertView) {
        super(convertView.getContext());
        headImg = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_voice_head_img);
        textViewLength = (TextView) convertView.findViewById(R.id.kf5_message_item_with_voice);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        progressBar = (ProgressBar) convertView.findViewById(R.id.kf5_progressbar);
        mImageView = (ImageView) convertView.findViewById(R.id.kf5_message_item_with_voice_play_img);
        convertView.setTag(this);
    }

    public void bindData(final IMMessage message, int position, IMMessage previousMessage, final Map<String, IMMessage> downLoadList, FileDownLoadCallBack callBack) {

        try {
            loadVoiceData(position, message, textViewLength, progressBar, downLoadList, callBack, mImageView, MessageAdapterItemClickListener.VoiceType.LEFT);
//            loadImage(headImg, R.drawable.kf5_agent);
            loadHeadImg(headImg, message.getUserId(), R.drawable.kf5_agent);
            dealDate(position, tvDate, message, previousMessage);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.printf("发音接收方可能有异常", e);
        }
    }

}
