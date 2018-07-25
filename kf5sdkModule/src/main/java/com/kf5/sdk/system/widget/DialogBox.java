package com.kf5.sdk.system.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kf5.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/17 15:24
 * email:812219713@qq.com
 */

public class DialogBox {


    private Context context;

    private LinearLayout mainView, singleBtn, doubleBtn;

    private TextView dialogTitle;

    private TextView dialogText;

    private Dialog dialog;

    private List<String> btnNames;

    private onClickListener listeners[];

    private onItemClick itemClick = null;

    private ListView listView;

    private String title;

    private String message;


    @SuppressLint("InflateParams")
    public DialogBox(Context context) {

        this.context = context;
        mainView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.kf5_dialog_layout, null);
        dialogTitle = (TextView) mainView.findViewById(R.id.kf5_dialogTitle);
        dialogText = (TextView) mainView.findViewById(R.id.kf5_dialogText);
        dialogText.setVisibility(View.GONE);
        dialogTitle.setVisibility(View.GONE);
        btnNames = new ArrayList<>();
        listeners = new onClickListener[2];
        dialog = new Dialog(context, R.style.kf5messagebox_style);
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public DialogBox setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置消息内容
     *
     * @param text
     * @return
     */
    public DialogBox setMessage(String text) {

        this.message = text;
        return this;
    }

    /**
     * 点击对话框之外的区域或者后退是否能够取消显示
     *
     * @param cancelable
     * @return
     */
    public DialogBox setCancelAble(boolean cancelable) {
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        return this;
    }


    /**
     * 设置左边按钮的相关内容
     *
     * @param text
     * @param clickListener
     * @return
     */
    public DialogBox setLeftButton(String text, onClickListener clickListener) {

        btnNames.add(text);
        listeners[0] = clickListener;

        return this;
    }

    /**
     * 判断是否处于显示状态
     *
     * @return
     */
    public boolean isShowing() {
        return dialog.isShowing();
    }


    /**
     * 设置右边按钮的相关内容
     *
     * @param text
     * @param onClickListener
     * @return
     */
    public DialogBox setRightButton(String text, onClickListener onClickListener) {

        btnNames.add(text);
        listeners[1] = onClickListener;

        return this;
    }


    /**
     * 设置多选项的内容
     *
     * @param items
     * @param itemClick
     * @return
     */
//    public DialogBox setItems(CharSequence[] items, onItemClick itemClick) {
//
//        this.itemClick = itemClick;
//        listView = new ListView(context);
//        listView.setCacheColorHint(0x00000000);
//        listView.setAdapter(new ArrayAdapter<CharSequence>(context, R.layout.dia, items));
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT, 1);
//        listView.setLayoutParams(layoutParams);
//        listView.setOnItemClickListener(new ItemClickListener());
//        return this;
//    }

    /**
     * 显示对话框
     */
    @SuppressLint("InflateParams")
    public void show() {

        mainView.removeAllViews();
        if (!TextUtils.isEmpty(title)) {
            mainView.addView(dialogTitle);
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
        if (!TextUtils.isEmpty(message)) {
            mainView.addView(dialogText);
            dialogText.setVisibility(View.VISIBLE);
            dialogText.setText(message);
        }
        if (listView != null) {
            mainView.addView(listView);
        }
        //只创建了一个按钮
        if (btnNames.size() == 1) {
            singleBtn = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.kf5_message_box_single_btn, null);
            TextView dialogBtn = (TextView) singleBtn.findViewById(R.id.kf5_dialogBtn);
            dialogBtn.setText(btnNames.get(0));
            dialogBtn.setOnClickListener(new ClickListener(0));
            mainView.addView(singleBtn);
        } else if (btnNames.size() == 2) {
            doubleBtn = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.kf5_message_box_double_btn, null);
            TextView leftBtn = (TextView) doubleBtn.findViewById(R.id.kf5_dialogLeftBtn);
            TextView rightBtn = (TextView) doubleBtn.findViewById(R.id.kf5_dialogRightBtn);
            leftBtn.setText(btnNames.get(0));
            leftBtn.setOnClickListener(new ClickListener(0));
            rightBtn.setText(btnNames.get(1));
            rightBtn.setOnClickListener(new ClickListener(1));
            mainView.addView(doubleBtn);
        }
        dialog.setContentView(mainView);
        dialog.show();

    }

    /**
     * 隐藏对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }


    private class ClickListener implements View.OnClickListener {

        private int index;


        public ClickListener(int index) {
            super();
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (listeners[index] == null) {
                dismiss();
            } else {
                listeners[index].onClick(DialogBox.this);
            }

        }

    }


    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if (itemClick != null) {
                itemClick.onItemClickListener(DialogBox.this, position);
            }
        }

    }

    public interface onClickListener {

        void onClick(DialogBox dialog);
    }

    public interface onItemClick {

        void onItemClickListener(DialogBox dialog, int index);

    }

}
