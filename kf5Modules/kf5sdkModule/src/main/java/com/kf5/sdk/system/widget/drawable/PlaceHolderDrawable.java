package com.kf5.sdk.system.widget.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kf5.sdk.R;
import com.kf5.sdk.system.utils.Utils;

/**
 * @author Chosen
 * @create 2019/1/8 11:46
 * @email 812219713@qq.com
 */
public class PlaceHolderDrawable extends Drawable {

    //占位图边界宽高
    private final int originWidth, originHeight;

    private Paint paint;
    private Paint pathPaint;

    private int bitmapWidth, bitmapHeight;
    private final int arrowWidth;
    private final boolean isLeft;
    private final int arrowMargin;
    private final boolean isVideo;

    public PlaceHolderDrawable(Context context, int originWidth, int originHeight, boolean isLeft, boolean isVideo) {
        this.originWidth = originWidth;
        this.originHeight = originHeight;
        this.isLeft = isLeft;
        this.isVideo = isVideo;
        arrowWidth = Utils.dip2px(context, 6);
        arrowMargin = Utils.dip2px(context, 2);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#CCCCCC"));
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kf5_empty_photo);
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
        pathPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(0, 0, originWidth, originHeight, paint);
        if (!isVideo) {
            canvas.save();
            if (isLeft) {
                canvas.translate((originWidth - bitmapWidth + arrowWidth + arrowMargin) / 2, (originHeight - bitmapHeight) / 2);
            } else {
                canvas.translate((originWidth - bitmapWidth - arrowWidth) / 2, (originHeight - bitmapHeight) / 2);
            }
            canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, pathPaint);
            canvas.restore();
        }
    }


    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
