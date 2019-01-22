package com.kf5.sdk.im.expression.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kf5.sdk.R;
import com.kf5.sdk.im.expression.utils.Constants;
import com.kf5.sdk.im.keyboard.adapter.EmoticonsAdapter;
import com.kf5.sdk.im.keyboard.api.EmoticonClickListener;
import com.kf5.sdk.im.keyboard.data.EmoticonEntity;
import com.kf5.sdk.im.keyboard.data.EmoticonPageEntity;

/**
 * author:chosen
 * date:2016/11/10 14:27
 * email:812219713@qq.com
 */

public class TextEmoticonsAdapter extends EmoticonsAdapter<EmoticonEntity> {

    public TextEmoticonsAdapter(Context context, EmoticonPageEntity emoticonPageEntity, EmoticonClickListener onEmoticonClickListener) {
        super(context, emoticonPageEntity, onEmoticonClickListener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.kf5_item_emoticon_text, null);
            viewHolder.rootView = convertView;
            viewHolder.ly_root = (LinearLayout) convertView.findViewById(R.id.kf5_ly_root);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.kf5_tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final boolean isDelBtn = isDelBtn(position);
        final EmoticonEntity emoticonEntity = mData.get(position);
        if (isDelBtn) {
            viewHolder.ly_root.setBackgroundResource(R.drawable.kf5_emotion_bg);
        } else {
            viewHolder.tv_content.setVisibility(View.VISIBLE);
            if (emoticonEntity != null) {
                viewHolder.tv_content.setText(emoticonEntity.getContent());
                viewHolder.ly_root.setBackgroundResource(R.drawable.kf5_emotion_bg);
            }
        }
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnEmoticonClickListener != null) {
                    mOnEmoticonClickListener.onEmoticonClick(emoticonEntity, Constants.EMOTICON_CLICK_TEXT, isDelBtn);
                }
            }
        });

        updateUI(viewHolder, parent);
        return convertView;
    }

    protected void updateUI(ViewHolder viewHolder, ViewGroup parent) {
        if (mDefaultItemHeight != mItemHeight) {
            viewHolder.tv_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mItemHeight));
        }
        mItemHeightMax = this.mItemHeightMax != 0 ? this.mItemHeightMax : (int) (mItemHeight * mItemHeightMaxRatio);
        mItemHeightMin = this.mItemHeightMin != 0 ? this.mItemHeightMin : mItemHeight;
        int realItemHeight = ((View) parent.getParent()).getMeasuredHeight() / mEmoticonPageEntity.getLine();
        realItemHeight = Math.min(realItemHeight, mItemHeightMax);
        realItemHeight = Math.max(realItemHeight, mItemHeightMin);
        viewHolder.ly_root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, realItemHeight));
    }

    public static class ViewHolder {
        public View rootView;
        public LinearLayout ly_root;
        public TextView tv_content;
    }
}