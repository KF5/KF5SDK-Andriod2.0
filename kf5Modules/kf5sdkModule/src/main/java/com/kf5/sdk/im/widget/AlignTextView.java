package com.kf5.sdk.im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.google.gson.TypeAdapter;
import com.kf5.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chosen
 * @create 2019/1/9 14:27
 * @email 812219713@qq.com
 */
public class AlignTextView extends AppCompatTextView {

    private float textHeight;//单行文字高度
    private float textLineSpaceExtra = 0;//行间距
    private int width;//宽度
    private List<String> lines = new ArrayList<>();//分割后的行数
    private List<Integer> tailLines = new ArrayList<>();//尾行
    private Align align = Align.LEFT;//默认左对齐
    private boolean firstCalc = true;//初始化计算

    private float lineSpacingMultiplier = 1.0f;
    private float lineSpacingAdd = 0f;

    private int originalHeight = 0;//原始高度
    private int originalLineCount = 0;//原始行数
    private int originalPaddingBottom = 0;//原始paddingBottom
    private boolean setPaddingFromMe = false;

    public AlignTextView(Context context) {
        super(context);
        setTextIsSelectable(false);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextIsSelectable(false);

        int[] attributes = new int[]{android.R.attr.lineSpacingExtra, android.R.attr.lineSpacingMultiplier};
        TypedArray typedArray = context.obtainStyledAttributes(attrs, attributes);
        lineSpacingAdd = typedArray.getDimensionPixelSize(0, 0);
        lineSpacingMultiplier = typedArray.getFloat(1, 1.0f);
        typedArray.recycle();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView);
        int align = array.getInt(R.styleable.AlignTextView_align, 0);
        switch (align) {
            case 1:
                this.align = Align.CENTER;
                break;
            case 2:
                this.align = Align.RIGHT;
                break;
            default:
                this.align = Align.LEFT;
                break;
        }
        array.recycle();
    }

    public enum Align {
        LEFT, CENTER, RIGHT//最后一行文字对齐方式
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //首先高度调整
        if (firstCalc) {
            width = getMeasuredWidth();
            String text = getText().toString();
            TextPaint paint = getPaint();
            lines.clear();
            tailLines.clear();

            //文本含有换行符时，单独处理
            String[] items = text.split("\\n");
            for (String item : items) {
                calc(paint, item);
            }
            //使用替代textView计算原始高度与行数
            measureTextViewHeight(text, paint.getTextSize(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
            //获取行高
            textHeight = 1.0f * originalHeight / originalLineCount;
            textLineSpaceExtra = textHeight * (lineSpacingMultiplier - 1) + lineSpacingAdd;
            //计算实际高度，加上多出的行的高度（一般是减少）
            int heightGap = ((int) ((textLineSpaceExtra + textHeight) * (lines.size() - originalLineCount)));
            setPaddingFromMe = true;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), originalPaddingBottom + heightGap);
            firstCalc = false;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        width = getMeasuredWidth();
        Paint.FontMetrics fm = paint.getFontMetrics();
        float firstHeight = getTextSize() - (fm.bottom - fm.descent + fm.ascent - fm.top);
        int gravity = getGravity();
        if ((gravity & 0x1000) == 0) {
            //是否是垂直居中
            firstHeight = firstHeight + (textHeight - firstHeight) / 2;
        }

        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        width = width - paddingLeft - paddingRight;

        for (int i = 0; i < lines.size(); i++) {
            float drawY = i * textHeight + firstHeight;
            String line = lines.get(i);
            //绘画起始x坐标
            float drawSpacingX = paddingLeft;
            float gap = (width - paint.measureText(line));
            float interval = gap / (line.length() - 1);

            //绘制最后一行
            if (tailLines.contains(i)) {
                interval = 0;
                if (align == Align.CENTER) {
                    drawSpacingX += gap / 2;
                } else if (align == Align.RIGHT) {
                    drawSpacingX += gap;
                }
            }

            for (int j = 0; j < line.length(); j++) {
                float drawX = paint.measureText(line.substring(0, j)) + interval * j;
                canvas.drawText(line.substring(j, j + 1), drawX + drawSpacingX, drawY + paddingTop + textLineSpaceExtra * i, paint);
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        firstCalc = true;
        super.setText(text, type);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (!setPaddingFromMe) {
            originalPaddingBottom = bottom;
        }
        setPaddingFromMe = false;
        super.setPadding(left, top, right, bottom);
    }

    public void setAlign(Align align) {
        this.align = align;
        invalidate();
    }

    /**
     * 计算每行显示的文本数
     *
     * @param paint
     * @param text
     */
    private void calc(Paint paint, String text) {
        if (text.length() == 0) {
            lines.add("\n");
            return;
        }
        int startPosition = 0;
        float oneChineseWidth = paint.measureText("测");
        int ignoreCalcLength = ((int) (width / oneChineseWidth));//忽略计算的长度
        StringBuilder sb = new StringBuilder(text.substring(0, Math.min(ignoreCalcLength + 1, text.length())));
        for (int i = ignoreCalcLength + 1; i < text.length(); i++) {
            if (paint.measureText(text.substring(startPosition, i + 1)) > width) {
                startPosition = i;
                //将之前的字符串加入到列表中
                lines.add(sb.toString());
                sb = new StringBuilder();
                //添加开始忽略的字符串，长度不足的话直接结束，否则继续
                if ((text.length() - startPosition) > ignoreCalcLength) {
                    sb.append(text.substring(startPosition, startPosition + ignoreCalcLength));
                } else {
                    lines.add(text.substring(startPosition));
                    break;
                }
                i = i + ignoreCalcLength - 1;
            } else {
                sb.append(text.charAt(i));
            }
        }
        if (sb.length() > 0) {
            lines.add(sb.toString());
        }
        tailLines.add(lines.size() - 1);
    }

    /**
     * 获取文本实际所占高度，辅助计算行高，行数
     *
     * @param text
     * @param textSize
     * @param deviceWidth
     */
    private void measureTextViewHeight(String text, float textSize, int deviceWidth) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        originalLineCount = textView.getLineCount();
        originalHeight = textView.getMeasuredHeight();
    }
}
