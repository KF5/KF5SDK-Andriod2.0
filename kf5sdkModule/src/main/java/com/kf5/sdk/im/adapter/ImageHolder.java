package com.kf5.sdk.im.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chosen.imageviewer.manager.ImagePreviewManager;
import com.chosen.videoplayer.ui.VideoPlayActivity;
import com.kf5.sdk.R;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.utils.ImageUtils;
import com.kf5.sdk.im.utils.Utils;
import com.kf5.sdk.im.widget.MaskImage;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.widget.PopList;
import com.kf5.sdk.system.widget.drawable.PlaceHolderDrawable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Chosen
 * @create 2019/1/8 11:32
 * @email 812219713@qq.com
 */
class ImageHolder extends BaseHolder {

    private MaskImage imageView;

    @Nullable
    private BaseSendHolder sendHolder;
    private boolean isVideo;

    ImageHolder(MessageAdapter messageAdapter, View convertView) {
        super(messageAdapter, convertView);
        imageView = convertView.findViewById(R.id.kf5_message_item_with_image_content_img);
    }

    void initData(int position, boolean isReceive, boolean isVideo) {
        super.initData(position, isReceive);
        this.isVideo = isVideo;
        if (!isReceive) {
            sendHolder = new BaseSendHolder(messageAdapter, convertView);
        }
    }

    @Override
    protected void setUpUI() {
        super.setUpUI();
        MessageType messageType = isVideo ? MessageType.VIDEO : MessageType.IMAGE;
        if (sendHolder != null) {
            sendHolder.setUpSendMessageUI(message, messageType, position);
        }
        dealImage();
        imageView.setOnClickListener(new ImageClickListener(message, messageType));
        imageView.setOnLongClickListener(new PopList<>(imageView, new ItemLongClickCallback(message.getUpload())));
    }

    private void dealImage() {
        final Upload upload = message.getUpload();
        if (upload == null) {
            return;
        }
        boolean useCache = !TextUtils.isEmpty(upload.getLocalPath()) && new File(upload.getLocalPath()).exists();
        String url = useCache ? upload.getLocalPath() : upload.getUrl();
        if (!TextUtils.isEmpty(url)) {
            ImageUtils.ImageSize imageSize;
            if (upload.getWidth() > 0 && upload.getHeight() > 0) {
                imageSize = ImageUtils.setImageSize(upload.getWidth(), upload.getHeight(), imageView, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
            } else if (useCache) {
                if (isVideo) {
                    imageSize = ImageUtils.setSizeOfVideo(upload.getLocalPath(), imageView);
                } else {
                    imageSize = ImageUtils.setImageSize(upload.getLocalPath(), imageView, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
                }
            } else {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                imageSize = ImageUtils.setImageSize(dm.widthPixels, dm.heightPixels, imageView, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
                //glide3
//                Glide.with(context).load(url)
//                        .asBitmap()
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                final int width = resource.getWidth();
//                                final int height = resource.getHeight();
//                                upload.setWidth(width);
//                                upload.setHeight(height);
//                                IMSQLManager.updateImageSizeByMessageId(context, message.getMessageId(), width, height);
//                            }
//                        });
                //glide4
                Glide.with(context).asBitmap().load(url)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                final int width = resource.getWidth();
                                final int height = resource.getHeight();
                                upload.setWidth(width);
                                upload.setHeight(height);
                                IMSQLManager.updateImageSizeByMessageId(context, message.getMessageId(), width, height);
                            }
                        });
            }
            if (imageSize != null && imageSize.width != 0 && imageSize.height != 0) {
                //glide3
//                Glide.with(context)
//                        .load(url)
//                        .asBitmap()
//                        .placeholder(new PlaceHolderDrawable(context, imageSize.width, imageSize.height, isReceive))
//                        .into(imageView);
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .apply(new RequestOptions().override(imageSize.width, imageSize.height).placeholder(new PlaceHolderDrawable(context, imageSize.width, imageSize.height, isReceive, isVideo)).centerCrop())
                        .into(imageView);
            } else {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                ImageUtils.ImageSize size = ImageUtils.getThumbnailDisplaySize(dm.widthPixels, dm.heightPixels, Utils.getImageMaxEdge(context), Utils.getImageMinEdge(context));
//                Glide.with(context)
//                        .load(url)
//                        .asBitmap()
//                        .placeholder(new PlaceHolderDrawable(context, size.width, size.height, isReceive))
//                        .into(imageView);
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .apply(new RequestOptions().override(size.width, size.height).placeholder(new PlaceHolderDrawable(context, size.width, size.height, isReceive, isVideo)).centerCrop())
                        .into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.kf5_empty_photo).into(imageView);
        }
    }

    private class ImageClickListener implements View.OnClickListener {

        private final IMMessage message;
        private final MessageType messageType;

        ImageClickListener(IMMessage message, MessageType messageType) {
            this.message = message;
            this.messageType = messageType;
        }

        @Override
        public void onClick(View v) {
            switch (messageType) {
                case VIDEO: {
                    if (message != null) {
                        Upload upload = message.getUpload();
                        if (upload != null) {
                            String localPath = upload.getLocalPath();
                            if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
                                VideoPlayActivity.openVideoPlayActivity(context, localPath, new File(localPath).getName());
                            } else if (!TextUtils.isEmpty(upload.getUrl())) {
                                VideoPlayActivity.openVideoPlayActivity(context, upload.getUrl(), upload.getName());
                            } else {
                                Toast.makeText(context, R.string.kf5_video_error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                break;

                case IMAGE: {
                    List<IMMessage> dataList = messageAdapter.getDataList();
                    List<IMMessage> imageMessageList = new ArrayList<>();
                    List<String> imagePathList = new ArrayList<>();
                    for (IMMessage imMessage : dataList) {
                        Upload upload = imMessage.getUpload();
                        if (upload != null) {
                            String uploadType = upload.getType();
                            if (com.kf5.sdk.system.utils.Utils.isImage(uploadType)) {
                                String localPath = upload.getLocalPath();
                                if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
                                    imagePathList.add(localPath);
                                    imageMessageList.add(imMessage);
                                } else if (!TextUtils.isEmpty(upload.getUrl())) {
                                    imagePathList.add(upload.getUrl());
                                    imageMessageList.add(imMessage);
                                }
                            }
                        }
                    }
                    if (!imagePathList.isEmpty()) {
                        ImagePreviewManager.startPreviewImage(context, FilePath.IMAGES_PATH, true, imageMessageList.indexOf(message), imagePathList);
                    }
                }
            }
        }
    }

    private class ItemLongClickCallback implements PopList.OnClickCallback<PopList.PopListItem> {

        private final String DOWNLOAD = context.getResources().getString(R.string.kf5_download);

        private final Upload upload;

        ItemLongClickCallback(Upload upload) {
            this.upload = upload;
        }

        @Override
        public void onItemClick(PopList.PopListItem data) {
            String clickContent = data.getContent();
            if (TextUtils.equals(DOWNLOAD, clickContent)) {
                String localPath = upload.getLocalPath();
                if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
                    Toast.makeText(context, context.getString(R.string.kf5_file_downloaded), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.kf5_start_to_download), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public List<PopList.PopListItem> getListData() {
            return Collections.singletonList(new PopList.PopListItem(DOWNLOAD));
        }
    }
}
