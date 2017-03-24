package com.kf5.sdk.im.adapter.clickspan;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.kf5.sdk.R;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.Utils;

/**
 * author:chosen
 * date:2017/3/24 15:13
 * email:812219713@qq.com
 */

public class CustomClickSpan extends ClickableSpan {

    private Context mContext;

    private String clickUrl;

    public CustomClickSpan(Context context, String clickUrl) {
        mContext = context;
        this.clickUrl = clickUrl;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        try {
            Intent intent = new Intent();
            Uri uri;
            if (Patterns.WEB_URL.matcher(clickUrl).find()) {
                uri = Uri.parse(CustomTextView.makeWebUrl(clickUrl));
            } else {
                uri = Uri.parse(clickUrl);
            }
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            if (Utils.isIntentAvailable(mContext, intent))
                mContext.startActivity(intent);
            else
                Toast.makeText(mContext, mContext.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
