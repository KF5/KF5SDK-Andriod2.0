package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.system.base.BaseLongClickListener;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;

/**
 * author:chosen
 * date:2016/10/25 18:16
 * email:812219713@qq.com
 */

public class MessageTextLongListener extends BaseLongClickListener {

    private int position;

    private IMMessage message;

    public MessageTextLongListener(Context context, IMMessage imMessage, int position) {
        super(context);
        this.position = position;
        this.message = imMessage;
    }

    @Override
    public boolean onLongClick(View v) {

        try {
            new DialogBox(context).setMessage(context.getString(R.string.kf5_copy_text_hint))
                    .setLeftButton(context.getString(R.string.kf5_cancel), null)
                    .setRightButton(context.getString(R.string.kf5_copy), new DialogBox.onClickListener() {
                        @Override
                        public void onClick(DialogBox dialog) {
                            dialog.dismiss();
                            Utils.copyText(message.getMessage(), context);
                            showToast(context.getString(R.string.kf5_copied));
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
