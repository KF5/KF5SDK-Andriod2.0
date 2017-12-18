package com.kf5.sdk.ticket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.ticket.entity.CheckItem;

import java.util.List;

/**
 * author:chosen
 * date:2017/1/4 14:11
 * email:812219713@qq.com
 */

public class CheckAdapter extends CommonAdapter<CheckItem> {


    public CheckAdapter(Context context, List<CheckItem> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflateLayout(R.layout.kf5_check_list_item, viewGroup);
            holder = new ViewHolder();
            holder.mContent = (TextView) view.findViewById(R.id.kf5_check_item_tv);
            holder.mImage = (ImageView) view.findViewById(R.id.kf5_check_item_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CheckItem checkItem = getItem(position);
        holder.mContent.setText(checkItem.getContent());
        if (checkItem.isSelected()) {
            holder.mImage.setVisibility(View.VISIBLE);
        } else {
            holder.mImage.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private class ViewHolder {

        TextView mContent;

        ImageView mImage;

    }

}
