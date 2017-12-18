package com.kf5sdk.exam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5sdk.exam.R;
import com.kf5sdk.exam.entity.HelpAddress;

import java.util.List;

/**
 * author:chosen
 * date:2017/5/8 15:39
 * email:812219713@qq.com
 */

public class HelpAddressAdapter extends CommonAdapter<HelpAddress> {

    public HelpAddressAdapter(Context context, List<HelpAddress> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflateLayout(R.layout.help_address_item, viewGroup);
            viewHolder.tvName = (TextView) view.findViewById(R.id.help_address_item_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvName.setText(getItem(position).name);
        return view;
    }

    private class ViewHolder {

        TextView tvName;

    }
}
