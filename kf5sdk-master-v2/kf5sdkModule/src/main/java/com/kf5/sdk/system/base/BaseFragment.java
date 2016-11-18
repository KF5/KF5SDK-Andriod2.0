package com.kf5.sdk.system.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author:chosen
 * date:2016/10/26 15:31
 * email:812219713@qq.com
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View mView;

    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mInflater = inflater;
        mView = inflateLayout(getLayoutId());
        bindView();
        return mView;
    }


    protected void bindView() {

    }


    protected View inflateLayout(int layoutId) {
        return mInflater.inflate(layoutId, null, false);
    }


    protected abstract int getLayoutId();

    protected <T extends View> T findView(int id) {
        //noinspection unchecked
        return (T) findView(mView, id);
    }

    protected <T extends View> T findView(View container, int id) {
        //noinspection unchecked
        return (T) container.findViewById(id);
    }

    @Override
    public void onClick(View view) {

    }
}
