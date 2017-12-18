package com.kf5.sdk.im.expression.adapter;

import android.content.Context;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.im.expression.utils.Constants;
import com.kf5.sdk.im.keyboard.api.EmoticonClickListener;
import com.kf5.sdk.im.keyboard.data.EmoticonEntity;
import com.kf5.sdk.im.keyboard.data.EmoticonPageEntity;
import com.kf5.sdk.system.utils.ImageLoaderManager;

/**
 * author:chosen
 * date:2016/11/10 14:24
 * email:812219713@qq.com
 */

public class BigEmoticonsAndTitleAdapter extends BigEmoticonsAdapter {

    protected final double DEF_HEIGHTMAXTATIO = 1.6;

    public BigEmoticonsAndTitleAdapter(Context context, EmoticonPageEntity emoticonPageEntity, EmoticonClickListener onEmoticonClickListener) {
        super(context, emoticonPageEntity, onEmoticonClickListener);
        this.mItemHeight = (int) context.getResources().getDimension(R.dimen.lf5_emoticon_size_big);
        this.mItemHeightMaxRatio = DEF_HEIGHTMAXTATIO;
    }

    protected void bindView(int position, ViewHolder viewHolder) {
        final boolean isDelBtn = isDelBtn(position);
        final EmoticonEntity emoticonEntity = mData.get(position);
        if (isDelBtn) {
            viewHolder.iv_emoticon.setImageResource(R.drawable.kf5_emoji_delete);
            viewHolder.iv_emoticon.setBackgroundResource(R.drawable.kf5_emotion_bg);
        } else {
            if (emoticonEntity != null) {
                try {
                    ImageLoaderManager.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    viewHolder.tv_content.setVisibility(View.VISIBLE);
                    viewHolder.tv_content.setText(emoticonEntity.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewHolder.iv_emoticon.setBackgroundResource(R.drawable.kf5_emotion_bg);
            }
        }

        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmoticonClickListener != null) {
                    mOnEmoticonClickListener.onEmoticonClick(emoticonEntity, Constants.EMOTICON_CLICK_BIG_IMAGE, isDelBtn);
                }
            }
        });
    }
}