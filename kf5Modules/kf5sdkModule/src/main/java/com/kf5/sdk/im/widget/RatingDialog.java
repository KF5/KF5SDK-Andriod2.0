package com.kf5.sdk.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kf5.sdk.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/17 16:25
 * email:812219713@qq.com
 */

public class RatingDialog {


    private Dialog dialog;

    private View mainView;

    private LayoutInflater inflater;

    private TextView
//            tvSatisfied, tvUnSatisfied,
            tvCancel, tvTitle;

    private ListView mListView;

    public RatingDialog setListener(OnRatingItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    private OnRatingItemClickListener listener;


    public RatingDialog(Context context, int ratingLevelCount) {
        inflater = LayoutInflater.from(context);
        dialog = new Dialog(context, R.style.kf5messagebox_style);
        mainView = inflater.inflate(R.layout.kf5_rating_layout, null, false);
        tvCancel = (TextView) mainView.findViewById(R.id.kf5_rating_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvTitle = (TextView) mainView.findViewById(R.id.kf5_dialogText);
        mListView = (ListView) mainView.findViewById(R.id.kf5_rating_list_view);
        List<String> ratingList;
        if (ratingLevelCount == 2) {
            ratingList = Arrays.asList(context.getResources().getStringArray(R.array.kf5_rating_status_count_2));
        } else if (ratingLevelCount == 3) {
            ratingList = Arrays.asList(context.getResources().getStringArray(R.array.kf5_rating_status_count_3));
        } else {
            ratingList = Arrays.asList(context.getResources().getStringArray(R.array.kf5_rating_status_count_5));
        }
        Collections.reverse(ratingList);
        final RatingItemAdapter adapter = new RatingItemAdapter(ratingList);
        mListView.setAdapter(adapter);
        final List<String> targetList = Arrays.asList(context.getResources().getStringArray(R.array.kf5_im_rating_status));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = adapter.getItem(position);
                if (listener != null) {
                    listener.onRatingClick(RatingDialog.this, targetList.indexOf(content));
                }
            }
        });
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

    private class RatingItemAdapter extends BaseAdapter {

        private List<String> mList;

        public RatingItemAdapter(List<String> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kf5_im_rating_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.kf5_im_rating_item_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvContent.setText(getItem(position));

            return convertView;
        }

        class ViewHolder {
            TextView tvContent;
        }
    }
}
