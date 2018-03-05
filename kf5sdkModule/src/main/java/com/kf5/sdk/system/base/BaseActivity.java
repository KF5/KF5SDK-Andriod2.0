package com.kf5.sdk.system.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;

import com.kf5.sdk.R;
import com.kf5.sdk.system.image.ImageSelectorActivity;
import com.kf5.sdk.system.mvp.presenter.Presenter;
import com.kf5.sdk.system.mvp.view.MvpView;
import com.kf5.sdk.system.permission.EasyPermissions;
import com.kf5.sdk.system.swipeback.BaseSwipeBackActivity;
import com.kf5.sdk.system.utils.DialogHandler;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.FileProviderUtil;
import com.kf5.sdk.system.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * author:chosen
 * date:2016/10/13 15:12
 * email:812219713@qq.com
 */

public abstract class BaseActivity<P extends Presenter<V>, V extends MvpView> extends BaseSwipeBackActivity implements MvpView, LoaderManager.LoaderCallbacks<P> {

    public static final int BASE_ACTIVITY_LOADER_ID = 100;

    protected P presenter;

    protected Activity mActivity;

    private DialogHandler mDialogHandler;

    protected boolean showDialog;

    public static final int METHOD_REQUEST_PERMISSION = 0, METHOD_TO_SETTING = 1, METHOD_FINISH_ACTIVITY = 2, METHOD_DO_NOTHING = -1;

    public static final int READ_PHONE_STATE_BY_CHAT_ACTIVITY = 0x10, CAMERA_STATE = 0x11, VOICE_RECORDER = 0x12, WRITE_EXTERNAL_STORAGE = 0x13;

    public final static int CAMERA = 0x01, GALLERY = 0x02;

    public File picFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.kf5_activity_anim_in, R.anim.kf5_anim_stay);
        super.onCreate(savedInstanceState);
        mActivity = this;
        getSupportLoaderManager().initLoader(BASE_ACTIVITY_LOADER_ID, null, this);
        setContentView(getLayoutID());
        initWidgets();
        setData();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.kf5_anim_stay, R.anim.kf5_activity_anim_out);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK && requestCode == CAMERA && picFile != null)
            picFile.delete();
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

    protected void initWidgets() {

    }

    protected void setData() {

    }

    protected abstract int getLayoutID();

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

    /**
     * 相机
     */
    public void takePictureFromCamera() {
        try {
            picFile = new File(FilePath.IMAGES_PATH + UUID.randomUUID() + ".jpg");
            if (!picFile.exists())
                //noinspection ResultOfMethodCallIgnored
                picFile.getParentFile().mkdirs();
            //noinspection ResultOfMethodCallIgnored
            picFile.createNewFile();
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            //兼容7.0拍照权限
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProviderUtil.getUriFromFile(this, picFile));
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 相册
     */
    public void getPictureFromGallery(int count) {
        Intent intent = new Intent(mActivity, ImageSelectorActivity.class);
        intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, count);
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, GALLERY);
    }


}
