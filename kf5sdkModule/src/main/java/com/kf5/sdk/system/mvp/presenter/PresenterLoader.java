package com.kf5.sdk.system.mvp.presenter;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * author:chosen
 * date:2016/10/13 15:25
 * email:812219713@qq.com
 */

public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private final PresenterFactory<T> mFactory;

    private T mPresenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.mFactory = factory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mPresenter != null) {
//            deliverResult(mPresenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mPresenter = mFactory.create();
        deliverResult(mPresenter);
    }

    @Override
    protected void onReset() {
        super.onReset();
        mPresenter = null;
    }
}
