package com.kf5.sdk.system.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.kf5.sdk.system.widget.ProgressDialog;

/**
 * author:chosen
 * date:2016/10/19 10:22
 * email:812219713@qq.com
 */

public class DialogHandler extends Handler {

    public static final int SHOW_DIALOG = 1;

    public static final int DISMISS_DIALOG = 2;

    private ProgressDialog mProgressDialog;

    private Context mContext;

    private DialogDismissListener mDialogDismissListener;

    private String content;

    private boolean showDialog;

    public DialogHandler(Context context, DialogDismissListener dialogDismissListener, String content, boolean showDialog) {
        mContext = context;
        this.showDialog = showDialog;
        mDialogDismissListener = dialogDismissListener;
        this.content = content;
    }

    private void initProgressDialog() {
        if (showDialog) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(mContext);
                if (!TextUtils.isEmpty(content))
                    mProgressDialog.setContent(content);
                mProgressDialog.setDialogDismissListener(new ProgressDialog.DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (mDialogDismissListener != null)
                            mDialogDismissListener.dismissDialog();
                    }
                });
            }
            mProgressDialog.show();
        }
    }


    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SHOW_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_DIALOG:
                dismissDialog();
                break;
        }
    }

    public interface DialogDismissListener {
        void dismissDialog();
    }
}
