package com.kf5.sdk.system.swipeback;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kf5.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/11/8 16:58
 * email:812219713@qq.com
 */

public class SwipeBackLayout extends FrameLayout {

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    private static final int FULL_ALPHA = 255;

    /**
     * Edge flag indicating that the left edge should be affected.
     */
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;

    /**
     * Edge flag indicating that the right edge should be affected.
     */
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;

    /**
     * Edge flag indicating that the bottom edge should be affected.
     */
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;

    /**
     * Edge flag set indicating all edges should be affected.
     */
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM;

    /**
     * A view is not currently being dragged or animating as a result of a
     * fling/snap.
     */
    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;

    /**
     * A view is currently being dragged. The position is currently changing as
     * a result of user input or simulated user input.
     */
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    /**
     * Default threshold of scroll
     */
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;

    private static final int OVER_SCROLL_DISTANCE = 10;

    private static final int[] EDGE_FLAGS = {
            EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL
    };

    private int mEdgeFlag;

    /**
     * Threshold of scroll, we will close the activity, when scrollPercent over
     * this value;
     */
    private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;

    private Activity mActivity;

    private boolean mEnable = true;

    private View mContentView;

    private ViewDragHelper mDragHelper;

    private float mScrollPercent;

    private int mContentLeft;

    private int mContentTop;

    /**
     * The set of listeners to be sent events through.
     */
    private List<SwipeListener> mListeners;

    private Drawable mShadowLeft;

    private Drawable mShadowRight;

    private Drawable mShadowBottom;

    private float mScrimOpacity;

    private int mScrimColor = DEFAULT_SCRIM_COLOR;

    private boolean mInLayout;

    private Rect mTmpRect = new Rect();

