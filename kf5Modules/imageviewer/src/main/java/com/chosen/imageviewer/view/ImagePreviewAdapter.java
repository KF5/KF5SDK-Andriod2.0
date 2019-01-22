package com.chosen.imageviewer.view;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
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
import com.chosen.imageviewer.R;
import com.chosen.imageviewer.bean.ImageInfo;
import com.chosen.imageviewer.glide.FileTarget;
import com.chosen.imageviewer.glide.ImageLoader;
import com.chosen.imageviewer.tool.common.NetworkUtil;
import com.chosen.imageviewer.tool.common.Print;
import com.chosen.imageviewer.tool.image.ImageUtil;
import com.chosen.imageviewer.tool.ui.MyToast;
import com.chosen.imageviewer.view.scaleview.FingerDragHelper;
import com.chosen.imageviewer.view.scaleview.ImageSource;
import com.chosen.imageviewer.view.scaleview.SubsamplingScaleImageViewDragClose;
import com.chosen.imageviewer.view.scaleview.SubsamplingScaleImageView;
import com.chosen.imageviewer.view.photoview.PhotoView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chosen
 * @create 2018/12/17 11:51
 * @email 812219713@qq.com
 */
public class ImagePreviewAdapter extends PagerAdapter {

    private static final String TAG = "ImagePreview";
    private Activity activity;
    private List<ImageInfo> imageInfo;
    private HashMap<String, SubsamplingScaleImageViewDragClose> imageHashMap = new HashMap<>();
    private HashMap<String, PhotoView> imageGifHashMap = new HashMap<>();
    private String finalLoadUrl = "";//最终加载的图片url
    private int phoneHeight = 0;

