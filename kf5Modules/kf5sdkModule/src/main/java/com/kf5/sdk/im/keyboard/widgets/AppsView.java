package com.kf5.sdk.im.keyboard.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/11/2 16:39
 * email:812219713@qq.com
 */

public class AppsView extends RelativeLayout {

    private View mView;

    private TextView mTextViewCamera, mTextViewAlbum;

    public TextView getTextViewCamera() {
        return mTextViewCamera;
    }

    public TextView getTextViewAlbum() {
        return mTextViewAlbum;
    }

    public AppsView(Context context) {
        this(context, null);
    }

    public AppsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.kf5_layout_chat_by_other, this);
        mTextViewCamera = (TextView) mView.findViewById(R.id.kf5_textview_choice_from_camera);
        mTextViewAlbum = (TextView) mView.findViewById(R.id.kf5_textview_choice_from_image);
    }

}
