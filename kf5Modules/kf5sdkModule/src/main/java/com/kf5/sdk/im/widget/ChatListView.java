package com.kf5.sdk.im.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/10/17 15:35
 * email:812219713@qq.com
 */

public class ChatListView extends ListView {

    private LayoutInflater inflater;

    private View view;

    public ChatListView(Context context) {
        this(context, null);
    }

    public ChatListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

//        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.kf5_list_view_footer_or_head_view, null);
//        view.setVisibility(View.GONE);
//        addHeaderView(view);
//        setTranscriptMode(TRANSCRIPT_MODE_NORMAL);
//        setVerticalScrollBarEnabled(false);
//        setFastScrollEnabled(false);
//        setStackFromBottom(false);
//        setItemsCanFocus(false);
//        setFocusable(true);
//        setFocusableInTouchMode(true);

    }


    /**
     * 设置头部progressbar的可见性
     */
    public void setHeaderViewInvisible() {

        if (view != null && view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }

    }

    /**
     * 设置headerview课件
     */
    public void setHeaderViewVisible() {

        if (view != null && view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 设置滚动模式正常
     */
    public void setTranscriptModeNormal() {

        if (getTranscriptMode() != TRANSCRIPT_MODE_NORMAL) {
            setTranscriptMode(TRANSCRIPT_MODE_NORMAL);
        }
    }


    /**
     * 设置自动滚动到底部
     */
    public void setTranscriptModeScroll() {


        if (getTranscriptMode() != TRANSCRIPT_MODE_ALWAYS_SCROLL) {
            setTranscriptMode(TRANSCRIPT_MODE_ALWAYS_SCROLL);
        }
        if (getLastVisiblePosition() != getCount() - 1) {
            setSelection(getBottom());
        }

    }


    /**
     * 延迟更新
     */
    public void postUpdateContent() {

        postDelayed(new Runnable() {

            @Override
            public void run() {

                setSelection(getBottom());
            }
        }, 500);

    }
}
