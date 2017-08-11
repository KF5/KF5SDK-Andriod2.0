package com.kf5.sdk.system.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/10/17 16:19
 * email:812219713@qq.com
 */

public class ProgressDialog extends Dialog implements DialogInterface.OnKeyListener {

    View view;
    TextView dialogText;
    ImageView imageView;
    AnimationDrawable animationDrawable;
    private Context context;

    public void setDialogDismissListener(DialogDismissListener dialogDismissListener) {
        this.dialogDismissListener = dialogDismissListener;
    }

    private DialogDismissListener dialogDismissListener;

    public ProgressDialog(Context context) {
        this(context, R.style.kf5messagebox_style);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init(context);
    }

    /**
     * 初始化dialog
     *
     * @param context
     */
    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.kf5_progress_bar, null);
        dialogText = (TextView) view.findViewById(R.id.kf5_progress_dialog_text);
        imageView = (ImageView) view.findViewById(R.id.kf5_image_view);
        imageView.setImageResource(R.drawable.kf5_loading_anim_drawable);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        setContentView(view);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(this);
    }

    public ProgressDialog setContent(String content) {
        if (!TextUtils.isEmpty(content))
            dialogText.setText(content);
        return this;
    }


    /**
     * 点击外部或者后退键时候可取消显示
     *
     * @param cancelable
     * @return
     */
    public ProgressDialog setCancelAble(boolean cancelable) {
        setCanceledOnTouchOutside(cancelable);
        setCancelable(cancelable);
        return this;
    }

    @Override
    public void show() {
        try {
            if (!isShowing()) {
                super.show();
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismiss() {
        try {
            super.dismiss();
            if (animationDrawable != null) {
                animationDrawable.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isShowing()) {
            if (dialogDismissListener != null) {
                dialogDismissListener.onDismiss();
            }
        }
        return false;
    }

    public interface DialogDismissListener {
        void onDismiss();
    }

}
