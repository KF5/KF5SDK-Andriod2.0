package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kf5.sdk.R;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.utils.MessageUtils;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.LogUtil;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.ToastUtil;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.DialogBox;
import com.kf5.sdk.system.widget.PopList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chosen
 * @create 2019/1/8 12:17
 * @email 812219713@qq.com
 */
class TextHolder extends BaseArrowHolder {

    enum TextMessageType {
        TEXT, FILE, AI_MESSAGE, CUSTOM
    }

    private TextView tvText;
    private TextMessageType textMessageType;
    @Nullable
    private BaseSendHolder sendHolder;

    TextHolder(MessageAdapter messageAdapter, View convertView) {
        super(messageAdapter, convertView);
        tvText = convertView.findViewById(R.id.kf5_message_item_with_text);
    }

    void initData(int position, boolean isReceive, TextMessageType textMessageType) {
        super.initData(position, isReceive);
        this.textMessageType = textMessageType;
        if (!isReceive) {
            sendHolder = new BaseSendHolder(messageAdapter, convertView);
        }
    }

    @Override
    protected void setUpUI() {
        super.setUpUI();
        MessageType messageType;
        if (textMessageType == TextMessageType.FILE) {
            messageType = MessageType.FILE;
        } else {
            messageType = MessageType.TEXT;
        }
        if (sendHolder != null) {
            sendHolder.setUpSendMessageUI(message, messageType, position);
        }
        buildContent();
    }

    private void buildContent() {
        switch (textMessageType) {
            case FILE:
                tvText.setText(Html.fromHtml("<a href=\"\">" + message.getUpload().getName() + "</a>"));
                tvText.setOnClickListener(new ClickListener(context, message));
                tvText.setOnLongClickListener(new PopList<>(tvText, new ItemLongClickCallback()));
                break;
            case CUSTOM:
            case AI_MESSAGE:
            case TEXT:
            default:
                CustomTextView.applyRichText(tvText, textMessageType == TextMessageType.AI_MESSAGE ? MessageUtils.decodeAIMessage(message.getMessage()) :
                        textMessageType == TextMessageType.CUSTOM ? MessageUtils.makeCustomMessageContent(context, message.getMessage()) : message.getMessage(), null);
                tvText.setOnLongClickListener(new PopList<>(tvText, new ItemLongClickCallback()));
                break;
        }
    }


    private class ClickListener implements View.OnClickListener {

        private IMMessage message;
        private Context context;

        public ClickListener(Context context, IMMessage message) {
            this.context = context;
            this.message = message;
        }

        @Override
        public void onClick(View v) {
            switch (textMessageType) {
                case FILE:
                    clickFile();
                    break;
            }
        }

        private void clickFile() {
            new DialogBox(context).setMessage(context.getString(R.string.kf5_open_file_hint))
                    .setLeftButton(context.getString(R.string.kf5_cancel), null)
                    .setRightButton(context.getString(R.string.kf5_open), new DialogBox.onClickListener() {

                        @Override
                        public void onClick(DialogBox dialog) {
                            dialog.dismiss();
                            Upload upload = message.getUpload();
                            if (upload == null) {
                                return;
                            }
                            String url = upload.getUrl();
                            if (!TextUtils.isEmpty(url)) {
                                File file = new File(FilePath.FILE + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                                if (file.exists()) {
                                    //直接打开
                                    Uri uri = Uri.parse(file.getAbsolutePath());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                                    if (Utils.isIntentAvailable(context, intent)) {
                                        context.startActivity(intent);
                                    } else {
                                        ToastUtil.showToast(context, context.getString(R.string.kf5_no_file_found_hint));
                                    }
                                } else {
                                    ToastUtil.showToast(context, context.getString(R.string.kf5_download_file));
                                }
                            }
                        }
                    }).show();
        }
    }

    private class ItemLongCollection extends PopList.PopListItem {
        ItemLongCollection(String content) {
            super(content);
        }
    }

    private class ItemLongClickCallback implements PopList.OnClickCallback<ItemLongCollection> {

        @Override
        public void onItemClick(ItemLongCollection data) {
            String clickData = data.getContent();
            switch (textMessageType) {
                case FILE:
                    if (TextUtils.equals(context.getString(R.string.kf5_download), clickData)) {
                        fileLongClick();
                    }
                    break;
                case TEXT:
                case AI_MESSAGE:
                case CUSTOM:
                    if (TextUtils.equals(context.getString(R.string.kf5_copy), clickData)) {
                        textCopy();
                    }
                    break;
            }
        }

        @Override
        public List<ItemLongCollection> getListData() {
            List<ItemLongCollection> list = new ArrayList<>();
            LogUtil.printf("文字消息类型" + textMessageType);
            switch (textMessageType) {
                case FILE:
                    list.add(new ItemLongCollection(context.getString(R.string.kf5_download)));
                    break;
                case TEXT:
                case AI_MESSAGE:
                case CUSTOM:
                    list.add(new ItemLongCollection(context.getString(R.string.kf5_copy)));
                    break;
            }
            if (!list.isEmpty() && tvText.getMovementMethod() != null && tvText.getMovementMethod() instanceof CustomTextView.RichTextMovementMethod) {
                ((CustomTextView.RichTextMovementMethod) tvText.getMovementMethod()).removeSpan((SpannableString.valueOf(tvText.getText())));
            }
            return list;
        }

        private void fileLongClick() {
            final Upload upload = message.getUpload();
            if (upload != null) {
                final String url = upload.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    final File file = new File(FilePath.FILE + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                    if (file.exists()) {
                        Toast.makeText(context, context.getString(R.string.kf5_file_downloaded), Toast.LENGTH_SHORT).show();
                    } else {
                        new DialogBox(context).setMessage(context.getString(R.string.kf5_download_file_hint))
                                .setLeftButton(context.getString(R.string.kf5_cancel), null)
                                .setRightButton(context.getString(R.string.kf5_download), new DialogBox.onClickListener() {

                                    @Override
                                    public void onClick(DialogBox dialog) {
                                        dialog.dismiss();
                                        Toast.makeText(context, context.getString(R.string.kf5_start_to_download), Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                }
            }
        }

        private void textCopy() {
            Utils.copyText(tvText.getText().toString(), context);
            ToastUtil.showToast(context, context.getString(R.string.kf5_copied));
        }
    }
}
