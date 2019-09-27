package com.kf5.sdk.system.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.widget.Toast;

import com.chosen.imageviewer.manager.ImagePreviewManager;
import com.kf5.sdk.R;
import com.kf5.sdk.helpcenter.ui.HelpCenterTypeDetailsActivity;
import com.kf5.sdk.im.ui.BaseChatActivity;
import com.kf5.sdk.system.album.ImageSelectorManager;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.ui.ImageActivity;
import com.kf5.sdk.system.widget.ActionSheetDialog;
import com.kf5.sdk.system.widget.DialogBox;

import java.util.ArrayList;

/**
 * author:chosen
 * date:2018/1/29 16:20
 * email:812219713@qq.com
 */

public final class IntentUtils {

    public static void phoneCall(final Context context, final String url) {

        try {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .setTitle(String.format(context.getString(R.string.kf5_make_call_title_hint), url))
                    .addSheetItem(context.getString(R.string.kf5_call), ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, url.startsWith("tel:") ? Uri.parse(url) : Uri.parse("tel:" + url));
                                    if (Utils.isIntentAvailable(context, intent))
                                        context.startActivity(intent);
                                    else
                                        Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addSheetItem(context.getString(R.string.kf5_copy_number), ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Utils.copyText(url, context);
                                    ToastUtil.showToast(context, context.getString(R.string.kf5_copied));
                                }
                            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void emailAddress(final Context context, final String url) {
        try {
            new ActionSheetDialog(context)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .setTitle(String.format(context.getString(R.string.kf5_send_email_title_hint), url))
                    .addSheetItem(context.getString(R.string.kf5_send_email), ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setData(url.startsWith("mailto:") ? Uri.parse(url) : Uri.parse("mailto:" + url));
                                    if (Utils.isIntentAvailable(context, intent))
                                        context.startActivity(intent);
                                    else
                                        Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
                                }
                            })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            Uri uri = Uri.parse(url);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            if (Utils.isIntentAvailable(context, intent))
                context.startActivity(intent);
            else
                Toast.makeText(context, context.getString(R.string.kf5_no_file_found_hint), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void browserImage(Context context, String url) {
        try {
//            Intent intent = new Intent(context, ImageActivity.class);
//            ArrayList<String> arrayList = new ArrayList<>();
//            arrayList.add(url);
//            intent.putExtra(Field.EXTRA_IMAGE_INDEX, 1);
//            intent.putStringArrayListExtra(Field.EXTRA_IMAGE_URLS, arrayList);
//            context.startActivity(intent);
            ImagePreviewManager.startPreviewImage(context, FilePath.IMAGES_PATH, true, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewHelpCenterDetail(Context context, String url) {
        try {
            Intent intent = new Intent(context, HelpCenterTypeDetailsActivity.class);
            intent.putExtra(Field.ID, Integer.parseInt(url));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendQuestionContent(Context context, String showContent, String url, boolean isCategory) {
        try {
            if (context instanceof BaseChatActivity) {
                BaseChatActivity chatActivity = (BaseChatActivity) context;
                chatActivity.onSendAITextMessage(showContent, Integer.parseInt(url), isCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAgent(Context context) {
        try {
            if (context instanceof BaseChatActivity) {
                BaseChatActivity chatActivity = (BaseChatActivity) context;
                chatActivity.aiToGetAgents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendCustomMessageIntent(Context mContext, String clickUrl) {
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
