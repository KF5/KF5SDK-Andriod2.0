package com.chosen.cameraview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.chosen.cameraview.utils.ScreenUtils;

/**
 * @author Chosen
 * @create 2018/12/3 15:14
 * @email 812219713@qq.com
 */
public class ReturnButton extends View {

    private int size;

    //    private int center_X;
//    private int center_Y;
    private float strokeWidth;

    private Paint paint;
    Path path;
    final int padding;

    public ReturnButton(Context context) {
        this(context, 16);
    }

    public ReturnButton(Context context, int size) {
        super(context);
        this.size = size;
//        center_X = size / 2;
//        center_Y = size / 2;

        strokeWidth = size / 15f;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);

        path = new Path();
        padding = ScreenUtils.dp2px(getContext(), 8);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(size, size / 2);
        setMeasuredDimension(size + padding * 2, size / 2 + padding * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        path.moveTo(strokeWidth, strokeWidth/2);
//        path.lineTo(center_X, center_Y - strokeWidth/2);
//        path.lineTo(size - strokeWidth, strokeWidth/2);
//        canvas.drawPath(path, paint);
        path.reset();
        path.moveTo(padding, padding);
        path.lineTo(getWidth() / 2, ((float) (getHeight() - padding * 1.5)));
        path.lineTo(getWidth() - padding, padding);
        canvas.drawPath(path, paint);
    }
}
