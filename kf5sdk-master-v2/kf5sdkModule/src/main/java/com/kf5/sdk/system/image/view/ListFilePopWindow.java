package com.kf5.sdk.system.image.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.kf5.sdk.R;
import com.kf5.sdk.system.image.utils.ScreenUtils;

/**
 * author:chosen
 * date:2016/10/18 10:28
 * email:812219713@qq.com
 */

public class ListFilePopWindow implements AdapterView.OnItemClickListener {
    private PopupWindow popupWindow;

    private View targetView;

    private ListView listView;

    private OnFileListPopWindowItemClickListener popwindowItemClickListener;

    public void setPopwindowItemClickListener(OnFileListPopWindowItemClickListener popwindowItemClickListener) {
        this.popwindowItemClickListener = popwindowItemClickListener;
    }


    public ListFilePopWindow(Context context, View targetView) {
        super();
        this.targetView = targetView;
        View contentView = LayoutInflater.from(context).inflate(R.layout.kf5_list_file_dir, null);
        listView = (ListView) contentView.findViewById(R.id.kf5_list_dir);
        Point point = ScreenUtils.getScreenSize(context);
        int height = (int) (point.y * (4.5f / 8.0f));
        popupWindow = new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(height);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });
        popupWindow.setAnimationStyle(R.style.KF5FileListPopAnim);
        popupWindow.setContentView(contentView);
        listView.setOnItemClickListener(this);
    }


    public void setListAdapter(ListAdapter adapter) {
        listView.setAdapter(adapter);
    }

    public boolean isShowing() {

        if (popupWindow == null) {
            return false;
        }
        return popupWindow.isShowing();

    }

    public void disMiss() {

        if (popupWindow != null) {
            popupWindow.dismiss();
        }

    }

    public void show() {

        if (popupWindow != null) {
            popupWindow.showAsDropDown(targetView, 0, 0);
//			popupWindow.showAtLocation(targetView, Gravity.BOTTOM, 0, 0);
        }

    }


    public interface OnFileListPopWindowItemClickListener {

        void onFileListItemCilck(AdapterView<?> adapterView, View view, int i, long l);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        disMiss();
        if (popwindowItemClickListener != null) {
            popwindowItemClickListener.onFileListItemCilck(parent, view, position, id);
        }
    }


}
