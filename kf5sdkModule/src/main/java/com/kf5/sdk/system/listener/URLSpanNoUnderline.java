package com.kf5.sdk.system.listener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.kf5.sdk.R;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.ui.ImageActivity;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;

import java.util.ArrayList;

/**
 * author:chosen
 * date:2016/10/20 17:18
 * email:812219713@qq.com
 */

public class URLSpanNoUnderline extends ClickableSpan {

    private Context context;

    private String url;

    private String clickText;

    private static final String PICTURE = "[图片]";

    private static final String TEL = "tel:";

    private static final String MAIL_TO = "mailto:";

    public URLSpanNoUnderline(Context context, String url, String clickText) {
        this.context = context;
        this.url = url;
        this.clickText = clickText;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        //			super.onClick(widget);
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            //拨打电话
            if (url.startsWith(TEL)) {
                new DialogBox(context).setMessage(context.getString(R.string.kf5_make_call_hint))
                        .setLeftButton(context.getString(R.string.kf5_cancel), null)
                        .setRightButton(context.getString(R.string.kf5_confirm), new DialogBox.onClickListener() {

                            @Override
                            public void onClick(DialogBox dialog) {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                                if (Utils.isIntentAvailable(context, intent))
                                    context.startActivity(intent);
                                else
                                    Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
                            }
                        }).show();

                //发送邮件
            } else if (url.startsWith(MAIL_TO)) {

                new DialogBox(context)
                        .setMessage(context.getString(R.string.kf5_send_email_hint))
                        .setLeftButton(context.getString(R.string.kf5_cancel), null)
                        .setRightButton(context.getString(R.string.kf5_confirm), new DialogBox.onClickListener() {

                            @Override
                            public void onClick(DialogBox dialog) {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setData(Uri.parse(url));
                                intent.setType("plain/text");
                                if (Utils.isIntentAvailable(context, intent))
                                    context.startActivity(intent);
                                else
                                    Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();

                            }
                        }).show();
                //网址
            } else if (TextUtils.equals(PICTURE, clickText)) {
                ArrayList<String> list = new ArrayList<>();
                list.add(url);
                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra(Field.EXTRA_IMAGE_INDEX, 0);
                intent.putStringArrayListExtra(Field.EXTRA_IMAGE_URLS, list);
                context.startActivity(intent);
            } else {
                if (Patterns.WEB_URL.matcher(url).matches()) {
                    url = CustomTextView.makeWebUrl(url);
                }
                Intent intent = new Intent();
                Uri uri = Uri.parse(url);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                if (Utils.isIntentAvailable(context, intent))
                    context.startActivity(intent);
                else
                    Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

