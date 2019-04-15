package com.kf5.sdk.system.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.kf5.sdk.system.mvp.presenter.Presenter;
import com.kf5.sdk.system.mvp.view.MvpView;

/**
 * author:chosen
 * date:2016/10/13 15:12
 * email:812219713@qq.com
 */

public abstract class BaseMVPActivity<P extends Presenter<V>, V extends MvpView> extends BaseActivity implements MvpView, LoaderManager.LoaderCallbacks<P> {

    public static final int BASE_ACTIVITY_LOADER_ID = 100;

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(BASE_ACTIVITY_LOADER_ID, null, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        presenter = data;
        //noinspection unchecked
        presenter.attachView((V) this);
    }


    @Override
    public void onLoaderReset(Loader<P> loader) {
        presenter = null;
    }

    @Override
    public void showLoading(String msg) {
        showProgressDialog(showDialog, null, null);
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void showError(int resultCode, String msg) {
        showToast(msg);
    }

}
