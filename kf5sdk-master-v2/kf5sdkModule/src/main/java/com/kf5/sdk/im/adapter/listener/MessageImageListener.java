package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.expression.utils.ImageBase;
import com.kf5.sdk.system.base.BaseClickListener;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.ui.ImageActivity;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.MD5Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/26 14:52
 * email:812219713@qq.com
 */

public class MessageImageListener extends BaseClickListener {

    private final IMMessage mIMMessage;

    public MessageImageListener(Context context, IMMessage imMessage) {
        super(context);
        this.mIMMessage = imMessage;
    }

    @Override
    public void onClick(View v) {

        try {
            if (mIMMessage != null) {
                Upload upload = mIMMessage.getUpload();
                if (upload != null) {
                    Intent intent = new Intent(context, ImageActivity.class);
                    List<String> pathList = new ArrayList<>();
                    String localPath = upload.getLocalPath();
                    //如果本地文件存在
                    if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
                        pathList.add(ImageBase.getImagePath(ImageBase.Scheme.FILE, context, localPath));
                    } else {
                        File file = new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(upload.getUrl()) + "." + upload.getType());
                        if (file.exists()) {
                            pathList.add(ImageBase.getImagePath(ImageBase.Scheme.FILE, context, file.getAbsolutePath()));
                        } else {
                            pathList.add(upload.getUrl());
                        }
                    }
                    intent.putExtra(Field.EXTRA_IMAGE_INDEX, 1);
                    intent.putStringArrayListExtra(Field.EXTRA_IMAGE_URLS, (ArrayList<String>) pathList);
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
