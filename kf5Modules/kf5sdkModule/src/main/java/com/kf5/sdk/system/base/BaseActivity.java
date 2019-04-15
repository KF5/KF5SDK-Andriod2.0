package com.kf5.sdk.system.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.entity.TitleBarProperty;
import com.kf5.sdk.system.permission.EasyPermissions;
import com.kf5.sdk.system.swipeback.BaseSwipeBackActivity;
import com.kf5.sdk.system.utils.ClickUtils;
import com.kf5.sdk.system.utils.DialogHandler;
import com.kf5.sdk.system.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chosen
 * @create 2019/4/4 16:33
 * @email 812219713@qq.com
 */
public abstract class BaseActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    protected Activity mActivity;

    private DialogHandler mDialogHandler;

    protected boolean showDialog;

    public static final int METHOD_REQUEST_PERMISSION = 0, METHOD_TO_SETTING = 1, METHOD_FINISH_ACTIVITY = 2, METHOD_DO_NOTHING = -1;

    public static final int READ_PHONE_STATE_BY_CHAT_ACTIVITY = 0x10, CAMERA_STATE = 0x11, VOICE_RECORDER = 0x12, WRITE_EXTERNAL_STORAGE = 0x13;

    public final static int CAMERA = 0x01, GALLERY = 0x02;

    private TitleBarProperty titleBarProperty;

    protected abstract int getLayoutID();

    protected abstract TitleBarProperty getTitleBarProperty();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.kf5_activity_anim_in, R.anim.kf5_anim_stay);
        super.onCreate(savedInstanceState);
        mActivity = this;
        titleBarProperty = getTitleBarProperty();
        setContentView(getLayoutID());
        initWidgets();
        setData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!EasyPermissions.hasPermissions(mActivity, permissions)) {
            applyPermissions(requestCode, METHOD_TO_SETTING, permissions);
        } else {
            onActivityResult(requestCode, -1, new Intent());
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.kf5_anim_stay, R.anim.kf5_activity_anim_out);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 显示dialog
     *
     * @param showDialog
     * @param content
     * @param dialogDismissListener
     */
    protected void showProgressDialog(boolean showDialog, String content, DialogHandler.DialogDismissListener dialogDismissListener) {
        if (mDialogHandler == null)
            mDialogHandler = new DialogHandler(mActivity, dialogDismissListener, content, showDialog);
        mDialogHandler.obtainMessage(DialogHandler.SHOW_DIALOG).sendToTarget();
    }

    /**
     * 关闭dialog
     */
    protected void dismissProgressDialog() {
        if (mDialogHandler != null) {
            mDialogHandler.obtainMessage(DialogHandler.DISMISS_DIALOG).sendToTarget();
            mDialogHandler = null;
        }
    }

    protected void showToast(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(mActivity, content);
            }
        });
    }

    private TextView tvTitle;
    protected TextView tvRightView;

    protected void initWidgets() {
        ImageView backImg = ((ImageView) findViewById(R.id.kf5_return_img));
        if (backImg != null) {
            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtils.isInvalidClick(v)) {
                        return;
                    }
                    finish();
                }
            });
        }
        tvRightView = ((TextView) findViewById(R.id.kf5_right_text_view));
        tvTitle = ((TextView) findViewById(R.id.kf5_title));
        if (titleBarProperty != null) {
            if (tvTitle != null && !TextUtils.isEmpty(titleBarProperty.getTitleContent())) {
                tvTitle.setText(titleBarProperty.getTitleContent());
            }
            if (tvRightView != null && titleBarProperty.isRightViewVisible()) {
                if (tvRightView.getVisibility() != View.VISIBLE) {
                    tvRightView.setVisibility(View.VISIBLE);
                }
                if (titleBarProperty.isRightViewClick()) {
                    tvRightView.setOnClickListener(this);
                }
                if (!TextUtils.isEmpty(titleBarProperty.getRightViewContent())) {
                    tvRightView.setText(titleBarProperty.getRightViewContent());
                }
            }
        }
    }

    protected void setTitleContent(String text) {
        if (tvTitle != null && !TextUtils.isEmpty(text)) {
            tvTitle.setText(text);
        }
    }

    protected void setData() {

    }


    /**
     * 申请权限
     *
     * @param requestCode 请求的对应码
     * @param methodCode  执行对应的逻辑索引
     * @param permissions 权限集
     */
    public void applyPermissions(int requestCode, int methodCode, String... permissions) {
        switch (methodCode) {
            case METHOD_REQUEST_PERMISSION:
                List<String> list = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    boolean isAuth = EasyPermissions.hasPermissions(mActivity, permissions[i]);
                    if (!isAuth)
                        list.add(permissions[i]);
                }

                ActivityCompat.requestPermissions(mActivity, list.toArray(new String[list.size()]), requestCode);
                break;
            case METHOD_TO_SETTING:
                applyPermissionsFromSetting(requestCode);
                break;
            case METHOD_FINISH_ACTIVITY:
                finish();
                break;
        }
    }

    private void applyPermissionsFromSetting(final int requestCode) {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
            @SuppressLint("StringFormatMatches") AlertDialog dialog = new AlertDialog.Builder(mActivity)
                    .setMessage(getString(R.string.kf5_get_auth_hint, applicationName))
                    .setPositiveButton(getString(R.string.kf5_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, requestCode);
                        }
                    }).setNegativeButton(getString(R.string.kf5_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (requestCode == READ_PHONE_STATE_BY_CHAT_ACTIVITY) {
                                finish();
                            }
                        }
                    }).create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 权限检查
     *
     * @param permissions
     * @return
     */
    public boolean hasPermission(String... permissions) {
        return EasyPermissions.hasPermissions(mActivity, permissions);
    }
}
