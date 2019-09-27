package com.kf5.sdk.im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kf5.sdk.R;
import com.kf5.sdk.system.utils.Utils;
import com.kf5.sdk.system.widget.drawable.TriangleDrawable;

/**
 * author:chosen
 * date:2016/10/17 16:13
 * email:812219713@qq.com
 */

public class MaskImage extends AppCompatImageView {

    Drawable mask;//遮罩图片
    final int boundOffsetHeight;//锚点y轴偏移量
    final int arrowWidth;//锚点宽度
    final int arrowHeight;//锚点高度

    //arrowMargin,maskMargin后期新增，遮罩图片bottom未被遮住形成圆角，故加maskMarin，部分图片的锚点被挡住一部分，故加arrowMargin。。。
    final int arrowMargin;
    final int maskMargin;


    private Orientation orientation;
    private TriangleDrawable triangleDrawable;//锚点遮罩
    private Path trianglePath;

    private final Paint paintMask;//画笔


    private Paint circleStrokePaint;
    private Paint trianglePaint;
    private Paint circleSolidPaint;
    int circleStrokeRadius;
    int circleSolidRadius;
    int triangleLength;

    private boolean showMaskView;

    private enum Orientation {
        LEFT, RIGHT
    }

    public MaskImage(Context context) {
        this(context, null);
    }

    public MaskImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }


    public MaskImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        boundOffsetHeight = Utils.dip2px(getContext(), 16);
        arrowWidth = Utils.dip2px(getContext(), 6);
        arrowHeight = Utils.dip2px(getContext(), 8);
        arrowMargin = Utils.dip2px(context, 2);
        maskMargin = Utils.dip2px(context, 4);
        paintMask = new Paint();
        paintMask.setAntiAlias(true);
        paintMask.setFilterBitmap(true);
        paintMask.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        init(context, attrs);

//        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        maskPaint.setStyle(Paint.Style.FILL);
//        maskPaint.setColor(Color.WHITE);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KF5MaskImage, 0, 0);
        int gravity = a.getInt(R.styleable.KF5MaskImage_kf5_mi_maskSource, 0);
        int maskType = a.getInt(R.styleable.KF5MaskImage_kf5_mi_maskType, -1);
        a.recycle();
        if (maskType == 0) {
            showMaskView = true;
            initMaskViewAttr(context);
        }
        boolean right = gravity == 1;
        orientation = right ? Orientation.RIGHT : Orientation.LEFT;
        triangleDrawable = right ? new TriangleDrawable(TriangleDrawable.RIGHT, Color.WHITE) : new TriangleDrawable(TriangleDrawable.LEFT, Color.WHITE);
        mask = getResources().getDrawable(R.drawable.kf5_im_list_item_bg_right);
    }

    private void initMaskViewAttr(Context context) {
        circleStrokePaint = new Paint();
        circleStrokePaint.setAntiAlias(true);
        circleStrokePaint.setStyle(Paint.Style.STROKE);
        circleStrokePaint.setStrokeWidth(Utils.dip2px(context, 2));
        circleStrokePaint.setColor(Color.WHITE);
        circleStrokeRadius = Utils.dip2px(getContext(), 24);


        triangleLength = Utils.dip2px(context, 32);
        trianglePath = new Path();
        trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setColor(Color.WHITE);


        circleSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleSolidPaint.setStyle(Paint.Style.FILL);
        circleSolidPaint.setColor(Color.parseColor("#33e6e6e6"));
        circleSolidRadius = Utils.dip2px(context, 24);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // bounds
        int width = getWidth();
        int height = getHeight();
        // create blend layer
        canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);

        if (orientation == Orientation.RIGHT) {
            //绘制矩形遮罩
            mask.setBounds(maskMargin, 0, width - (arrowWidth + arrowMargin), height - maskMargin);
            mask.draw(canvas);

            //绘制锚点
            canvas.save();
            canvas.translate(width - (arrowWidth + arrowMargin), boundOffsetHeight);
            triangleDrawable.setBounds(0, 0, arrowWidth, arrowHeight);
            triangleDrawable.draw(canvas);
            canvas.restore();
        } else {

            //LTR,先绘制锚点
            canvas.save();
            canvas.translate(arrowMargin, boundOffsetHeight);
            triangleDrawable.setBounds(0, 0, arrowWidth, arrowHeight);
            triangleDrawable.draw(canvas);
            canvas.restore();

            //再绘制矩形遮罩
            canvas.save();
            canvas.translate(arrowWidth + arrowMargin, 0);
//            rectF.top = 0;
//            rectF.left = 0;
//            rectF.right = width - (arrowWidth + arrowMargin);
//            rectF.bottom = height;
//            canvas.drawRoundRect(rectF, maskMargin, maskMargin, maskPaint);
            mask.setBounds(0, 0, width - (arrowWidth + arrowMargin), height - maskMargin);
            mask.draw(canvas);
            canvas.restore();
        }

        //
        // source
        //
        canvas.saveLayer(0, 0, width, height, paintMask, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        canvas.restore();

        if (showMaskView) {
            canvas.save();
            int centerX;
            if (orientation == Orientation.LEFT) {
                canvas.translate(arrowWidth+arrowMargin, 0);
                centerX = (width - arrowWidth ) / 2;
            } else {
                centerX = (width - arrowWidth) / 2;
            }
            int centerY = height / 2;
            canvas.drawCircle(centerX, centerY, circleStrokeRadius, circleStrokePaint);
            canvas.drawCircle(centerX, centerY, circleSolidRadius, circleSolidPaint);
            //开始计算三角形的坐标
            int triangleViewHeight = ((int) (triangleLength * Math.sin(Math.toRadians(60))));
            trianglePath.reset();
            //顺时针划线
            trianglePath.moveTo(centerX - triangleViewHeight / 3, centerY - triangleLength / 2);
            trianglePath.lineTo(centerX + triangleViewHeight / 3 * 2, centerY);
            trianglePath.lineTo(centerX - triangleViewHeight / 3, centerY + triangleLength / 2);
            trianglePath.close();
            canvas.drawPath(trianglePath, trianglePaint);
            canvas.restore();
        }

    }


    @Override
    public boolean isOpaque() {
        return false;
    }


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
