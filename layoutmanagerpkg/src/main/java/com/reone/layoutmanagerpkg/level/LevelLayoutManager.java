package com.reone.layoutmanagerpkg.level;


import android.graphics.PointF;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wangxingsheng on 2020/6/9.
 * desc:层级式LayoutManager
 */
public class LevelLayoutManager extends RecyclerView.LayoutManager {
    private PointF[] pointFS;//itemCount > maxCount时，每一个item应该处于的位置，从上到下的顺序存放至此
    private LayoutParams lp;//构建参数

    public LevelLayoutManager() {
        lp = new LayoutParams();
    }

    private void LOG(String log) {
        if (lp.debug) {
            Log.d("LevelLayoutManager", log);
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        LOG("onLayoutChildren -> " + state);
        if (state.getItemCount() <= 0 || lp.maxCount <= 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        calculateChildes(recycler);
        if (pointFS == null || pointFS.length == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        onLayoutItem(recycler, state);
    }

    /**
     * 布局所有item
     * <p>
     * 整体是对齐RecycleView的底部的
     * 最后一个在最底下、最前面
     */
    private void onLayoutItem(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        detachAndScrapAttachedViews(recycler);
        int needLayoutCount = Math.min(getItemCount(), lp.maxCount);
        int firstIndex = pointFS.length - needLayoutCount;
        int lastIndex = pointFS.length - 1;

        int lastTempIndex = getItemCount() - pointFS.length - 1;
        if (firstIndex == 0 && lastTempIndex >= 0) {
            View item = recycler.getViewForPosition(lastTempIndex);
            layoutItemWithPointF(item, 0);
        }
        for (int i = firstIndex; i <= lastIndex; i++) {
            int itemIndex = getItemCount() - (pointFS.length - i);
            View item = recycler.getViewForPosition(itemIndex);
            addView(item);
            layoutItemWithPointF(item, i);
        }
    }

    private void layoutItemWithPointF(View item, int pointIndex) {
        addView(item);
        measureChildWithMargins(item, 0, 0);
        int height = getDecoratedMeasuredHeight(item);
        int width = getDecoratedMeasuredWidth(item);
        int left = (int) pointFS[pointIndex].x;
        int top = (int) pointFS[pointIndex].y;
        if (lp.showShadow && Build.VERSION.SDK_INT >= 21) {
            item.setElevation(pointIndex * lp.elevation + lp.elevation);
        }
        layoutDecoratedWithMargins(item, left, top, left + width, top + height);
    }

    /**
     * 计算每个item应该处于的位置
     */
    private void calculateChildes(RecyclerView.Recycler recycler) {
        pointFS = new PointF[lp.maxCount];
        //计算item之间的间隔
        int itemHorizontalDistance = 0;
        int itemVerticalDistance = 0;
        View child = recycler.getViewForPosition(getItemCount() - 1);
        measureChildWithMargins(child, 0, 0);
        int lastChildWidth = getDecoratedMeasuredWidth(child);
        int lastChildHeight = getDecoratedMeasuredHeight(child);
        if ((lp.direction & Direction.LEFT) != 0 || (lp.direction & Direction.RIGHT) != 0) {
            itemHorizontalDistance = (getHorizontalSpace() - childStartShadowPadding() - childEndPadding() - lp.paddingLeft - lp.paddingRight - lastChildWidth) / (lp.maxCount - 1);
        }

        if ((lp.direction & Direction.UP) != 0 || (lp.direction & Direction.DOWN) != 0) {
            itemVerticalDistance = (getVerticalSpace() - childStartShadowPadding() - childEndPadding() - lp.paddingTop - lp.paddingBottom - lastChildHeight) / (lp.maxCount - 1);
        }

        for (int i = lp.maxCount - 1; i >= 0; i--) {
            float x = (getWidth() - lastChildWidth) * 1f / 2;
            if ((lp.direction & Direction.LEFT) != 0) {
                int left = getPaddingLeft() + lp.paddingLeft + childEndPadding();
                int dx = (lp.maxCount - i - 1) * itemHorizontalDistance;
                x = left + dx;
            } else if ((lp.direction & Direction.RIGHT) != 0) {
                int right = getWidth() - getPaddingRight() - lp.paddingRight - childEndPadding();
                int dx = (lp.maxCount - i - 1) * itemHorizontalDistance;
                x = right - lastChildWidth - dx;
            }
            float y = (getHeight() - lastChildHeight) * 1f / 2;
            if ((lp.direction & Direction.UP) != 0) {
                int top = getPaddingTop() + lp.paddingTop + childEndPadding();
                int dy = (lp.maxCount - i - 1) * itemVerticalDistance;
                y = top + dy;
            } else if ((lp.direction & Direction.DOWN) != 0) {
                int bottom = getHeight() - getPaddingBottom() - lp.paddingBottom - childEndPadding();
                int dy = (lp.maxCount - i - 1) * itemVerticalDistance;
                y = bottom - lastChildHeight - dy;
            }
            pointFS[i] = new PointF(x, y);
        }
    }

    /**
     * 单层所需要的阴影位置大小
     */
    private int singleShadowPadding() {
        return lp.showShadow ? lp.shadowPadding : 0;
    }

    /**
     * 最后一个/最顶层所需要的阴影位置大小
     */
    private int childEndPadding() {
        return singleShadowPadding() * (lp.maxCount - 1);
    }

    /**
     * 第一个/最下层所需要的阴影位置大小
     */
    private int childStartShadowPadding() {
        return singleShadowPadding();
    }

    /**
     * 获取RecyclerView的显示高度
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * 获取RecyclerView的显示宽度
     */
    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public void setPaddingLeft(int paddingLeft) {
        lp.paddingLeft = paddingLeft;
        requestLayout();
    }

    public void setPaddingRight(int paddingRight) {
        lp.paddingRight = paddingRight;
        requestLayout();
    }

    public void setPaddingTop(int paddingTop) {
        lp.paddingTop = paddingTop;
        requestLayout();
    }

    public void setPaddingBottom(int paddingBottom) {
        lp.paddingBottom = paddingBottom;
        requestLayout();
    }

    public void setPadding(int left, int top, int right, int bottom) {
        lp.paddingLeft = left;
        lp.paddingRight = right;
        lp.paddingTop = top;
        lp.paddingBottom = bottom;
        requestLayout();
    }

    public void setShowShadow(boolean showShadow) {
        lp.showShadow = showShadow;
        requestLayout();
    }

    public void setMaxCount(int maxCount) {
        lp.maxCount = maxCount;
        requestLayout();
    }

    public void setDirection(@Direction int direction) {
        lp.direction = direction;
        requestLayout();
    }

    /**
     * 通过设置{@linkplain View#setElevation(float)}来实现层级效果，此属性需要api 21
     */
    @RequiresApi(21)
    public void setElevation(int elevation) {
        lp.elevation = elevation;
        lp.shadowPadding = elevation * 2;
        requestLayout();
    }

    public void setDebug(boolean debug) {
        lp.debug = debug;
    }

    @IntDef({
            Direction.RIGHT,
            Direction.UP_RIGHT,
            Direction.UP,
            Direction.UP_LEFT,
            Direction.LEFT,
            Direction.DOWN_LEFT,
            Direction.DOWN,
            Direction.DOWN_RIGHT,
    })
    public @interface Direction {
        int UP = 1;
        int DOWN = 1 << 1;
        int LEFT = 1 << 2;
        int RIGHT = 1 << 3;

        int UP_RIGHT = UP | RIGHT;
        int UP_LEFT = UP | LEFT;
        int DOWN_LEFT = DOWN | LEFT;
        int DOWN_RIGHT = DOWN | RIGHT;
    }

    public static class LayoutParams {
        static final int DEFAULT_MAX_COUNT = 3;
        static final int DEFAULT_ELEVATION = 3;
        static final int DEFAULT_SHADOW_PADDING = DEFAULT_ELEVATION * 2;
        static final int DEFAULT_ANGLE = Direction.DOWN_LEFT;
        //padding
        int paddingLeft = 0;
        int paddingRight = 0;
        int paddingTop = 0;
        int paddingBottom = 0;
        boolean showShadow = true;//是否需要设置高度（会预留阴影距离）
        int maxCount = DEFAULT_MAX_COUNT;//最大可显示条数
        int elevation = DEFAULT_ELEVATION;//高度间隔，每个item之间相差高度
        int shadowPadding = DEFAULT_SHADOW_PADDING;//给高度所带来的的阴影保留显示空间
        @Direction
        int direction = DEFAULT_ANGLE;//角度，布局方向，起始item在最定层
        boolean debug;
    }
}
