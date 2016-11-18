package com.kf5.sdk.im.adapter;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageFileClickListener;
import com.kf5.sdk.im.adapter.listener.MessageFileLongClickListener;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.Utils;

/**
 * author:chosen
 * date:2016/10/26 10:45
 * email:812219713@qq.com
 */

class FileSendHolder extends BaseContext {

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
            ImageLoaderManager.getInstance(context).displayImage(R.drawable.kf5_end_user, imageView);
            tvFileName.setText(Html.fromHtml("<a href=\"\">" + message.getUpload().getName() + "</a>"));
            progressBar.setVisibility(View.GONE);
            failLayout.setBackgroundColor(Color.TRANSPARENT);
            if (position == 0) {
                tvDate.setText(Utils.getAllTime(message.getCreated()));
                tvDate.setVisibility(View.VISIBLE);
            } else {
                if (previousMessage != null && (message.getCreated() - previousMessage.getCreated()) > 2 * 60) {
                    tvDate.setText(Utils.getAllTime(message.getCreated()));
                    tvDate.setVisibility(View.VISIBLE);
                } else {
                    tvDate.setVisibility(View.GONE);
                }
            }
            tvFileName.setOnClickListener(new MessageFileClickListener(context, message));
            tvFileName.setOnLongClickListener(new MessageFileLongClickListener(context, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
