package com.kf5.sdk.system.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kf5.sdk.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author:chosen
 * date:2016/10/19 11:25
 * email:812219713@qq.com
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener, AdapterView.OnItemLongClickListener {

    private final static int RELEASE_To_REFRESH = 0;

    private final static int PULL_To_REFRESH = 1;

    private final static int REFRESHING = 2;

    private final static int DONE = 3;

    private final static int LOADING = 4;

    private final static int RATIO = 3;

    private LayoutInflater inflater;

    private LinearLayout headView;

    private TextView tipsTextview;

    private TextView lastUpdatedTextView;

    private ImageView arrowImageView;

    private ProgressBar progressBar;

    private onScrollState onScrollState;

    private onScrollChange onScrollChange;

    private RotateAnimation animation;

    private RotateAnimation reverseAnimation;

    private boolean isRecored;

    protected int headContentWidth;

    private int headContentHeight;

    private int startY;

    private int firstItemIndex;

    private int state;

    private boolean isBack;

    private OnRefreshListener refreshListener;

    private boolean isRefreshable;

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    private boolean isRefresh;

    private LinearLayout footerLayout;

    private ProgressBar footerBar;

    private TextView footerDesc;

    private int footerViewHeight;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public void setOnScrollState(onScrollState onScrollState) {
        this.onScrollState = onScrollState;
    }

    public void setOnScrollChange(onScrollChange onScrollChange) {
        this.onScrollChange = onScrollChange;
    }


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.kf5_list_head, null);
        arrowImageView = (ImageView) headView.findViewById(R.id.kf5_head_arrowImageView);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.kf5_head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.kf5_head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.kf5_head_lastUpdatedTextView);
        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();
        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();
        addHeaderView(headView);
        setOnScrollListener(this);
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);
        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        state = DONE;
        isRefreshable = false;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        firstItemIndex = firstVisibleItem;
        if (onScrollChange != null) {
            onScrollChange.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private View getFooterView() {
        footerLayout = (LinearLayout) inflater.inflate(R.layout.kf5_listview_footerview, null);
        footerBar = (ProgressBar) footerLayout.findViewById(R.id.kf5_footer_progressBar);
        footerDesc = (TextView) footerLayout.findViewById(R.id.kf5_footer_tips);
        footerLayout.setPadding(0, 0, 0, 0);
        return footerLayout;
    }

    /**
     * 添加底部footerView
     */
    public void addFooterView() {
        addFooterView(getFooterView());
    }

    /**
     * 设置底部footerView状态为加载更多内容
     */
    public void setFooterViewLoadingData() {
        resetFooterViewState();
        footerBar.setVisibility(View.VISIBLE);
        footerDesc.setText(getContext().getString(R.string.kf5_loading));
        footerLayout.setVisibility(View.VISIBLE);
    }


    /**
     * 设置底部footerView状态为没有内容
     */
    public void setFooterViewNoData() {
        footerBar.setVisibility(View.GONE);
        footerDesc.setText(getContext().getString(R.string.kf5_no_data));
        footerLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 重置底部footerView状态
     */
    public void resetFooterViewState() {
        footerLayout.setPadding(0, 0, 0, 0);
    }

    /**
     * 没有更多内容时，采用动画设置底部View消失
     */
    public void setFooterViewInvisibleWithAnim() {
        footerBar.setVisibility(View.GONE);
        footerViewHeight = footerLayout.getMeasuredHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setTarget(footerLayout);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                footerLayout.setPadding(0, 0, 0, -(int) (i / 100 * footerViewHeight));
            }
        });
        valueAnimator.start();
    }

    /**
     * 直接设置底部View不可见
     */
    public void setFooterViewInvisible() {
        if (footerLayout != null) {
            footerViewHeight = footerLayout.getMeasuredHeight();
            footerLayout.setPadding(0, -1 * footerViewHeight, 0, 0);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:

                    if (state != REFRESHING && state != LOADING) {
                        if (state == PULL_To_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();

                        }
                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }

                    isRecored = false;
                    isBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();

                    if (!isRecored && firstItemIndex == 0) {
                        isRecored = true;
                        startY = tempY;
                    }

                    if (state != REFRESHING && isRecored && state != LOADING) {

                        if (state == RELEASE_To_REFRESH) {

                            setSelection(0);

                            if (((tempY - startY) / RATIO < headContentHeight)
                                    && (tempY - startY) > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();

                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                            } else {
                            }
                        }
                        if (state == PULL_To_REFRESH) {

                            setSelection(0);

                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }

                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        if (state == PULL_To_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight
                                    + (tempY - startY) / RATIO, 0, 0);

                        }

                        if (state == RELEASE_To_REFRESH) {
                            headView.setPadding(0, (tempY - startY) / RATIO
                                    - headContentHeight, 0, 0);
                        }

                    }

                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                tipsTextview.setText(getContext().getString(R.string.kf5_release_to_update));

                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                    tipsTextview.setText(getContext().getString(R.string.kf5_pull_to_update));
                } else {
                    tipsTextview.setText(getContext().getString(R.string.kf5_pull_to_update));
                }
                break;

            case REFRESHING:
                headView.setPadding(0, 0, 0, 0);
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText(getContext().getString(R.string.kf5_updating));
                lastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
            case DONE:
                if (isRefresh) {
                    ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
                    valueAnimator.setTarget(headView);
                    valueAnimator.setDuration(500);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                            headView.setPadding(0, -(int) (i / 100 * headContentHeight), 0, 0);
                        }
                    });
                    valueAnimator.start();
                } else {
                    headView.setPadding(0, -1 * headContentHeight, 0, 0);
                }
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.kf5_ic_pulltorefresh_arrow);
                tipsTextview.setText(getContext().getString(R.string.kf5_release_to_update));
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setStateRefreshing() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setTarget(headView);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float i = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                headView.setPadding(0, (int) ((i / 100 * headContentHeight) - headContentHeight), 0, 0);
            }
        });
        valueAnimator.start();
        progressBar.setVisibility(View.VISIBLE);
        arrowImageView.clearAnimation();
        arrowImageView.setVisibility(View.GONE);
        tipsTextview.setText(getContext().getString(R.string.kf5_updating));
        lastUpdatedTextView.setVisibility(View.VISIBLE);

    }

    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void onRefreshComplete() {
        state = DONE;
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String date = format.format(new Date());
        lastUpdatedTextView.setText(getContext().getString(R.string.kf5_up_update_date, date));
        changeHeaderViewByState();
    }

    private void onRefresh() {
        if (null != refreshListener) {
            isRefresh = true;
            refreshListener.onRefresh();
            resetFooterViewState();
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
        String date = format.format(new Date());
        lastUpdatedTextView.setText(getContext().getString(R.string.kf5_up_update_date, date));

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (onScrollState != null) {
            onScrollState.onScrollStateChanged(view, scrollState);

        }
    }

    public interface onScrollState {
        void onScrollStateChanged(AbsListView view, int scrollState);
    }
    public interface onScrollChange {
        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }


}