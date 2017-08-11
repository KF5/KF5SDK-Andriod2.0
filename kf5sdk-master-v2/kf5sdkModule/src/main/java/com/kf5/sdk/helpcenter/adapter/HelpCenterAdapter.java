package com.kf5.sdk.helpcenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.entity.HelpCenterItem;
import com.kf5.sdk.system.base.CommonAdapter;

import java.util.List;

/**
 * 
 * author:chosen
 * date:2016/10/19 10:50
 * email:812219713@qq.com
 */

public class HelpCenterAdapter extends CommonAdapter<HelpCenterItem> {

    public HelpCenterAdapter(Context context, List<HelpCenterItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflateLayout(R.layout.kf5_help_list_item, viewGroup);
            holder.mTextView = findViewById(view, R.id.kf5_help_list_item_title);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();
        holder.bindData(getItem(i));
        return view;
    }


    private class ViewHolder {
        TextView mTextView;

        void bindData(HelpCenterItem helpCenterItem) {
            mTextView.setText(helpCenterItem.getTitle());
        }

    }


}
