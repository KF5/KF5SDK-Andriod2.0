package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.base.BaseLongClickListener;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.widget.DialogBox;

import java.io.File;

/**
 * author:chosen
 * date:2016/10/26 10:43
 * email:812219713@qq.com
 */

public class MessageFileLongClickListener extends BaseLongClickListener {

    private IMMessage message;

    private DialogBox chatDialog;

    public MessageFileLongClickListener(Context context, IMMessage message) {
        super(context);
        this.message = message;
    }

    @Override
    public boolean onLongClick(View v) {
        final Upload upload = message.getUpload();
        if (upload != null) {
            final String url = upload.getUrl();
            if (!TextUtils.isEmpty(url)) {
                final File file = new File(FilePath.FILE + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                if (file.exists()) {
                    Toast.makeText(context, context.getString(R.string.kf5_file_downloaded), Toast.LENGTH_SHORT).show();
                } else {
                    if (chatDialog == null) {
                        chatDialog = new DialogBox(context);
                        chatDialog.setMessage(context.getString(R.string.kf5_download_file_hint))
                                .setLeftButton(context.getString(R.string.kf5_cancel), null)
                                .setRightButton(context.getString(R.string.kf5_download), new DialogBox.onClickListener() {

                                    @Override
                                    public void onClick(DialogBox dialog) {
                                        dialog.dismiss();
                                        Toast.makeText(context, context.getString(R.string.kf5_start_to_download), Toast.LENGTH_SHORT).show();
//                                        HttpRequestManager.getInstance(context).downloadFile(url, FilePath.FILE, file.getName(), new FileDownLoadCallBack() {
//                                            @Override
//                                            public void onResult(String result) {
//                                                if (context != null && context instanceof Activity) {
//                                                    ((Activity) context).runOnUiThread(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(context, "文件已下载\n" + FilePath.FILE, Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                }
//                                            }
//                                        });
                                    }
                                });
                    }
                    chatDialog.show();
                }
            }
        }
        return true;
    }
}
