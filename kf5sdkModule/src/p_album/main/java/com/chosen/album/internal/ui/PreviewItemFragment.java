package com.chosen.album.internal.ui;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chosen.album.internal.entity.Item;
import com.chosen.album.internal.entity.SelectionSpec;
import com.chosen.album.internal.utils.PathUtils;
import com.chosen.album.internal.utils.PhotoMetadataUtils;
import com.chosen.album.listener.OnFragmentInteractionListener;
import com.chosen.imageviewer.ImagePreview;
import com.chosen.imageviewer.tool.image.ImageUtil;
import com.chosen.imageviewer.view.photoview.PhotoView;
import com.chosen.imageviewer.view.scaleview.ImageSource;
import com.chosen.imageviewer.view.scaleview.SubsamplingScaleImageView;
import com.chosen.imageviewer.view.scaleview.SubsamplingScaleImageViewDragClose;
import com.chosen.videoplayer.Jzvd;
import com.chosen.videoplayer.JzvdStd;
import com.kf5.sdk.R;

import java.io.File;


/**
 * @author Chosen
 * @create 2019/1/9 16:34
 * @email 812219713@qq.com
 */
public class PreviewItemFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";
    private OnFragmentInteractionListener mListener;

    SubsamplingScaleImageViewDragClose imageView;
    PhotoView photoView;

    public static PreviewItemFragment newInstance(Item item) {
        PreviewItemFragment fragment = new PreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.kf5_album_fragment_preview_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Item item = getArguments().getParcelable(ARGS_ITEM);
        if (item == null) {
            return;
        }
        View videoPlayButton = view.findViewById(R.id.video_play_button);
        if (item.isVideo()) {
            videoPlayButton.setVisibility(View.VISIBLE);
            videoPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = PathUtils.getPath(v.getContext(), item.uri);
                    if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                        File file = new File(path);
                        Jzvd.startFullscreen(v.getContext(), JzvdStd.class, file.getAbsolutePath(), !TextUtils.isEmpty(file.getName()) ? file.getName() : "视频");
                    }
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(item.uri, "video/*");
//                    try {
//                        startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        Toast.makeText(getContext(), R.string.error_no_video_activity, Toast.LENGTH_SHORT).show();
//                    }
                }
            });
        } else {
            videoPlayButton.setVisibility(View.GONE);
        }

        imageView = view.findViewById(R.id.photo_view);
        photoView = view.findViewById(R.id.gif_or_image_view);
        final ProgressBar progressBar = view.findViewById(R.id.progress_view);
        Point size = PhotoMetadataUtils.getBitmapSize(item.getContentUri(), getActivity());
        if (item.isGif() || item.isVideo()) {
            photoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            if (item.isGif()) {
                SelectionSpec.getInstance().imageEngine.loadGifImage(getContext(), size.x, size.y, photoView,
                        item.getContentUri());
            } else {
                SelectionSpec.getInstance().imageEngine.loadImage(getContext(), size.x, size.y, photoView,
                        item.getContentUri());
            }
            photoView.setOnClickListener(new ViewClickListener());
        } else {
            photoView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_CENTER_INSIDE);
            imageView.setDoubleTapZoomStyle(SubsamplingScaleImageViewDragClose.ZOOM_FOCUS_CENTER);
            imageView.setDoubleTapZoomDuration(ImagePreview.getInstance().getZoomTransitionDuration());
            imageView.setMinScale(ImagePreview.getInstance().getMinScale());
            imageView.setMaxScale(ImagePreview.getInstance().getMaxScale());
            imageView.setDoubleTapZoomScale(ImagePreview.getInstance().getMediumScale());
            boolean isLongImage = ImageUtil.isLongImage(getActivity(), PathUtils.getPath(getActivity(), item.getContentUri()));
            if (isLongImage) {
                imageView.setMinimumScaleType(SubsamplingScaleImageViewDragClose.SCALE_TYPE_START);
            }
            imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_USE_EXIF);
            imageView.setImage(ImageSource.uri(item.getContentUri()));
            imageView.setOnImageEventListener(new SubsamplingScaleImageViewDragClose.DefaultOnImageEventListener() {
                @Override
                public void onReady() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            imageView.setOnClickListener(new ViewClickListener());
        }


//        ImageViewTouch image = (ImageViewTouch) view.findViewById(R.id.image_view);
//        image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
//
//        image.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
//            @Override
//            public void onSingleTapConfirmed() {
//                if (mListener != null) {
//                    mListener.onClick();
//                }
//            }
//        });
//
//        Point size = PhotoMetadataUtils.getBitmapSize(item.getContentUri(), getActivity());
//        if (item.isGif()) {
//            SelectionSpec.getInstance().imageEngine.loadGifImage(getContext(), size.x, size.y, image,
//                    item.getContentUri());
//        } else {
//            SelectionSpec.getInstance().imageEngine.loadImage(getContext(), size.x, size.y, image,
//                    item.getContentUri());
//        }

    }

    public void resetView() {
        if (getView() != null) {
//            ((ImageViewTouch) getView().findViewById(R.id.image_view)).resetMatrix();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        if (imageView != null) {
            imageView.destroyDrawingCache();
            imageView.recycle();
            imageView = null;
        }
        photoView = null;
        super.onDestroyView();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class ViewClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick();
            }
        }
    }
}
