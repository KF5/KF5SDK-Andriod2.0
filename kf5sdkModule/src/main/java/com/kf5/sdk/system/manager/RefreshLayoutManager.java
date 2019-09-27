package com.kf5.sdk.system.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.List;

/**
 * @author Chosen
 * @create 2019/3/29 17:05
 * @email 812219713@qq.com
 * RefreshLayout管理类
 */
public final class RefreshLayoutManager {

    private static final int EMPTY_IMAGE_ID = R.id.kf5_empty_image;
    private static final int EMPTY_TEXT_ID = R.id.kf5_empty_text;

    private RefreshLayoutManager() {
    }


    public static void configRefreshLayoutWithRefresh(@NonNull Context context, RefreshLayout refreshLayout, boolean refreshAble) {
        if (refreshLayout == null) {
            return;
        }
        if (refreshAble) {
            ClassicsHeader classicsHeader = new ClassicsHeader(context)
                    .setSpinnerStyle(SpinnerStyle.FixedBehind)
                    .setPrimaryColorId(R.color.kf5_title_bar_bg)
                    .setAccentColorId(R.color.kf5_white);
            refreshLayout.setRefreshHeader(classicsHeader);
        } else {
            refreshLayout.setEnableRefresh(false);
        }
    }

    public static void configRefreshLayoutWithRefreshAndLoadMore(@NonNull Context context, RefreshLayout refreshLayout, boolean refreshAble, boolean loadMoreAble) {
        configRefreshLayoutWithRefresh(context, refreshLayout, refreshAble);
        if (refreshLayout == null) {
            return;
        }
        if (loadMoreAble) {
            ClassicsFooter classicsFooter = new ClassicsFooter(context);
            classicsFooter.setBackgroundResource(R.color.kf5_white);
            classicsFooter.setSpinnerStyle(SpinnerStyle.FixedBehind);
            refreshLayout.setRefreshFooter(classicsFooter);
        } else {
            refreshLayout.setEnableLoadMore(false);
        }
    }

    public static void configEmptyLayoutResource(View view, @DrawableRes int emptyImgSrc, @StringRes int emptyTextSrc) {
        ImageView imageView = ((ImageView) bindEmptyView(view, EMPTY_IMAGE_ID));
        if (imageView != null) {
            imageView.setImageResource(emptyImgSrc);
        }
        TextView textView = ((TextView) bindEmptyView(view, EMPTY_TEXT_ID));
        if (textView != null) {
            textView.setText(emptyTextSrc);
        }
    }

    public static void configEmptyLayoutResource(View view, Bitmap imageBitmap, @StringRes int emptyTextSrc) {
        TextView textView = ((TextView) bindEmptyView(view, EMPTY_TEXT_ID));
        if (textView != null) {
            textView.setText(emptyTextSrc);
        }
        ImageView imageView = ((ImageView) bindEmptyView(view, EMPTY_IMAGE_ID));
        if (imageBitmap != null && imageView != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static void configEmptyLayoutResource(View view, Bitmap imageBitmap, String emptyText) {
        TextView textView = ((TextView) bindEmptyView(view, EMPTY_TEXT_ID));
        if (textView != null && !TextUtils.isEmpty(emptyText)) {
            textView.setText(emptyText);
        }
        ImageView imageView = ((ImageView) bindEmptyView(view, EMPTY_IMAGE_ID));
        if (imageView != null && imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static void configEmptyLayoutResource(View view, @DrawableRes int emptyImgSrc, String emptyText) {
        TextView textView = ((TextView) bindEmptyView(view, EMPTY_TEXT_ID));
        if (textView != null && !TextUtils.isEmpty(emptyText)) {
            textView.setText(emptyText);
        }
        ImageView imageView = ((ImageView) bindEmptyView(view, EMPTY_IMAGE_ID));
        if (imageView != null) {
            imageView.setImageResource(emptyImgSrc);
        }
    }


    public static void configEmptyLayoutResource(Activity activity, @DrawableRes int emptyImgSrc, @StringRes int emptyTextSrc) {
        TextView textView = ((TextView) bindEmptyView(activity, EMPTY_TEXT_ID));
        if (textView != null) {
            textView.setText(emptyTextSrc);
        }
        ImageView imageView = ((ImageView) bindEmptyView(activity, EMPTY_IMAGE_ID));
        if (imageView != null) {
            imageView.setImageResource(emptyImgSrc);
        }
    }

    public static void configEmptyLayoutResource(Activity activity, Bitmap imageBitmap, @StringRes int emptyTextSrc) {
        TextView textView = ((TextView) bindEmptyView(activity, EMPTY_TEXT_ID));
        if (textView != null) {
            textView.setText(emptyTextSrc);
        }
        ImageView imageView = ((ImageView) bindEmptyView(activity, EMPTY_IMAGE_ID));
        if (imageView != null && imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static void configEmptyLayoutResource(Activity activity, Bitmap imageBitmap, String emptyText) {
        TextView textView = ((TextView) bindEmptyView(activity, EMPTY_TEXT_ID));
        if (textView != null && !TextUtils.isEmpty(emptyText)) {
            textView.setText(emptyText);
        }
        ImageView imageView = ((ImageView) bindEmptyView(activity, EMPTY_IMAGE_ID));
        if (imageView != null && imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public static void configEmptyLayoutResource(Activity activity, @DrawableRes int emptyImgSrc, String emptyText) {
        TextView textView = ((TextView) bindEmptyView(activity, EMPTY_TEXT_ID));
        if (textView != null && !TextUtils.isEmpty(emptyText)) {
            textView.setText(emptyText);
        }
        ImageView imageView = ((ImageView) bindEmptyView(activity, EMPTY_IMAGE_ID));
        if (imageView != null) {
            imageView.setImageResource(emptyImgSrc);
        }
    }


    public static void setEmptyViewVisibility(List<?> list, @NonNull View emptyView) {
        if (list == null || emptyView == null) {
            return;
        }
        if (list.size() < 1) {
            if (emptyView.getVisibility() != View.VISIBLE) {
                emptyView.setVisibility(View.VISIBLE);
            }
        } else {
            if (emptyView.getVisibility() != View.INVISIBLE) {
                emptyView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 将下拉刷新状态重置为完成状态
     *
     * @param hasMoreData 是否可以继续触发加载更多，如果为{@code true},则正常结束加载更多状态{@link RefreshLayout#finishLoadMore()},
     *                    否则{@link RefreshLayout#finishLoadMoreWithNoMoreData()}.
     */
    public static void finishRefreshAndLoadMore(RefreshLayout refreshLayout, boolean hasMoreData) {
        if (refreshLayout == null) {
            return;
        }
        refreshLayout.finishRefresh();
        if (hasMoreData) {
            refreshLayout.finishLoadMore();
            refreshLayout.setNoMoreData(false);
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }


    private static View bindEmptyView(@NonNull View view, @IdRes int viewID) {
        return view.findViewById(viewID);
    }

    private static View bindEmptyView(@NonNull Activity activity, @IdRes int viewId) {
        return activity.findViewById(viewId);
    }
}
