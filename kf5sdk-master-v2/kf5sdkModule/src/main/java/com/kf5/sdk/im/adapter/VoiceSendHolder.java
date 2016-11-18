package com.kf5.sdk.im.adapter;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageAdapterItemClickListener;
import com.kf5.sdk.im.adapter.listener.MessageResendListener;
import com.kf5.sdk.im.api.FileDownLoadCallBack;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.internet.DownLoadManager;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/26 15:15
 * email:812219713@qq.com
 */

class VoiceSendHolder extends BaseContext {

    private TextView textViewLength;

    private CircleImageView headImg;

    private MediaPlayer mediaPlayer;

    private RelativeLayout relativeLayout;

    private ProgressBar progressBar;

    private TextView tvDate;


    VoiceSendHolder(View convertView) {
        super(convertView.getContext());
        headImg = (CircleImageView) convertView.findViewById(R.id.kf5_message_item_with_voice_head_img);
        textViewLength = (TextView) convertView.findViewById(R.id.kf5_message_item_with_voice);
        tvDate = (TextView) convertView.findViewById(R.id.kf5_tvDate);
        progressBar = (ProgressBar) convertView.findViewById(R.id.kf5_progressbar);
        relativeLayout = (RelativeLayout) convertView.findViewById(R.id.kf5_progress_layout);
        convertView.setTag(this);
    }

    public void bindData(IMMessage message, int position, IMMessage previousMessage, List<String> downLoadList, FileDownLoadCallBack callBack) {

        try {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textViewLength.measure(w, h);
            int width = textViewLength.getMeasuredWidth();
            textViewLength.setOnClickListener(new MessageAdapterItemClickListener(message, position));
            Upload upload = message.getUpload();
            String url = upload.getUrl();
            File localFile = new File(FilePath.SAVE_RECORDER + MD5Utils.GetMD5Code(url) + ".amr");
            if (localFile.exists()) {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(localFile.getAbsolutePath()));
                if (mediaPlayer != null) {
                    int length = mediaPlayer.getDuration() / 1000 + 1;
                    //noinspection StringBufferReplaceableByString
                    textViewLength.setText(new StringBuilder().append(length).append("''").toString());
                    double maxWidth = Utils.getDisplayWidth(context) / 3 * 2;
                    double percentWidth = (maxWidth - width) / 60;
                    ViewGroup.LayoutParams layoutParams = textViewLength.getLayoutParams();
                    layoutParams.width = (int) (width + percentWidth * length);
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    textViewLength.setLayoutParams(layoutParams);
                }
            } else {
                File file = new File(FilePath.SAVE_RECORDER + upload.getName());
                if (file.exists()) {
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(file.getAbsolutePath()));
                    if (mediaPlayer != null) {
                        int length = mediaPlayer.getDuration() / 1000 + 1;
                        //noinspection StringBufferReplaceableByString
                        textViewLength.setText(new StringBuilder().append(length).append("''").toString());
                        double maxWidth = Utils.getDisplayWidth(context) / 3 * 2;
                        double percentWidth = (maxWidth - width) / 60;
                        ViewGroup.LayoutParams layoutParams = textViewLength.getLayoutParams();
                        layoutParams.width = (int) (width + percentWidth * length);
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        textViewLength.setLayoutParams(layoutParams);
                    }
                } else {
                    if (!downLoadList.contains(upload.getName())) {
                        downLoadList.add(upload.getName());
                        DownLoadManager.getInstance().downloadFile(upload.getUrl(), FilePath.SAVE_RECORDER, upload.getName(), callBack);
                    }
                }
            }
            ImageLoaderManager.getInstance(context).displayImage(R.drawable.kf5_end_user, headImg);
            if (message.getStatus() == Status.SENDING) {
                progressBar.setVisibility(View.VISIBLE);
                relativeLayout.setBackgroundColor(Color.TRANSPARENT);
            } else if (message.getStatus() == Status.SUCCESS) {
                progressBar.setVisibility(View.GONE);
                relativeLayout.setBackgroundColor(Color.TRANSPARENT);
            } else if (message.getStatus() == Status.FAILED) {
                progressBar.setVisibility(View.GONE);
                relativeLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.kf5_message_send_failed_img_drawable));
                relativeLayout.setOnClickListener(new MessageResendListener(context, message));
            }
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

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

