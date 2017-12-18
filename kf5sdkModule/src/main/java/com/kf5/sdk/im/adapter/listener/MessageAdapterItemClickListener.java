package com.kf5.sdk.im.adapter.listener;

import android.text.TextUtils;
import android.view.View;

import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.MD5Utils;

import java.io.File;

/**
 * author:chosen
 * date:2016/10/26 15:14
 * email:812219713@qq.com
 */

public class MessageAdapterItemClickListener implements View.OnClickListener {

    private IMMessage imMessage;

    private int position;

    public MessageAdapterItemClickListener(IMMessage object, int position) {
        super();
        this.imMessage = object;
        this.position = position;
    }

    @Override
    public void onClick(View v) {

        try {
            Upload upload = imMessage.getUpload();
            String url = upload.getUrl();
            File localFile = new File(FilePath.SAVE_RECORDER + MD5Utils.GetMD5Code(url) + ".amr");
            if (localFile.exists()) {
                VoicePlayListener.getInstance().startPlay(localFile.getAbsolutePath());
            } else {
                File file = new File(FilePath.SAVE_RECORDER + upload.getName());
                if (file.exists()) {
                    VoicePlayListener.getInstance().startPlay(file.getAbsolutePath());
                } else if (!TextUtils.isEmpty(url)) {
                    VoicePlayListener.getInstance().startPlay(url);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