    /**
     * Edge being dragged
     */
    private int mTrackingEdge;

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.KF5SwipeBackLayoutStyle);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KF5SwipeBackLayout, defStyle,
                R.style.KF5SwipeBackLayout);

        int edgeSize = a.getDimensionPixelSize(R.styleable.KF5SwipeBackLayout_kf5_sbl_edge_size, -1);
        if (edgeSize > 0)
            setEdgeSize(edgeSize);
        int mode = EDGE_FLAGS[a.getInt(R.styleable.KF5SwipeBackLayout_kf5_sbl_edge_flag, 0)];
        setEdgeTrackingEnabled(mode);

        int shadowLeft = a.getResourceId(R.styleable.KF5SwipeBackLayout_kf5_sbl_shadow_left,
                R.drawable.kf5_shadow_left);
        int shadowRight = a.getResourceId(R.styleable.KF5SwipeBackLayout_kf5_sbl_shadow_right,
                R.drawable.kf5_shadow_right);
        int shadowBottom = a.getResourceId(R.styleable.KF5SwipeBackLayout_kf5_sbl_shadow_bottom, R.drawable.kf5_shadow_bottom);
        setShadow(shadowLeft, EDGE_LEFT);
        setShadow(shadowRight, EDGE_RIGHT);
        setShadow(shadowBottom, EDGE_BOTTOM);
        a.recycle();
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setMaxVelocity(minVel * 2f);
    }

    /**
     * Sets the sensitivity of the NavigationLayout.
     *
     * @param context     The application context.
     * @param sensitivity value between 0 and 1, the final value for touchSlop =
     *                    ViewConfiguration.getScaledTouchSlop * (1 / s);
     */
    public void setSensitivity(Context context, float sensitivity) {
        mDragHelper.setSensitivity(context, sensitivity);
    }

    /**
     * Set up contentView which will be moved by user gesture
     *
     * @param view
     */
    private void setContentView(View view) {
        mContentView = view;
    }

    public void setEnableGesture(boolean enable) {
        mEnable = enable;
    }

    /**
     * Enable edge tracking for the selected edges of the parent view. The
     * callback's
     * {@link ViewDragHelper.Callback#onEdgeTouched(int, int)}
     * and
     * {@link ViewDragHelper.Callback#onEdgeDragStarted(int, int)}
     * methods will only be invoked for edges for which edge tracking has been
     * enabled.
     *
     * @param edgeFlags Combination of edge flags describing the edges to watch
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setEdgeTrackingEnabled(int edgeFlags) {
        mEdgeFlag = edgeFlags;
        mDragHelper.setEdgeTrackingEnabled(mEdgeFlag);
    }

    /**
     * Set a color to use for the scrim that obscures primary content while a
     * drawer is open.
     *
     * @param color Color to use in 0xAARRGGBB format.
     */
    public void setScrimColor(int color) {
        mScrimColor = color;
        invalidate();
    }

    /**
     * Set the size of an edge. This is the range in pixels along the edges of
     * this view that will actively detect edge touches or drags if edge
     * tracking is enabled.
     *
     * @param size The size of an edge in pixels
     */
    public void setEdgeSize(int size) {
        mDragHelper.setEdgeSize(size);
    }

    /**
     * Register a callback to be invoked when a swipe event is sent to this
     * view.
     *
     * @param listener the swipe listener to attach to this view
     * @deprecated use {@link #addSwipeListener} instead
     */
    @Deprecated
    public void setSwipeListener(SwipeListener listener) {
        addSwipeListener(listener);
    }

    /**
     * Add a callback to be invoked when a swipe event is sent to this view.
     *
     * @param listener the swipe listener to attach to this view
     */
    public void addSwipeListener(SwipeListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<SwipeListener>();
        }
        mListeners.add(listener);
    }

    /**
     * Removes a listener from the set of listeners
     *
     * @param listener
     */
    public void removeSwipeListener(SwipeListener listener) {
        if (mListeners == null) {
            return;
        }
        mListeners.remove(listener);
    }

    public interface SwipeListener {
        /**
         * Invoke when state change
         *
         * @param state         flag to describe scroll state
         * @param scrollPercent scroll percent of this view
         * @see #STATE_IDLE
         * @see #STATE_DRAGGING
         * @see #STATE_SETTLING
         */
        void onScrollStateChange(int state, float scrollPercent);

        /**
         * Invoke when edge touched
         *
         * @param edgeFlag edge flag describing the edge being touched
         * @see #EDGE_LEFT
         * @see #EDGE_RIGHT
         * @see #EDGE_BOTTOM
         */
        void onEdgeTouch(int edgeFlag);

        /**
         * Invoke when scroll percent over the threshold for the first time
         */
        void onScrollOverThreshold();
    }

    /**
     * Set scroll threshold, we will close the activity, when scrollPercent over
     * this value
     *
     * @param threshold
     */
    public void setScrollThresHold(float threshold) {
        if (threshold >= 1.0f || threshold <= 0) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        mScrollThreshold = threshold;
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param shadow   Drawable to use
     * @param edgeFlag
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setShadow(Drawable shadow, int edgeFlag) {
        if ((edgeFlag & EDGE_LEFT) != 0) {
            mShadowLeft = shadow;
        } else if ((edgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight = shadow;
        } else if ((edgeFlag & EDGE_BOTTOM) != 0) {
            mShadowBottom = shadow;
        }
        invalidate();
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param resId    Resource of drawable to use
     * @param edgeFlag
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setShadow(int resId, int edgeFlag) {
        setShadow(getResources().getDrawable(resId), edgeFlag);
    }

    /**
     * Scroll out contentView and finish the activity
     */
    public void scrollToFinishActivity() {
        final int childWidth = mContentView.getWidth();
        final int childHeight = mContentView.getHeight();

        int left = 0, top = 0;
        if ((mEdgeFlag & EDGE_LEFT) != 0) {
            left = childWidth + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE;
            mTrackingEdge = EDGE_LEFT;
        } else if ((mEdgeFlag & EDGE_RIGHT) != 0) {
            left = -childWidth - mShadowRight.getIntrinsicWidth() - OVER_SCROLL_DISTANCE;
            mTrackingEdge = EDGE_RIGHT;
        } else if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
            top = -childHeight - mShadowBottom.getIntrinsicHeight() - OVER_SCROLL_DISTANCE;
            mTrackingEdge = EDGE_BOTTOM;
        }
        mDragHelper.smoothSlideViewTo(mContentView, left, top);
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        try {
            return mDragHelper.shouldInterceptTouchEvent(event);
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: handle exception
            // issues #9
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = true;
        try {
            if (mContentView != null)
                mContentView.layout(mContentLeft, mContentTop,
                        mContentLeft + mContentView.getMeasuredWidth(),
                        mContentTop + mContentView.getMeasuredHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mInLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;

        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (mScrimOpacity > 0 && drawContent
                && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);

        if ((mTrackingEdge & EDGE_LEFT) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = mTmpRect;
        child.getHitRect(childRect);

        if ((mEdgeFlag & EDGE_LEFT) != 0) {
            mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top,
                    childRect.left, childRect.bottom);
            mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowLeft.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight.setBounds(childRect.right, childRect.top,
                    childRect.right + mShadowRight.getIntrinsicWidth(), childRect.bottom);
            mShadowRight.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowRight.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
            mShadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right,
                    childRect.bottom + mShadowBottom.getIntrinsicHeight());
            mShadowBottom.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowBottom.draw(canvas);
        }
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private boolean mIsScrollOverValid;


        /**
         * Called when the user's input indicates that they want to capture the
         * given child view with the pointer indicated by pointerId. The
         * callback should return true if the user is permitted to drag the
         * given view with the indicated pointer.
         * <p>
         * ViewDragHelper may call this method multiple times for the same view
         * even if the view is already captured; this indicates that a new
         * pointer is trying to take control of the view.
         * </p>
         * <p>
         * If this method returns true, a call to
         * {@link #onViewCaptured(View, int)} will follow if the
         * capture is successful.
         * </p>
         *
         * @param child     Child the user is attempting to capture
         * @param pointerId ID of the pointer attempting the capture
         * @return true if capture should be allowed, false otherwise
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            boolean ret = mDragHelper.isEdgeTouched(mEdgeFlag, pointerId);
            if (ret) {
                if (mDragHelper.isEdgeTouched(EDGE_LEFT, pointerId)) {
                    mTrackingEdge = EDGE_LEFT;
                } else if (mDragHelper.isEdgeTouched(EDGE_RIGHT, pointerId)) {
                    mTrackingEdge = EDGE_RIGHT;
                } else if (mDragHelper.isEdgeTouched(EDGE_BOTTOM, pointerId)) {
                    mTrackingEdge = EDGE_BOTTOM;
                }
                if (mListeners != null && !mListeners.isEmpty()) {
                    for (SwipeListener listener : mListeners) {
                        listener.onEdgeTouch(mTrackingEdge);
                    }
                }
                mIsScrollOverValid = true;
            }
            boolean directionCheck = false;
            if (mEdgeFlag == EDGE_LEFT || mEdgeFlag == EDGE_RIGHT) {
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, pointerId);
            } else if (mEdgeFlag == EDGE_BOTTOM) {
                directionCheck = !mDragHelper
                        .checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, pointerId);
            } else if (mEdgeFlag == EDGE_ALL) {
                directionCheck = true;
            }
            return ret & directionCheck;
        }

        /**
         * Return the magnitude of a draggable child view's horizontal range of
         * motion in pixels. This method should return 0 for views that cannot
         * move horizontally.
         *
         * @param child Child view to check
         * @return range of horizontal motion in pixels
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mEdgeFlag & (EDGE_LEFT | EDGE_RIGHT);
        }

        /**
         * Return the magnitude of a draggable child view's vertical range of
         * motion in pixels. This method should return 0 for views that cannot
         * move vertically.
         *
         * @param child Child view to check
         * @return range of vertical motion in pixels
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return mEdgeFlag & EDGE_BOTTOM;
        }

        /**
         * Called when the captured view's position changes as the result of a
         * drag or settle.
         *
         * @param changedView View whose position changed
         * @param left        New X coordinate of the left edge of the view
         * @param top         New Y coordinate of the top edge of the view
         * @param dx          Change in X position from the last call
         * @param dy          Change in Y position from the last call
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                mScrollPercent = Math.abs((float) left
                        / (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left
                        / (mContentView.getWidth() + mShadowRight.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                mScrollPercent = Math.abs((float) top
                        / (mContentView.getHeight() + mShadowBottom.getIntrinsicHeight()));
            }
            mContentLeft = left;
            mContentTop = top;
            invalidate();
            if (mScrollPercent < mScrollThreshold && !mIsScrollOverValid) {
                mIsScrollOverValid = true;
            }
            if (mListeners != null && !mListeners.isEmpty()
                    && mDragHelper.getViewDragState() == STATE_DRAGGING
                    && mScrollPercent >= mScrollThreshold && mIsScrollOverValid) {
                mIsScrollOverValid = false;
                for (SwipeListener listener : mListeners) {
                    listener.onScrollOverThreshold();
                }
            }

            if (mScrollPercent >= 1) {
                if (!mActivity.isFinishing()) {
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                }
            }
        }

        /**
         * Called when the child view is no longer being actively dragged. The
         * fling velocity is also supplied, if relevant. The velocity values may
         * be clamped to system minimums or maximums.
         * <p>
         * Calling code may decide to fling or otherwise release the view to let
         * it settle into place. It should do so using
         * {@link  ViewDragHelper.Callback#settleCapturedViewAt(int, int)} or
         * {@link ViewDragHelper.Callback#flingCapturedView(int, int, int, int)}. If the Callback
         * invokes one of these methods, the ViewDragHelper will enter
         * {@link #STATE_SETTLING} and the view capture will not fully end until
         * it comes to a complete stop. If neither of these methods is invoked
         * before <code>onViewReleased</code> returns, the view will stop in
         * place and the ViewDragHelper will return to {@link #STATE_IDLE}.
         * </p>
         *
         * @param releasedChild The captured child view now being released
         * @param xvel          X velocity of the pointer as it left the screen in pixels
         *                      per second.
         * @param yvel          Y velocity of the pointer as it left the screen in pixels
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            final int childHeight = releasedChild.getHeight();

            int left = 0, top = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && mScrollPercent > mScrollThreshold ? childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE : 0;
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && mScrollPercent > mScrollThreshold ? -(childWidth
                        + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE) : 0;
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                top = yvel < 0 || yvel == 0 && mScrollPercent > mScrollThreshold ? -(childHeight
                        + mShadowBottom.getIntrinsicHeight() + OVER_SCROLL_DISTANCE) : 0;
            }

            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        /**
         * Restrict the motion of the dragged child view along the horizontal
         * axis. The default implementation does not allow horizontal motion;
         * the extending class must override this method and provide the desired
         * clamping.
         *
         * @param child Child view being dragged
         * @param left  Attempted motion along the X axis
         * @param dx    Proposed change in position for left
         * @return The new clamped position for left
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            return ret;
        }

        /**
         * Restrict the motion of the dragged child view along the vertical
         * axis. The default implementation does not allow vertical motion; the
         * extending class must override this method and provide the desired
         * clamping.
         *
         * @param child Child view being dragged
         * @param top   Attempted motion along the Y axis
         * @param dy    Proposed change in position for top
         * @return The new clamped position for top
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int ret = 0;
            if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                ret = Math.min(0, Math.max(top, -child.getHeight()));
            }
            return ret;
        }

        /**
         * Called when the drag state changes. See the <code>STATE_*</code>
         * constants for more information.
         *
         * @param state The new drag state
         * @see #STATE_IDLE
         * @see #STATE_DRAGGING
         * @see #STATE_SETTLING
         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (mListeners != null && !mListeners.isEmpty()) {
                for (SwipeListener listener : mListeners) {
                    listener.onScrollStateChange(state, mScrollPercent);
                }
            }
        }
    }
}
