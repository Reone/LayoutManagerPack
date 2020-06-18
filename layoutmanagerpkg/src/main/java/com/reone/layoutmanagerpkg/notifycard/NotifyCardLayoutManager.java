package com.reone.layoutmanagerpkg.notifycard;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

/**
 * Created by wangxingsheng on 2020/6/9.
 * desc:卡片通知
 */
public class NotifyCardLayoutManager extends RecyclerView.LayoutManager {
    public static final int DEFAULT_MAX_COUNT = 3;
    private int offsetXofLast = 0;//最有一项、最下方item是可以左右滑动删除的。
    private int maxCount;//最大可显示条数
    private PointF[] pointFS;//itemCount > maxCount时，每一个item应该处于的位置，从上到下的顺序存放至此
    private ValueAnimator lastItemMoveAnimator;
    private RecyclerView recyclerView;
    @Nullable
    private OnItemRemoveListener onItemRemoveListener = null;

    public NotifyCardLayoutManager() {
        this(DEFAULT_MAX_COUNT);
    }

    public NotifyCardLayoutManager(int maxCount) {
        this.maxCount = maxCount;
    }

    private void LOG(String log) {
        if (isDebug()) {
            Log.d("NotifyCardLayoutManager", log);
        }
    }

    protected boolean isDebug() {
        return false;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        findRecyclerView();
    }

    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        if (lastItemMoveAnimator != null) {
            lastItemMoveAnimator.cancel();
            lastItemMoveAnimator = null;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        LOG("onLayoutChildren -> " + state);
        if (state.getItemCount() <= 0 || maxCount <= 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        calculateChildes(recycler);
        if (pointFS == null || pointFS.length == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (lastItemMoveAnimator == null || !lastItemMoveAnimator.isRunning()) {
            offsetXofLast = 0;
        }
        onLayoutItem(recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        LOG("scrollHorizontallyBy -> " + state.getRemainingScrollHorizontal() + ", offsetXofLast:" + offsetXofLast);
        if (lastItemMoveAnimator == null || !lastItemMoveAnimator.isRunning()) {
            offsetXofLast += dx;
            onLayoutItem(recycler, state);
            if (Math.abs(offsetXofLast) > getHorizontalSpace()) {//在fling过程中，如果item已经
                if (recyclerView != null) {
                    recyclerView.post(() -> {
                        recyclerView.stopScroll();//这个方法在RecycleView计算布局的过程中不能调用
                        if (onItemRemoveListener != null && getItemCount() > 0) {
                            onItemRemoveListener.onItemRemove(getItemCount() - 1);
                            requestLayout();
                        }
                    });
                }
            }
        }
        return dx;
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_DRAGGING://action_move
                LOG("onScrollStateChanged SCROLL_STATE_DRAGGING");
                break;
            case RecyclerView.SCROLL_STATE_IDLE://action_up
                LOG("onScrollStateChanged SCROLL_STATE_IDLE");
                if (Math.abs(offsetXofLast) < getHorizontalSpace() && (lastItemMoveAnimator == null || !lastItemMoveAnimator.isRunning())) {
                    initLastItemMoveAnimator();
                    lastItemMoveAnimator.start();
                }
                break;
            case RecyclerView.SCROLL_STATE_SETTLING://fling
                LOG("onScrollStateChanged SCROLL_STATE_SETTLING");
                break;
        }
    }

    /**
     * 通过反射获取RecycleView，以便执行后续操作
     */
    private void findRecyclerView() {
        try {
            Field field = RecyclerView.LayoutManager.class.getDeclaredField("mRecyclerView");
            field.setAccessible(true);
            recyclerView = (RecyclerView) field.get(this);
            LOG("findRecyclerView " + recyclerView);
            recyclerView.setItemAnimator(new NotifyDefaultItemAnimator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据偏移量初始化需要执行的动画
     */
    private void initLastItemMoveAnimator() {
        if (lastItemMoveAnimator != null) {
            lastItemMoveAnimator.cancel();
        }
        final int start = offsetXofLast;
        final int lastSpaceWidth = getHorizontalSpace();
        if (Math.abs(start) < lastSpaceWidth / 2) {
            lastItemMoveAnimator = ValueAnimator.ofInt(start, 0);
            lastItemMoveAnimator.setInterpolator(new DecelerateInterpolator());
        } else {
            if (start > 0) {
                lastItemMoveAnimator = ValueAnimator.ofInt(start, lastSpaceWidth);
            } else {
                lastItemMoveAnimator = ValueAnimator.ofInt(start, start - lastSpaceWidth);
            }
            lastItemMoveAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            lastItemMoveAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (onItemRemoveListener != null && getItemCount() > 0) {
                        onItemRemoveListener.onItemRemove(getItemCount() - 1);
                    }
                }
            });
        }
        lastItemMoveAnimator.setDuration(300);
        lastItemMoveAnimator.addUpdateListener(animation -> {
            offsetXofLast = (int) animation.getAnimatedValue();
            LOG("lastItemMoveAnimator offsetXofLast: " + offsetXofLast);
            requestLayout();
        });
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
        int needLayoutCount = Math.min(getItemCount(), maxCount);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            item.setElevation(pointIndex * 5);
        }
        if (pointIndex == pointFS.length - 1) {
            left = left - offsetXofLast;
        }
        layoutDecoratedWithMargins(item, left, top, left + width, top + height);
    }

    /**
     * 计算每个item应该处于的位置
     */
    private void calculateChildes(RecyclerView.Recycler recycler) {
        pointFS = new PointF[maxCount];
        View child = recycler.getViewForPosition(getItemCount() - 1);
        measureChildWithMargins(child, 0, 0);
        int lastChildHeight = getDecoratedMeasuredHeight(child);
        int lastChildWidth = getDecoratedMeasuredWidth(child);
        int itemVerticalDistance = (getVerticalSpace() - lastChildHeight) / (maxCount - 1);
        int itemHorizontalDistance = (getHorizontalSpace() - lastChildWidth) / (maxCount - 1);
        for (int i = maxCount - 1; i >= 0; i--) {
            int dx = (maxCount - i - 1) * itemHorizontalDistance;
            int dy = (maxCount - i - 1) * itemVerticalDistance;
            pointFS[i] = new PointF(getPaddingLeft() + dx, getHeight() - getPaddingBottom() - lastChildHeight - dy);
        }
    }

    /**
     * 可以横移，因为底部是可以横滑删除的
     */
    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    /**
     * 获取RecyclerView的显示高度
     */
    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    /**
     * 获取RecyclerView的显示宽度
     */
    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public void setOnItemRemoveListener(@NonNull OnItemRemoveListener onItemRemoveListener) {
        this.onItemRemoveListener = onItemRemoveListener;
    }

    public interface OnItemRemoveListener {
        void onItemRemove(int position);
    }

    private static class NotifyDefaultItemAnimator extends DefaultItemAnimator {
        @Override
        public long getRemoveDuration() {
            //不需要remove的效果，但是要不影响其他动画的进行
            return 0;
        }
    }
}
