package priv.wind.recycleviewdemo.extend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Dongbaicheng
 * @version 2018/1/23
 */

public class RecyclerExtend extends RecyclerView {
    public RecyclerExtend(Context context) {
        super(context);
    }

    public RecyclerExtend(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerExtend(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        //        drawHeaderText(c, 50,50, "测试表头",25, R.color.primary);
    }

    /**
     * 画表头文字
     *
     * @param c         画布
     * @param x         X轴坐标
     * @param y         Y轴坐标
     * @param text      文字
     * @param textSize  字体大小
     * @param textColor 字体颜色
     */
    public void drawHeaderText(Canvas c, int x, int y, String text, int textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(getContext().getResources().getColor(textColor));
        c.drawText(text, x, y, paint);
    }
}
