package com.kf5.sdk.system.listener;

import android.content.Context;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.BaseLongClickListener;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;

/**
 * author:chosen
 * date:2016/10/20 16:53
 * email:812219713@qq.com
 */

public class CopyTextLongClickListener extends BaseLongClickListener {

    private String text;

    private DialogBox chatDialog;

    public CopyTextLongClickListener(Context context, String text) {
        super(context);
        this.text = text;
    }

    @Override
    public boolean onLongClick(View v) {

        if (chatDialog == null) {
            chatDialog = new DialogBox(context);
            chatDialog.setMessage(context.getString(R.string.kf5_copy_text_hint))
                    .setLeftButton(context.getString(R.string.kf5_cancel), null)
                    .setRightButton(context.getString(R.string.kf5_copy), new DialogBox.onClickListener() {
                        @Override
                        public void onClick(DialogBox dialog) {
                            dialog.dismiss();
                            Utils.copyText(text, context);
                        }
                    });
        }
        chatDialog.show();
        return true;
    }
}
