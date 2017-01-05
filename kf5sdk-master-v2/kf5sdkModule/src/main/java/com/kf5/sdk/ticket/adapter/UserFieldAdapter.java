package com.kf5.sdk.ticket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.base.CommonAdapter;
import com.kf5.sdk.ticket.entity.UserField;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/21 15:36
 * email:812219713@qq.com
 */

public class UserFieldAdapter extends CommonAdapter<UserField> {

    public UserFieldAdapter(Context context, List<UserField> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflateLayout(R.layout.kf5_user_field_item, viewGroup);
            viewHolder.mName = findViewById(view, R.id.kf5_user_field_name);
            viewHolder.mValue = findViewById(view, R.id.kf5_user_field_value);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.bindData(getItem(position));

        return view;
    }

    private class ViewHolder {

        TextView mName, mValue;

        void bindData(UserField userField) {
            mName.setText(userField.getName());
            mValue.setText(userField.getValue());
        }
    }

}
