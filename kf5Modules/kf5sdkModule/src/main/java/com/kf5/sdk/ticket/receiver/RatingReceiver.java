package com.kf5.sdk.ticket.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.kf5.sdk.system.entity.Field;

/**
 * author:chosen
 * date:2017/1/5 15:20
 * email:812219713@qq.com
 */

public class RatingReceiver extends BroadcastReceiver {

    public static final String RATING_FILTER = "com.kf5sdk.ticket.RATING_SUCCESS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(), RATING_FILTER)) {
            if (mRatingListener != null) {
                int rating = intent.getIntExtra(Field.RATING, 0);
                String content = intent.getStringExtra(Field.RATING_CONTENT);
                mRatingListener.ratingSuccess(rating, content);
            }
        }
    }

    private RatingListener mRatingListener;

    public void setRatingListener(RatingListener ratingListener) {
        mRatingListener = ratingListener;
    }

    public interface RatingListener {

        void ratingSuccess(int rating, String content);

    }
}
