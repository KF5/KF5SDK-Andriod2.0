package com.kf5.sdk.helpcenter.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.entity.Post;
import com.kf5.sdk.helpcenter.mvp.presenter.HelpCenterDetailPresenter;
import com.kf5.sdk.helpcenter.mvp.usecase.HelpUseCaseManager;
import com.kf5.sdk.helpcenter.mvp.view.IHelpCenterDetailView;
import com.kf5.sdk.system.base.BaseActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.mvp.presenter.PresenterFactory;
import com.kf5.sdk.system.mvp.presenter.PresenterLoader;
import com.kf5.sdk.system.utils.Utils;

import java.util.TimerTask;

public class HelpCenterTypeDetailsActivity extends BaseActivity<HelpCenterDetailPresenter, IHelpCenterDetailView> implements IHelpCenterDetailView, View.OnClickListener {


    private ImageView mBackImg;

    private WebView mWebView;

    private TextView mDetailTitle, mDate;

    public final static String WEB_STYLE = "<style>* {font-size:18px;line-height:30px;} p {color:#6C6C6C;} a {color:#333333;} img {max-width:310px;} " +
            "pre {font-size:9pt;line-height:12pt;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;}</style>";

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mBackImg = (ImageView) findViewById(R.id.kf5_return_img);
        mBackImg.setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.kf5_post_detail_content);
        mDetailTitle = (TextView) findViewById(R.id.kf5_post_detail_title);
        mDate = (TextView) findViewById(R.id.kf5_post_detail_date);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setBuiltInZoomControls(false); //显示放大缩小 controler
        webSettings.setSupportZoom(false); //可以缩放
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.kf5_activity_help_center_detail;
    }

    @Override
    public void onLoadResult(final Post post) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                mDetailTitle.setText(post.getTitle());
                String body = post.getContent();
                if (!body.trim().startsWith("<style>"))
                    body = WEB_STYLE + body;
                body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
                body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
                mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);
                mDate.setText(Utils.getAllTime(post.getCreatedAt()));
            }
        });
    }

    @Override
    public void showError(int resultCode, final String msg) {
        super.showError(resultCode, msg);
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWebView.getClass().getMethod("onPause").invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWebView.getClass().getMethod("onResume").invoke(mWebView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<HelpCenterDetailPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new PresenterFactory<HelpCenterDetailPresenter>() {
            @Override
            public HelpCenterDetailPresenter create() {
                return new HelpCenterDetailPresenter(HelpUseCaseManager.provideHelpCenterDetailCase());
            }
        });
    }

    @Override
    public void onLoadFinished(Loader<HelpCenterDetailPresenter> loader, HelpCenterDetailPresenter data) {
        super.onLoadFinished(loader, data);
        showDialog = true;
        presenter.getPostDetail();
    }

    @Override
    public int getPostId() {
        return getIntent().getIntExtra(Field.ID, 0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.kf5_return_img) {
            finish();
        }
    }

    private class MyWebViewClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, String url) {
            //			view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            //			addJSImageClickListener();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                Intent intent = new Intent();
                Uri uri = Uri.parse(url);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                if (Utils.isIntentAvailable(mActivity, intent))
                    startActivity(intent);
                else
                    showToast(getString(R.string.kf5_no_file_found_hint));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


}
