package com.chosen.album.internal.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.chosen.imageviewer.view.HackyViewPager;


/**
 * @author Chosen
 * @create 2019/1/9 16:35
 * @email 812219713@qq.com
 */
public class PreviewViewPager extends HackyViewPager {

    public PreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
//        if (v instanceof ImageViewTouch) {
//            return ((ImageViewTouch) v).canScroll(dx) || super.canScroll(v, checkV, dx, x, y);
//        }
//        return super.canScroll(v, checkV, dx, x, y);
//    }
}
