package com.kf5.sdk.system.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kf5.sdk.R;
import com.kf5.sdk.system.photoview.OnPhotoTapListener;
import com.kf5.sdk.system.photoview.PhotoView;
import com.kf5.sdk.system.utils.ImageLoaderManager;

/**
 * author:chosen
 * date:2016/11/17 11:40
 * email:812219713@qq.com
 */

public class ImageDetailFragment extends Fragment {

    private String mImageUrl;

    private PhotoView mImageView;

    private ProgressBar mProgressBar;

    private static final String URL = "url";

    private View mView;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment detailFragment = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString(URL, imageUrl);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString(URL) : null;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.kf5_image_detail_fragment, container, false);
        mImageView = (PhotoView) mView.findViewById(R.id.kf5_image);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.kf5_loading);
        mImageView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageLoaderManager.getInstance(getActivity()).displayImage(mImageUrl, mImageView, new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                try {
                    if (mProgressBar != null)
                        mProgressBar.setVisibility(View.GONE);
                    if (isAdded()) {
                        Toast.makeText(getActivity(), getString(R.string.kf5_display_error_hint), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (mProgressBar != null)
                    mProgressBar.setVisibility(View.GONE);
                return false;
            }
        });
    }
}
