package com.kf5.sdk.system.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.fragment.ImageDetailFragment;
import com.kf5.sdk.system.widget.CustomViewPager;

import java.util.List;

public class ImageActivity extends FragmentActivity {

    private static final String STATE_POSITION = "STATE_POSITION";

    private ViewPager mCustomViewPager;

    private int pagerPosition;

    private TextView mTVIndicator;

    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf5_image_detail_pager);
        if (savedInstanceState != null)
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        pagerPosition = getIntent().getIntExtra(Field.EXTRA_IMAGE_INDEX, 0);
        mList = getIntent().getStringArrayListExtra(Field.EXTRA_IMAGE_URLS);
        mCustomViewPager = (ViewPager) findViewById(R.id.kf5_pager);
        mTVIndicator = (TextView) findViewById(R.id.kf5_indicator);
        mCustomViewPager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(), mList.toArray(new String[mList.size()])));
        mTVIndicator.setText(getString(R.string.kf5_viewpager_indicator, 1, mCustomViewPager.getAdapter().getCount()));
        mCustomViewPager.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTVIndicator.setText(getString(R.string.kf5_viewpager_indicator, position + 1, mCustomViewPager.getAdapter().getCount()));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCustomViewPager.setCurrentItem(pagerPosition);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_POSITION, mCustomViewPager.getCurrentItem());
    }

    private static class ImagePagerAdapter extends FragmentStatePagerAdapter {

        private final String[] urls;

        private ImagePagerAdapter(FragmentManager fragmentManager, String[] urls) {
            super(fragmentManager);
            this.urls = urls;
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            String url = urls[position];
            return ImageDetailFragment.newInstance(url);
//            return ImageFragment.newInstance(url);
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return urls != null ? urls.length : 0;
        }
    }

}