    public ImagePreviewAdapter(Activity activity, @NonNull List<ImageInfo> imageInfo) {
        this.imageInfo = imageInfo;
        this.activity = activity;
        WindowManager windowManager = ((WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.phoneHeight = metrics.heightPixels;
    }

    public void closePage() {
        try {
            if (imageHashMap != null && imageHashMap.size() > 0) {
                for (Map.Entry<String, SubsamplingScaleImageViewDragClose> entry : imageHashMap.entrySet()) {
                    if (entry != null && entry.getValue() != null) {
                        ((SubsamplingScaleImageViewDragClose) entry.getValue()).destroyDrawingCache();
                        ((SubsamplingScaleImageViewDragClose) entry.getValue()).recycle();
                    }
                }
                imageHashMap.clear();
                imageHashMap = null;
            }

            if (imageGifHashMap != null && imageGifHashMap.size() > 0) {
                for (Map.Entry<String, PhotoView> entry : imageGifHashMap.entrySet()) {
                    if (entry != null && entry.getValue() != null) {
                        entry.getValue().destroyDrawingCache();
                        entry.getValue().setImageBitmap(null);
                    }
                }
                imageGifHashMap.clear();
                imageGifHashMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return imageInfo.size();
    }


    /**
     * 加载原图
     *
     * @param imageInfo
     */
    public void loadOrigin(ImageInfo imageInfo) {
        String originalUrl = imageInfo.getOriginUrl();
        Print.d(TAG, "loadOrigin originUrl = " + originalUrl);
        if (imageHashMap == null || imageGifHashMap == null) {
            Print.d(TAG, "hash map == null");
            notifyDataSetChanged();
            return;
        }

        if (imageHashMap.get(originalUrl) != null && imageGifHashMap.get(originalUrl) != null) {
            SubsamplingScaleImageViewDragClose imageView = imageHashMap.get(imageInfo.getOriginUrl());
            PhotoView imageGif = imageGifHashMap.get(imageInfo.getOriginUrl());

            File cacheFile = ImageLoader.getGlideCacheFile(activity, imageInfo.getOriginUrl());
            if (cacheFile != null && cacheFile.exists()) {
                boolean isCacheIsGif = ImageUtil.isGifImageWithMime(cacheFile.getAbsolutePath());
                if (isCacheIsGif) {
                    imageGif.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);

                    Glide.with(activity)
                            .asGif()
                            .load(cacheFile)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(ImagePreview.getInstance().getErrorPlaceHolder()))
                            .into(imageGif);
                } else {
                    imageGif.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                    String thumbnailUrl = imageInfo.getThumbnailUrl();
                    File smallCacheFile = ImageLoader.getGlideCacheFile(activity, thumbnailUrl);

                    ImageSource small = null;
                    if (smallCacheFile != null && smallCacheFile.exists()) {
                        String smallImagePath = smallCacheFile.getAbsolutePath();
                        small = ImageSource.bitmap(ImageUtil.getImageBitmap(smallImagePath, ImageUtil.getBitmapDegree(smallImagePath)));
                        int widSmall = ImageUtil.getWidthHeight(smallImagePath)[0];
                        int heiSmall = ImageUtil.getWidthHeight(smallImagePath)[1];
                        small.dimensions(widSmall, heiSmall);
                    }

                    String imagePath = cacheFile.getAbsolutePath();
                    ImageSource origin = ImageSource.uri(imagePath);
                    int widOrigin = ImageUtil.getWidthHeight(imagePath)[0];
                    int heiOrigin = ImageUtil.getWidthHeight(imagePath)[1];
                    origin.dimensions(widOrigin, heiOrigin);
                    boolean isLongImage = ImageUtil.isLongImage(imagePath);
                    if (isLongImage) {
                        imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
                    } else {
                        imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_CENTER_INSIDE);
                    }
                    imageView.setRotation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                    imageView.setImage(origin, small);
                }
            } else {
                Print.d(TAG, "load original cache == null");
                notifyDataSetChanged();
            }
        } else {
            Print.d(TAG, "hash map get == null");
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (activity == null) {
            return container;
        }
        View convertView = View.inflate(activity, R.layout.kf5_imageviewer_item_photoview, null);
        final ProgressBar progressBar = convertView.findViewById(R.id.progress_view);
        final FingerDragHelper fingerDragHelper = convertView.findViewById(R.id.fingerDragHelper);
        final SubsamplingScaleImageViewDragClose imageView = convertView.findViewById(R.id.photo_view);
        final PhotoView imageGif = convertView.findViewById(R.id.gif_view);

        final ImageInfo info = this.imageInfo.get(position);
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

        if (ImagePreview.getInstance().isEnableClickClose()) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
            imageGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }

        if (ImagePreview.getInstance().isEnableDragClose()) {
            fingerDragHelper.setOnAlphaChangedListener(new FingerDragHelper.onAlphaChangedListener() {
                @Override
                public void onTranslationYChanged(MotionEvent event, float translationY) {
                    float yAbs = Math.abs(translationY);
                    float percent = yAbs / phoneHeight;
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

        imageGifHashMap.remove(originPathUrl);
        imageGifHashMap.put(originPathUrl, imageGif);

        imageHashMap.remove(originPathUrl);
        imageHashMap.put(originPathUrl, imageView);

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
                imageGif.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                Glide.with(activity)
                        .asGif()
                        .load(cacheFile)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(ImagePreview.getInstance().getErrorPlaceHolder()))
                        .listener(new RequestListener<GifDrawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target,
                                                        boolean isFirstResource) {
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
            } else {
                imageGif.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                String imagePath = cacheFile.getAbsolutePath();
                boolean isLongImage = ImageUtil.isLongImage(imagePath);
                if (isLongImage) {
                    imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
                }
                imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                imageView.setImage(ImageSource.uri(Uri.fromFile(new File(cacheFile.getAbsolutePath()))));
                imageView.setOnImageEventListener(new SubsamplingScaleImageViewDragClose.DefaultOnImageEventListener() {
                    @Override
                    public void onReady() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
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
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target,
                                                            boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);

                                    imageGif.setVisibility(View.GONE);
                                    imageView.setVisibility(View.VISIBLE);
                                    imageView.setImage(ImageSource.resource(ImagePreview.getInstance().getErrorPlaceHolder()));

                                    String errorMsg = activity.getResources().getString(R.string.kf5_imageviewer_failed_to_load_img);
                                    if (e != null) {
                                        errorMsg = errorMsg.concat(":\n").concat(e.getMessage());
                                    }
                                    if (errorMsg.length() > 200) {
                                        errorMsg = errorMsg.substring(0, 199);
                                    }
                                    MyToast.getInstance()._short(activity.getApplicationContext(), errorMsg);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource,
                                                               boolean isFirstResource) {
                                    String imagePath = resource.getAbsolutePath();
                                    boolean isCacheIsGif = ImageUtil.isGifImageWithMime(imagePath);
                                    if (isCacheIsGif) {
                                        imageGif.setVisibility(View.VISIBLE);
                                        imageView.setVisibility(View.GONE);
                                        Glide.with(activity)
                                                .asGif()
                                                .load(imagePath)
                                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(ImagePreview.getInstance().getErrorPlaceHolder()))
                                                .listener(new RequestListener<GifDrawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                                                Target<GifDrawable> target, boolean isFirstResource) {
                                                        imageGif.setVisibility(View.GONE);
                                                        imageView.setVisibility(View.VISIBLE);
                                                        imageView.setImage(ImageSource.resource(ImagePreview.getInstance().getErrorPlaceHolder()));
                                                        return true;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target,
                                                                                   DataSource dataSource, boolean isFirstResource) {
                                                        progressBar.setVisibility(View.GONE);
                                                        return false;
                                                    }
                                                })
                                                .into(imageGif);
                                    } else {
                                        imageGif.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);

                                        boolean isLongImage = ImageUtil.isLongImage(imagePath);
                                        if (isLongImage) {
                                            imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
                                        }
                                        imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                                        imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                                        imageView.setOnImageEventListener(new SubsamplingScaleImageViewDragClose.DefaultOnImageEventListener() {
                                            @Override
                                            public void onReady() {
                                                progressBar.setVisibility(View.GONE);
                                            }

                                        });
                                    }
                                    return true;
                                }
                            }).into(new FileTarget());
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            String imagePath = resource.getAbsolutePath();
                            boolean isCacheIsGif = ImageUtil.isGifImageWithMime(imagePath);
                            if (isCacheIsGif) {
                                imageGif.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.GONE);
                                Glide.with(activity)
                                        .asGif()
                                        .load(imagePath)
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(ImagePreview.getInstance().getErrorPlaceHolder()))
                                        .listener(new RequestListener<GifDrawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target,
                                                                        boolean isFirstResource) {
                                                imageGif.setVisibility(View.GONE);
                                                imageView.setVisibility(View.VISIBLE);
                                                imageView.setImage(ImageSource.resource(ImagePreview.getInstance().getErrorPlaceHolder()));
                                                return true;
                                            }

                                            @Override
                                            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target,
                                                                           DataSource dataSource, boolean isFirstResource) {
                                                progressBar.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(imageGif);
                            } else {
                                imageGif.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);

                                boolean isLongImage = ImageUtil.isLongImage(imagePath);
                                if (isLongImage) {
                                    imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
                                }
                                imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                                imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                                imageView.setOnImageEventListener(new SubsamplingScaleImageViewDragClose.DefaultOnImageEventListener() {
                                    @Override
                                    public void onReady() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                            return true;
                        }
                    }).into(new FileTarget());
                    return true;
                }

                @Override
                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource,
                                               boolean isFirstResource) {
                    String imagePath = resource.getAbsolutePath();
                    boolean isCacheIsGif = ImageUtil.isGifImageWithMime(imagePath);
                    if (isCacheIsGif) {
                        imageGif.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                        Glide.with(activity)
                                .asGif()
                                .load(imagePath)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(ImagePreview.getInstance().getErrorPlaceHolder()))
                                .listener(new RequestListener<GifDrawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target,
                                                                boolean isFirstResource) {
                                        imageGif.setVisibility(View.GONE);
                                        imageView.setVisibility(View.VISIBLE);
                                        imageView.setImage(ImageSource.resource(ImagePreview.getInstance().getErrorPlaceHolder()));
                                        return true;
                                    }

                                    @Override
                                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target,
                                                                   DataSource dataSource, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(imageGif);
                    } else {
                        imageGif.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);

                        boolean isLongImage = ImageUtil.isLongImage(imagePath);
                        if (isLongImage) {
                            imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
                        }
                        imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
                        imageView.setImage(ImageSource.uri(Uri.fromFile(new File(resource.getAbsolutePath()))));
                        imageView.setOnImageEventListener(new SubsamplingScaleImageViewDragClose.DefaultOnImageEventListener() {
                            @Override
                            public void onReady() {
                                progressBar.setVisibility(View.GONE);
                            }

                        });
                    }
                    return true;
                }
            }).into(new FileTarget());
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        try {
            container.removeView(((View) object));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ImageLoader.clearMemory(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (imageHashMap != null && imageHashMap.get(imageInfo.get(position).getOriginUrl()) != null) {
                imageHashMap.get(imageInfo.get(position).getOriginUrl()).destroyDrawingCache();
                imageHashMap.get(imageInfo.get(position).getOriginUrl()).recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (imageGifHashMap != null && imageGifHashMap.get(imageInfo.get(position).getOriginUrl()) != null) {
                imageGifHashMap.get(imageInfo.get(position).getOriginUrl()).destroyDrawingCache();
                imageGifHashMap.get(imageInfo.get(position).getOriginUrl()).setImageBitmap(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
