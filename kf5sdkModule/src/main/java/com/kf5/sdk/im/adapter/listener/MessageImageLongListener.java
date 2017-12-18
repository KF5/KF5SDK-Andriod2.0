package com.kf5.sdk.im.adapter.listener;

import android.content.Context;
import android.view.View;

import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.system.base.BaseLongClickListener;

/**
 * author:chosen
 * date:2016/10/26 14:52
 * email:812219713@qq.com
 */

public class MessageImageLongListener extends BaseLongClickListener {

    private int position;

    private IMMessage message;

    public MessageImageLongListener(Context context, IMMessage message, int position) {
        super(context);
        this.position = position;
        this.message = message;
    }
    @Override
    public boolean onLongClick(View v) {

        return true;

    }

}
