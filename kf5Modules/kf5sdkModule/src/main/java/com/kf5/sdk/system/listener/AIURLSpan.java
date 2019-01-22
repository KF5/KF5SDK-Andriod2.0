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
import com.kf5.sdk.helpcenter.ui.HelpCenterTypeDetailsActivity;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;

/**
 * author:chosen
 * date:2016/10/20 17:21
 * email:812219713@qq.com
 */

public class AIURLSpan extends ClickableSpan {

    private String url;

    private String type;

    private String clickText;

    private Context context;

    public AIURLSpan(String id, String type, String clickText, Context context) {
        this.url = id;
        this.context = context;
        this.type = type;
        this.clickText = clickText;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//        super.updateDrawState(ds);
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        try {
            if (TextUtils.equals(Field.GET_AGENT, url)) {
                if (context instanceof BaseChatActivity) {
                    BaseChatActivity chatActivity = (BaseChatActivity) context;
                    chatActivity.aiToGetAgents();
                }
                //超链接
            } else if (Patterns.WEB_URL.matcher(url).matches()) {
                Intent intent = new Intent();
                Uri uri = Uri.parse(CustomTextView.makeWebUrl(url));
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                if (Utils.isIntentAvailable(context, intent))
                    context.startActivity(intent);
                else
                    Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
                //电话
            } else if (Patterns.EMAIL_ADDRESS.matcher(url).matches()) {
                new DialogBox(context).setMessage(context.getString(R.string.kf5_send_email_hint))
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
            } else {
                if (TextUtils.equals(Field.CHAT_QUESTION, type)) {
                    if (context instanceof BaseChatActivity) {
                        BaseChatActivity chatActivity = (BaseChatActivity) context;
                        chatActivity.onSendAITextMessage(clickText, Integer.parseInt(url));
                    }
                } else if (TextUtils.equals(Field.CHAT_DOCUMENT, type)) {
                    Intent intent = new Intent(context, HelpCenterTypeDetailsActivity.class);
                    intent.putExtra(Field.ID, Integer.parseInt(url));
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}