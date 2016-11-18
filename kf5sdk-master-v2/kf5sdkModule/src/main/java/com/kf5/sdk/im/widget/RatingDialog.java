package com.kf5.sdk.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/10/17 16:25
 * email:812219713@qq.com
 */

public class RatingDialog {


    private Dialog dialog;

    private View mainView;

    private LayoutInflater inflater;

    private TextView tvSatisfied, tvUnSatisfied, tvCancel, tvTitle;

    public RatingDialog setListener(OnRatingItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    private OnRatingItemClickListener listener;


    public RatingDialog(Context context) {
        inflater = LayoutInflater.from(context);
        dialog = new Dialog(context, R.style.kf5messagebox_style);
        mainView = inflater.inflate(R.layout.kf5_rating_layout, null, false);
        tvCancel = (TextView) mainView.findViewById(R.id.kf5_rating_cancel);
        tvCancel.setOnClickListener(new RatingItemClickListener(-1));
        tvSatisfied = (TextView) mainView.findViewById(R.id.kf5_rating_satisfied);
        tvSatisfied.setOnClickListener(new RatingItemClickListener(1));
        tvUnSatisfied = (TextView) mainView.findViewById(R.id.kf5_rating_unsatisfied);
        tvUnSatisfied.setOnClickListener(new RatingItemClickListener(0));
        tvTitle = (TextView) mainView.findViewById(R.id.kf5_dialogText);
        dialog.setContentView(mainView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }


    public void setTitle(String title) {

        if (tvTitle != null && !TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

    }


    /**
     * 显示评价对话框
     */
    public void show() {

        dialog.show();

    }


    /**
     * 是否处于显示状态
     *
     * @return
     */
    public boolean isShow() {

        return dialog.isShowing();
    }


    /**
     * 隐藏对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnRatingItemClickListener {

        /**
         * 回调接口 0，满意；1不满意；-1取消
         *
         * @param dialog 对话框
         * @param index  点击位置
         */
        void onRatingClick(RatingDialog dialog, int index);

    }

    class RatingItemClickListener implements View.OnClickListener {

        private int index;

        public RatingItemClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {

            switch (index) {
                case 1:
                    //满意；
                    if (listener != null)
                        listener.onRatingClick(RatingDialog.this, 1);
                    break;
                case 0:
                    //不满意
                    if (listener != null)
                        listener.onRatingClick(RatingDialog.this, 0);
                    break;
                case -1:
                    //取消
                    if (listener != null)
                        listener.onRatingClick(RatingDialog.this, -1);
                    break;
            }

        }
    }

}
