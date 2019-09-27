package com.chosen.imageviewer.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chosen.imageviewer.ImagePreview;
import com.chosen.imageviewer.bean.ImageInfo;
import com.chosen.imageviewer.glide.FileTarget;
import com.chosen.imageviewer.glide.ImageLoader;
import com.chosen.imageviewer.tool.common.NetworkUtil;
import com.chosen.imageviewer.tool.image.ImageUtil;
import com.chosen.imageviewer.tool.ui.MyToast;
import com.chosen.imageviewer.tool.ui.PhoneUtil;
import com.chosen.imageviewer.view.photoview.PhotoView;
import com.chosen.imageviewer.view.scaleview.FingerDragHelper;
import com.chosen.imageviewer.view.scaleview.ImageSource;
import com.chosen.imageviewer.view.scaleview.SubsamplingScaleImageViewDragClose;
import com.kf5.sdk.R;

import java.io.File;
import java.util.List;

/**
 * @author Chosen
 * @create 2018/12/17 11:51
 * @email 812219713@qq.com
 */
public class ImagePreviewAdapter extends PagerAdapter {

    private static final String TAG = "ImagePreview";
    private Activity activity;
    private List<ImageInfo> imageInfoList;
    private SparseArray<SubsamplingScaleImageViewDragClose> imageHashMap = new SparseArray<>();
    private SparseArray<PhotoView> imageGifHashMap = new SparseArray<>();
    private String finalLoadUrl = "";//最终加载的图片url
    private int phoneHeight = 0;

