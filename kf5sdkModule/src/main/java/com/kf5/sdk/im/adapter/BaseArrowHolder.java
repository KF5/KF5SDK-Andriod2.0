package com.kf5.sdk.im.adapter;

import android.graphics.Color;
import android.view.View;

import com.kf5.sdk.R;
import com.kf5.sdk.system.widget.drawable.TriangleDrawable;

/**
 * @author Chosen
 * @create 2019/1/8 11:02
 * @email 812219713@qq.com
 */
class BaseArrowHolder extends BaseHolder {

    private View arrowView;

    public BaseArrowHolder(MessageAdapter messageAdapter, View convertView) {
        super(messageAdapter, convertView);
        arrowView = convertView.findViewById(R.id.kf5_im_arrow);
    }

    @Override
    protected void setUpUI() {
        super.setUpUI();
        int arrowColor = isReceive ? Color.WHITE : context.getResources().getColor(R.color.kf5_im_list_item_right_mask_color);
        arrowView.setBackgroundDrawable(new TriangleDrawable(isReceive ? TriangleDrawable.LEFT : TriangleDrawable.RIGHT, arrowColor));
    }
}
