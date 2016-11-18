package com.kf5.sdk.im.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageImageListener;
import com.kf5.sdk.im.adapter.listener.MessageImageLongListener;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.utils.ImageUtils;
import com.kf5.sdk.im.utils.Utils;
import com.kf5.sdk.im.widget.CircleImageView;
import com.kf5.sdk.im.widget.MaskImage;
import com.kf5.sdk.system.base.BaseContext;
import com.kf5.sdk.system.utils.ByteArrayUtil;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.MD5Utils;

import java.io.File;

/**
 * author:chosen
 * date:2016/10/26 10:47
 * email:812219713@qq.com
 */

class ImageReceiveHolder extends BaseContext {

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

        final Upload upload;
        try {
            upload = message.getUpload();
            maskImage.setOnClickListener(new MessageImageListener(context, message));
            maskImage.setOnLongClickListener(new MessageImageLongListener(context, message, position));
            final String url = upload.getUrl();
            String localPath = upload.getLocalPath();
            if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
                File file = new File(localPath);
                ImageUtils.setImageSize(file.getAbsolutePath(), maskImage, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
                ImageLoaderManager.getInstance(context).displayImage("file://" + file.getAbsolutePath(), maskImage);
            } else if (!TextUtils.isEmpty(url) && url.startsWith("http") && new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(url) + "." + upload.getType()).exists()) {
                File file = new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                ImageUtils.setImageSize(file.getAbsolutePath(), maskImage, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
                IMSQLManager.updateLocalPathByTimeStamp(context, file.getAbsolutePath(), message.getTimeStamp());
                ImageLoaderManager.getInstance(context).displayImage("file://" + file.getAbsolutePath(), maskImage);
            } else if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
                ImageLoaderManager.getInstance(context).displayImage(url, maskImage, upload.getWidth(), upload.getHeight(), new RequestListener<String, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        File file = new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                        ByteArrayUtil.cacheBitmapToSDCard(resource, upload.getType(), file);
                        upload.setLocalPath(file.getAbsolutePath());
                        IMSQLManager.updateLocalPathByTimeStamp(context, file.getAbsolutePath(), message.getTimeStamp());
                        return false;
                    }
                });
            } else {
                ImageLoaderManager.getInstance(context).displayImage(R.drawable.kf5_image_loading_failed, maskImage);
            }
            ImageLoaderManager.getInstance(context).displayImage(R.drawable.kf5_agent, imageView);
            if (position == 0) {
                tvDate.setText(com.kf5.sdk.system.utils.Utils.getAllTime(message.getCreated()));
                tvDate.setVisibility(View.VISIBLE);
            } else {
                if (previousMessage != null && (message.getCreated() - previousMessage.getCreated()) > 2 * 60) {
                    tvDate.setText(com.kf5.sdk.system.utils.Utils.getAllTime(message.getCreated()));
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
