package com.kf5.sdk.system.album;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chosen.album.Matisse;
import com.chosen.album.MimeType;
import com.chosen.album.filter.Filter;
import com.chosen.album.internal.entity.CaptureStrategy;
import com.chosen.album.listener.OnCheckedListener;
import com.chosen.album.listener.OnSelectedListener;
import com.kf5.sdk.R;

import java.util.List;
import java.util.Set;

/**
 * @author Chosen
 * @create 2019/1/10 10:12
 * @email 812219713@qq.com
 */
public final class ImageSelectorManager {

    private ImageSelectorManager() {
    }

    public static void toTicketImageGallery(Activity activity, int requestCode) {
        toImageSelector(MimeType.ofImage(), activity, requestCode);
    }

    public static void toIMImageGallery(Activity activity, int requestCode) {
        toImageSelector(MimeType.ofAll(), activity, requestCode);
    }

    private static void toImageSelector(Set<MimeType> mimeTypes, Activity activity, int requestCode) {
        Matisse.from(activity)
                .choose(mimeTypes, false)
                .countable(true)
                .capture(false)//不启用拍照功能，captureStrategy无效
                .captureStrategy(
                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .maxSelectable(1)
                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(
                        activity.getResources().getDimensionPixelSize(R.dimen.kf5_grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
//                                            .imageEngine(new Glide3Engine())  // for glide-V3
                .imageEngine(new Glide4Engine())    // for glide-V4
                .setOnSelectedListener(new OnSelectedListener() {
                    @Override
                    public void onSelected(
                            @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("onSelected", "onSelected: pathList=" + pathList);
                    }
                })
                .originalEnable(true)
                .maxOriginalSize(50)
                .autoHideToolbarOnSingleTap(true)
                .setOnCheckedListener(new OnCheckedListener() {
                    @Override
                    public void onCheck(boolean isChecked) {
                        // DO SOMETHING IMMEDIATELY HERE
                        Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                    }
                })
                .forResult(requestCode);
    }
}
