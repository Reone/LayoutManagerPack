package com.reone.layoutmanagerdemo.utils;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wangxingsheng on 2020/6/9.
 * desc:
 */
public abstract class ItemClickAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    @Nullable
    private OnItemClickListener<E> onItemClickListener = null;

    public void setOnItemClickListener(@Nullable OnItemClickListener<E> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void attachClick(View view, E item, int position) {
        view.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item, position);
            }
        });
    }

    protected void attachClick(View view, E item, int position, Runnable runnable) {
        view.setOnClickListener(v -> {
            runnable.run();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item, position);
            }
        });
    }

    public interface OnItemClickListener<O> {
        void onItemClick(O item, int position);
    }
}
