package com.kf5.sdk.system.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * author:chosen
 * date:2016/10/19 10:43
 * email:812219713@qq.com
 */

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;

    private List<T> mList;

    private LayoutInflater mLayoutInflater;

    public CommonAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    protected List<T> getDataList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return position >= 0 ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public abstract View getView(int position, View view, ViewGroup viewGroup);

    protected <T extends View> T findViewById(View convertView, int id) {
        //noinspection unchecked
        return (T) convertView.findViewById(id);
    }

    protected View inflateLayout(int layoutId, ViewGroup container) {
        return mLayoutInflater.inflate(layoutId, container, false);
    }

}

