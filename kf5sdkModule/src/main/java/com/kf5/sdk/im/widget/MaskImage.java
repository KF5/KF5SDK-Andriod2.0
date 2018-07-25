package com.kf5.sdk.im.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kf5.sdk.R;

/**
 * author:chosen
 * date:2016/10/17 16:13
 * email:812219713@qq.com
 */

public class MaskImage extends ImageView {

    int mMaskSource = 0;  // 遮罩图片id
    Drawable mask;

    public MaskImage(Context context) {
        this(context, null);
    }

    public MaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KF5MaskImage);
        int source = typedArray.getInt(R.styleable.KF5MaskImage_kf5_mi_maskSource, -1);
        typedArray.recycle();
        if (source == 0) {
            mMaskSource = R.drawable.kf5_message_from_text_bg_nomal;
        } else if (source == 1) {
            mMaskSource = R.drawable.kf5_message_to_text_bg_normal;
        }
        mask = getResources().getDrawable(mMaskSource);
    }

    private static final Paint paintMask = createMaskPaint();


    private static final Paint createMaskPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        return paint;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mask != null) {
            // bounds
            int width = getWidth();
            int height = getHeight();
            // create blend layer
            canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
            //
            // mask
            //
            if (mask != null) {
                mask.setBounds(0, 0, width, height);
                mask.draw(canvas);
            }
            //
            // source
            //
            {
                canvas.saveLayer(0, 0, width, height, paintMask, Canvas.ALL_SAVE_FLAG);
                super.onDraw(canvas);
                canvas.restore();
            }
            // apply blend layer
            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                setAlpha(0.8f);

                break;
            case MotionEvent.ACTION_UP:

                setAlpha(1f);

                break;
            case MotionEvent.ACTION_CANCEL:

                setAlpha(1f);

                break;
            case MotionEvent.ACTION_MOVE:

                setAlpha(0.8f);

                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }


}
