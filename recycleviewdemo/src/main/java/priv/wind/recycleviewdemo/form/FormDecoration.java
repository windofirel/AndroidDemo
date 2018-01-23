package priv.wind.recycleviewdemo.form;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

import priv.wind.recycleviewdemo.R;


/**
 * @author Dongbaicheng
 * @version 2017/12/1
 */

public class FormDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "wind_demo";

    private float mDividerHeight;
    private Context mContext;
    private List<FormAttrBean> mFormAttrBeans;

    public FormDecoration(Context context, List<FormAttrBean> formAttrBeans) {
        mContext = context;
        mFormAttrBeans = formAttrBeans;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawDivider(c, parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHeader(c);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //        if (parent.getChildAdapterPosition(view) == 0) {
        //            outRect.set(0, 30, 0, (int) mDividerHeight);
        //            return;
        //        }
        outRect.set(0, 0, 0, (int) mDividerHeight);
    }

    //region 绘制序列号 未实现
    // TODO: 2018/1/18  因为未实现随左右滚动保持在屏幕左侧，所以弃用
    private void drawSequence(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = 0;
        int right = 55;
        int a = parent.getScrollState();
        Log.i(TAG, "drawSequence: a = " + a);
        int textSize = 25;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int top = child.getTop();
            int bottom = child.getBottom();
            drawHeaderBackground(c, R.color.primary_dark, R.color.primary, left, right, child.getTop(), bottom);
            drawSequenceText(c, right / 2, top + textSize, String.valueOf(i + 1), textSize, R.color.primary_text);
        }
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
    public void drawSequenceText(Canvas c, int x, int y, String text, int textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(mContext.getResources().getColor(textColor));
        c.drawText(text, x, y, paint);
    }
    //endregion

    //region 绘制表头

    /**
     * 绘制表头
     *
     * @param c 画布
     */
    private void drawHeader(Canvas c) {
        //参数
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 30;
        int textSize = 25;
        int baseY = textSize;
        int baseX = left;

        //根据表头列名和宽度数组绘制表头
        for (int i = 0; i < mFormAttrBeans.size(); i++) {
            int width = mFormAttrBeans.get(i).columnWidth;
            String text = mFormAttrBeans.get(i).headerName;
            if (i == 0) {
                right += width;
                drawHeaderBackground(c, R.color.primary_dark, R.color.primary, left, right, top, bottom);
                drawHeaderText(c, baseX + width / 2, baseY, text, textSize, R.color.primary_text);
            } else {
                right += width;
                left += mFormAttrBeans.get(i - 1).columnWidth;
                baseX = left;
                drawHeaderBackground(c, R.color.primary_dark, R.color.primary, left, right, top, bottom);
                drawHeaderText(c, baseX + width / 2, baseY, text, textSize, R.color.primary_text);
            }
        }
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
        paint.setColor(mContext.getResources().getColor(textColor));
        c.drawText(text, x, y, paint);
    }

    /**
     * 画表头背景
     *
     * @param c           画布
     * @param strokeColor 背景框颜色
     * @param fillColor   背景颜色
     * @param left        左坐标
     * @param right       右坐标
     * @param top         顶坐标
     * @param bottom      底坐标
     */
    public void drawHeaderBackground(Canvas c, int strokeColor, int fillColor, int left, int right, int top, int bottom) {
        //画背景颜色
        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(mContext.getResources().getColor(fillColor));
        c.drawRect(left, top, right, bottom, fillPaint);

        //画背景框
        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(mContext.getResources().getColor(strokeColor));
        strokePaint.setStyle(Paint.Style.STROKE);
        c.drawRect(left, top, right, bottom, strokePaint);
    }
    //endregion

    //region 绘制表单项背景和分割线

    /**
     * 画divider (orientation为vertical)
     *
     * @param c
     * @param parent
     */
    private void drawDivider(Canvas c, RecyclerView parent) {
        mDividerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, mContext.getResources().getDisplayMetrics());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mContext.getResources().getColor(R.color.primary_dark));
        // recyclerView是否设置了paddingLeft和paddingRight
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            // divider的top 应该是 item的bottom 加上 marginBottom 再加上 Y方向上的位移
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(child.getTranslationY());
            // divider的bottom就是top加上divider的高度了
            final int bottom = (int) (top + mDividerHeight);
            c.drawRect(left, top, right, bottom, paint);
            drawItem(c, child.getTop(), child.getBottom());
        }
    }

    /**
     * 绘制表单项
     *
     * @param c      画布
     * @param top    顶部坐标
     * @param bottom 底部坐标
     */
    private void drawItem(Canvas c, int top, int bottom) {
        int right = 0;
        int left = 0;
        for (int i = 0; i < mFormAttrBeans.size(); i++) {
            int itemWidth = mFormAttrBeans.get(i).columnWidth;
            if (i == 0) {
                right += itemWidth;
                drawItemBackground(c, R.color.primary_dark, left, right, top, bottom);
            } else {
                right += itemWidth;
                left += mFormAttrBeans.get(i - 1).columnWidth;
                drawItemBackground(c, R.color.primary_dark, left, right, top, bottom);
            }
        }
        drawItemBackground(c, R.color.primary_dark, left, right, top, bottom);
    }

    /**
     * 画表单项背景
     *
     * @param c           画布
     * @param strokeColor 背景框颜色
     * @param left        左坐标
     * @param right       右坐标
     * @param top         顶坐标
     * @param bottom      底坐标
     */
    public void drawItemBackground(Canvas c, int strokeColor, int left, int right, int top, int bottom) {
        //画背景框
        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(mContext.getResources().getColor(strokeColor));
        strokePaint.setStyle(Paint.Style.STROKE);
        c.drawRect(left, top, right, bottom, strokePaint);
    }
    //endregion
}
