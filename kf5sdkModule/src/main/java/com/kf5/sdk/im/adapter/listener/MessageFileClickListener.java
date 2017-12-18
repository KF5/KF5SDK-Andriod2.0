package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.TextUtils;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.base.BaseClickListener;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;

import java.io.File;

/**
 * author:chosen
 * date:2016/10/26 10:41
 * email:812219713@qq.com
 */

public class MessageFileClickListener extends BaseClickListener {

    private IMMessage message;

    private DialogBox chatDialog;

    public MessageFileClickListener(Context context, IMMessage message) {
        super(context);
        this.message = message;
    }

    @Override
    public void onClick(View v) {

        if (chatDialog == null) {
            chatDialog = new DialogBox(context);
            chatDialog.setMessage(context.getString(R.string.kf5_open_file_hint))
                    .setLeftButton(context.getString(R.string.kf5_cancel), null)
                    .setRightButton(context.getString(R.string.kf5_open), new DialogBox.onClickListener() {

                        @Override
                        public void onClick(DialogBox dialog) {
                            dialog.dismiss();
                            Upload upload = message.getUpload();
                            if (upload == null) {
                                return;
                            }
                            String url = upload.getUrl();
                            if (!TextUtils.isEmpty(url)) {
                                File file = new File(FilePath.FILE + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                                if (file.exists()) {
                                    //直接打开
                                    Uri uri = Uri.parse(file.getAbsolutePath());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                                    if (Utils.isIntentAvailable(context, intent)) {
                                        context.startActivity(intent);
                                    } else {
                                        showToast(context.getString(R.string.kf5_no_file_found_hint));
                                    }
                                } else {
                                    showToast(context.getString(R.string.kf5_download_file));
                                }
                            }

                        }
                    });
        }
        chatDialog.show();
    }
}