    public ImagePreviewAdapter(Activity activity, @NonNull List<ImageInfo> imageInfo) {
        this.imageInfoList = imageInfo;
        this.activity = activity;
        WindowManager windowManager = ((WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.phoneHeight = metrics.heightPixels;
    }

    public void closePage() {
        try {
            if (imageHashMap != null && imageHashMap.size() > 0) {
                for (int i = 0; i < imageHashMap.size(); i++) {
                    SubsamplingScaleImageViewDragClose entry = imageHashMap.get(i);
                    if (entry != null) {
                        entry.destroyDrawingCache();
                        entry.recycle();
                    }
                }
                imageHashMap.clear();
                imageHashMap = null;
            }

            if (imageGifHashMap != null && imageGifHashMap.size() > 0) {
                for (int i = 0; i < imageGifHashMap.size(); i++) {
                    PhotoView entry = imageGifHashMap.get(i);
                    entry.destroyDrawingCache();
                    entry.setImageBitmap(null);
                }
                imageGifHashMap.clear();
                imageGifHashMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ImageLoader.clearMemory(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return imageInfoList.size();
    }


    /**
     * 加载原图
     */
    public void loadOrigin(int position) {

        if (imageHashMap == null || imageGifHashMap == null) {
            notifyDataSetChanged();
            return;
        }
        ImageInfo info = imageInfoList.get(position);
        String originalUrl = info.getOriginUrl();
        if (imageHashMap.get(position) != null && imageGifHashMap.get(position) != null) {
            final SubsamplingScaleImageViewDragClose imageView = imageHashMap.get(position);
            final PhotoView imageGif = imageGifHashMap.get(position);

            File cacheFile = ImageLoader.getGlideCacheFile(activity, originalUrl);
            if (cacheFile != null && cacheFile.exists()) {
                boolean isCacheIsGif = ImageUtil.isGifImageWithMime(cacheFile.getAbsolutePath());
                if (isCacheIsGif) {
                    imageGif.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);

                    Glide.with(activity)
                            .asGif()
                            .load(cacheFile)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .error(ImagePreview.getInstance().getErrorPlaceHolder()))
                            .into(imageGif);
                } else {
                    imageGif.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    String thumbnailUrl = info.getThumbnailUrl();
                    File smallCacheFile = ImageLoader.getGlideCacheFile(activity, thumbnailUrl);

                    ImageSource small = null;
                    if (smallCacheFile != null && smallCacheFile.exists()) {
                        String smallImagePath = smallCacheFile.getAbsolutePath();
                        small = ImageSource.bitmap(
                                ImageUtil.getImageBitmap(smallImagePath, ImageUtil.getBitmapDegree(smallImagePath)));
                        int widSmall = ImageUtil.getWidthHeight(smallImagePath)[0];
                        int heiSmall = ImageUtil.getWidthHeight(smallImagePath)[1];
                        small.dimensions(widSmall, heiSmall);
                    }

                    String imagePath = cacheFile.getAbsolutePath();
                    ImageSource origin = ImageSource.uri(imagePath);
                    int widOrigin = ImageUtil.getWidthHeight(imagePath)[0];
                    int heiOrigin = ImageUtil.getWidthHeight(imagePath)[1];
                    origin.dimensions(widOrigin, heiOrigin);

                    setImageSpec(imagePath, imageView);

                    imageView.setOrientation(SubsamplingScaleImageViewDragClose.ORIENTATION_USE_EXIF);
                    imageView.setImage(origin.tilingDisabled(), small != null ? small.tilingDisabled() : null);
                }
            } else {
                notifyDataSetChanged();
            }
        } else {
            notifyDataSetChanged();
        }
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        if (activity == null) {
            return container;
        }
        View convertView = View.inflate(activity, R.layout.kf5_imageviewer_item_photoview, null);
        final ProgressBar progressBar = convertView.findViewById(R.id.progress_view);
        final FingerDragHelper fingerDragHelper = convertView.findViewById(R.id.fingerDragHelper);
        final SubsamplingScaleImageViewDragClose imageView = convertView.findViewById(R.id.photo_view);
        final PhotoView imageGif = convertView.findViewById(R.id.gif_view);

        final ImageInfo info = this.imageInfoList.get(position);
        final String originPathUrl = info.getOriginUrl();
        final String thumbPathUrl = info.getThumbnailUrl();

        imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_CENTER_INSIDE);
        imageView.setDoubleTapZoomStyle(SubsamplingScaleImageViewDragClose.ZOOM_FOCUS_CENTER);
        imageView.setDoubleTapZoomDuration(ImagePreview.getInstance().getZoomTransitionDuration());
        imageView.setMinScale(ImagePreview.getInstance().getMinScale());
        imageView.setMaxScale(ImagePreview.getInstance().getMaxScale());
        imageView.setDoubleTapZoomScale(ImagePreview.getInstance().getMediumScale());

        imageGif.setZoomTransitionDuration(ImagePreview.getInstance().getZoomTransitionDuration());
        imageGif.setMinimumScale(ImagePreview.getInstance().getMinScale());
        imageGif.setMaximumScale(ImagePreview.getInstance().getMaxScale());
        imageGif.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImagePreview.getInstance().isEnableClickClose()) {
                    activity.finish();
                }
                if (ImagePreview.getInstance().getBigImageClickListener() != null) {
                    ImagePreview.getInstance().getBigImageClickListener().onClick(v, position);
                }
            }
        });
        imageGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImagePreview.getInstance().isEnableClickClose()) {
                    activity.finish();
                }
                if (ImagePreview.getInstance().getBigImageClickListener() != null) {
                    ImagePreview.getInstance().getBigImageClickListener().onClick(v, position);
                }
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ImagePreview.getInstance().getBigImageLongClickListener() != null) {
                    return ImagePreview.getInstance().getBigImageLongClickListener().onLongClick(v, position);
                }
                return false;
            }
        });
        imageGif.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ImagePreview.getInstance().getBigImageLongClickListener() != null) {
                    return ImagePreview.getInstance().getBigImageLongClickListener().onLongClick(v, position);
                }
                return false;
            }
        });

        if (ImagePreview.getInstance().isEnableDragClose()) {
            fingerDragHelper.setOnAlphaChangedListener(new FingerDragHelper.onAlphaChangedListener() {
                @Override
                public void onTranslationYChanged(MotionEvent event, float translationY) {
                    float yAbs = Math.abs(translationY);
                    float percent = yAbs / PhoneUtil.getPhoneHei(activity.getApplicationContext());
                    float number = 1.0F - percent;

                    if (activity instanceof ImagePreviewActivity) {
                        ((ImagePreviewActivity) activity).setAlpha(number);
                    }

                    if (imageGif.getVisibility() == View.VISIBLE) {
                        imageGif.setScaleY(number);
                        imageGif.setScaleX(number);
                    }
                    if (imageView.getVisibility() == View.VISIBLE) {
                        imageView.setScaleY(number);
                        imageView.setScaleX(number);
                    }
                }
            });
        }

        imageGifHashMap.remove(position);
        imageGifHashMap.put(position, imageGif);

        imageHashMap.remove(position);
        imageHashMap.put(position, imageView);

        ImagePreview.LoadStrategy loadStrategy = ImagePreview.getInstance().getLoadStrategy();
        // 根据当前加载策略判断，需要加载的url是哪一个
        if (loadStrategy == ImagePreview.LoadStrategy.Default) {
            finalLoadUrl = thumbPathUrl;
        } else if (loadStrategy == ImagePreview.LoadStrategy.AlwaysOrigin) {
            finalLoadUrl = originPathUrl;
        } else if (loadStrategy == ImagePreview.LoadStrategy.AlwaysThumb) {
            finalLoadUrl = thumbPathUrl;
        } else if (loadStrategy == ImagePreview.LoadStrategy.NetworkAuto) {
            if (NetworkUtil.isWiFi(activity)) {
                finalLoadUrl = originPathUrl;
            } else {
                finalLoadUrl = thumbPathUrl;
            }
        }
        finalLoadUrl = finalLoadUrl.trim();
        final String url = finalLoadUrl;

        // 显示加载圈圈
        progressBar.setVisibility(View.VISIBLE);

        // 判断原图缓存是否存在，存在的话，直接显示原图缓存，优先保证清晰。
        File cacheFile = ImageLoader.getGlideCacheFile(activity, originPathUrl);
        if (cacheFile != null && cacheFile.exists()) {
            boolean isCacheIsGif = ImageUtil.isGifImageWithMime(cacheFile.getAbsolutePath());
            if (isCacheIsGif) {
                String imagePath = cacheFile.getAbsolutePath();
                loadGifImageSpec(imagePath, imageView, imageGif, progressBar);
            } else {
                String imagePath = cacheFile.getAbsolutePath();
                loadImageSpec(imagePath, imageView, imageGif, progressBar);
            }
        } else {
            Glide.with(activity).downloadOnly().load(url).addListener(new RequestListener<File>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target,
                                            boolean isFirstResource) {

                    Glide.with(activity).downloadOnly().load(url).addListener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target,
                                                    boolean isFirstResource) {

                            Glide.with(activity).downloadOnly().load(url).addListener(new RequestListener<File>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                            Target<File> target, boolean isFirstResource) {
                                    loadFailed(imageView, imageGif, progressBar, e);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target,
                                                               DataSource dataSource, boolean isFirstResource) {
                                    loadSuccess(resource, imageView, imageGif, progressBar);
                                    return true;
                                }
                            }).into(new FileTarget() {
                                @Override
                                public void onLoadStarted(@Nullable Drawable placeholder) {
                                    super.onLoadStarted(placeholder);
                                }
                            });
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            loadSuccess(resource, imageView, imageGif, progressBar);
                            return true;
                        }
                    }).into(new FileTarget() {
                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                        }
                    });
                    return true;
                }

                @Override
                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    loadSuccess(resource, imageView, imageGif, progressBar);
                    return true;
                }
            }).into(new FileTarget() {
                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                }
            });
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView((View) object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ImageLoader.clearMemory(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (imageHashMap != null && imageHashMap.get(position) != null) {
                imageHashMap.get(position).destroyDrawingCache();
                imageHashMap.get(position).recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (imageGifHashMap != null && imageGifHashMap.get(position) != null) {
                imageGifHashMap.get(position).destroyDrawingCache();
                imageGifHashMap.get(position).setImageBitmap(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, final Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private void loadFailed(SubsamplingScaleImageViewDragClose imageView, ImageView imageGif, ProgressBar progressBar,
                            GlideException e) {
        progressBar.setVisibility(View.GONE);
        imageGif.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);

        imageView.setZoomEnabled(false);
        imageView.setImage(ImageSource.resource(ImagePreview.getInstance().getErrorPlaceHolder()).tilingDisabled());

        String errorMsg = activity.getResources().getString(R.string.kf5_imageviewer_failed_to_load_img);
        if (e != null) {
            errorMsg = errorMsg.concat(":\n").concat(e.getMessage());
        }
        if (errorMsg.length() > 200) {
            errorMsg = errorMsg.substring(0, 199);
        }
        MyToast.getInstance()._short(activity.getApplicationContext(), errorMsg);
    }

    private void loadSuccess(File resource, SubsamplingScaleImageViewDragClose imageView, ImageView imageGif,
                             ProgressBar progressBar) {
        String imagePath = resource.getAbsolutePath();
        boolean isCacheIsGif = ImageUtil.isGifImageWithMime(imagePath);
        if (isCacheIsGif) {
            loadGifImageSpec(imagePath, imageView, imageGif, progressBar);
        } else {
            loadImageSpec(imagePath, imageView, imageGif, progressBar);
        }
    }

    private void setImageSpec(final String imagePath, final SubsamplingScaleImageViewDragClose imageView) {
        boolean isLongImage = ImageUtil.isLongImage(activity, imagePath);
        if (isLongImage) {
            imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
            imageView.setMinScale(ImageUtil.getLongImageMinScale(activity, imagePath));
            imageView.setMaxScale(ImageUtil.getLongImageMaxScale(activity, imagePath));
            imageView.setDoubleTapZoomScale(ImageUtil.getLongImageMaxScale(activity, imagePath));
        } else {
            boolean isWideImage = ImageUtil.isWideImage(activity, imagePath);
            boolean isSmallImage = ImageUtil.isSmallImage(activity, imagePath);
            if (isWideImage) {
                imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_CENTER_INSIDE);
                imageView.setMinScale(ImagePreview.getInstance().getMinScale());
                imageView.setMaxScale(ImagePreview.getInstance().getMaxScale());
                imageView.setDoubleTapZoomScale(ImageUtil.getWideImageDoubleScale(activity, imagePath));
            } else if (isSmallImage) {
                imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_CUSTOM);
                imageView.setMinScale(ImageUtil.getSmallImageMinScale(activity, imagePath));
                imageView.setMaxScale(ImageUtil.getSmallImageMaxScale(activity, imagePath));
                imageView.setDoubleTapZoomScale(ImageUtil.getSmallImageMaxScale(activity, imagePath));
            } else {
                imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_CENTER_INSIDE);
                imageView.setMinScale(ImagePreview.getInstance().getMinScale());
                imageView.setMaxScale(ImagePreview.getInstance().getMaxScale());
                imageView.setDoubleTapZoomScale(ImagePreview.getInstance().getMediumScale());
            }
        }
    }

    private void loadImageSpec(final String imagePath, final SubsamplingScaleImageViewDragClose imageView,
                               final ImageView imageGif, final ProgressBar progressBar) {

        imageGif.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);

        setImageSpec(imagePath, imageView);

        imageView.setOrientation(SubsamplingScaleImageViewDragClose.ORIENTATION_USE_EXIF);
        imageView.setImage(ImageSource.uri(Uri.fromFile(new File(imagePath))).tilingDisabled());

        imageView.setOnImageEventListener(new SubsamplingScaleImageViewDragClose.OnImageEventListener() {
            @Override
            public void onReady() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onImageLoaded() {

            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {

            }

            @Override
            public void onTileLoadError(Exception e) {

            }

            @Override
            public void onPreviewReleased() {

            }
        });
    }

    private void loadGifImageSpec(final String imagePath, final SubsamplingScaleImageViewDragClose imageView,
                                  final ImageView imageGif, final ProgressBar progressBar) {

        imageGif.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);

        Glide.with(activity)
                .asGif()
                .load(imagePath)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(ImagePreview.getInstance().getErrorPlaceHolder()))
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target,
                                                boolean isFirstResource) {
                        imageGif.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImage(ImageSource.resource(ImagePreview.getInstance().getErrorPlaceHolder()).tilingDisabled());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageGif);
    }
}
