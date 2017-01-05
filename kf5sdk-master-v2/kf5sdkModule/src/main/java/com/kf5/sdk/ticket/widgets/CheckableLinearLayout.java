package com.kf5.sdk.ticket.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

/**
 * author:chosen
 * date:2017/1/4 14:05
 * email:812219713@qq.com
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private Set<Checkable> mCheckableSet = new HashSet<>();

    private boolean mChecked;

    public CheckableLinearLayout(Context context) {
        super(context);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v instanceof Checkable) {
                mCheckableSet.add((Checkable) v);
            }
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked == mChecked) {
            return;
        }
        mChecked = checked;
        for (Checkable checkable : mCheckableSet) {
            checkable.setChecked(checked);
        }
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {

        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
