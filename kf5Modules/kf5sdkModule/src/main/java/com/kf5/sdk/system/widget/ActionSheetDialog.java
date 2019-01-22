package com.kf5.sdk.system.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kf5.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/17 14:41
 * email:812219713@qq.com
 * 底部弹层对话框
 */

public class ActionSheetDialog {

    private Context mContext;

    private Dialog mDialog;

    private TextView mTextTitle;

    private TextView mTextCancel;

    private LinearLayout mLayoutContent;

    private ScrollView mScrollViewContent;

    private boolean showTitle;

    private List<SheetItem> mSheetItemList;

    private Display mDisplay;

    public ActionSheetDialog(Context context) {
        mContext = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();
    }

    public ActionSheetDialog builder() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.kf5_toast_view_actionsheet, null);
        view.setMinimumWidth(mDisplay.getWidth());

        mScrollViewContent = (ScrollView) view.findViewById(R.id.kf5_sLayout_content);
        mLayoutContent = (LinearLayout) view.findViewById(R.id.kf5_lLayout_content);
        mTextTitle = (TextView) view.findViewById(R.id.kf5_txt_title);
        mTextCancel = (TextView) view.findViewById(R.id.kf5_txt_cancel);
        mTextCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        mDialog = new Dialog(mContext, R.style.KF5ActionSheetDialogStyle);
        mDialog.setContentView(view);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);

        return this;
    }

    public ActionSheetDialog setTitle(String title) {
        showTitle = true;
        mTextTitle.setVisibility(View.VISIBLE);
        mTextTitle.setText(title);
        return this;
    }

    public ActionSheetDialog setCancelable(boolean cancelAble) {
        mDialog.setCancelable(cancelAble);
        return this;
    }

    public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * @param stringItem item内容
     * @param color      颜色
     * @param listener   监听事件
     * @return
     */
    public ActionSheetDialog addSheetItem(String stringItem, SheetItemColor color,
                                          OnSheetItemClickListener listener) {
        if (mSheetItemList == null)
            mSheetItemList = new ArrayList<>();
        mSheetItemList.add(new SheetItem(stringItem, listener, color));
        return this;
    }

    private void setSheetItems() {
        if (mSheetItemList == null || mSheetItemList.size() <= 0)
            return;
        int size = mSheetItemList.size();
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mScrollViewContent.getLayoutParams();
            params.height = mDisplay.getHeight() / 2;
            mScrollViewContent.setLayoutParams(params);
        }

        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = mSheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;
            final OnSheetItemClickListener listener = sheetItem.mItemClickListener;
            TextView textView = new TextView(mContext);
            textView.setText(strItem);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);

            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.kf5_actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.kf5_actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.kf5_actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.kf5_actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.kf5_actionsheet_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.kf5_actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.kf5_actionsheet_bottom_selector);
                    }
                }
            }

            if (color == null) {
                textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                textView.setTextColor(Color.parseColor(color.getName()));
            }

            float scale = mContext.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, height));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(index);
                    mDialog.dismiss();
                }
            });

            mLayoutContent.addView(textView);
        }
    }

    public void show() {
        setSheetItems();
        mDialog.show();
    }


    public class SheetItem {

        String name;

        OnSheetItemClickListener mItemClickListener;

        SheetItemColor color;

        public SheetItem(String name, OnSheetItemClickListener itemClickListener, SheetItemColor sheetItemColor) {
            this.name = name;
            mItemClickListener = itemClickListener;
            color = sheetItemColor;
        }
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public enum SheetItemColor {
        Blue("#037BFF"), Red("#FD4A2E");

        private String name;

        SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
