package com.reone.layoutmanagerpkg.swipe;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by wangxingsheng on 2020/7/3.
 * desc:左右滑动进行item额外操作（例如：滑动删除、侧滑收藏等）
 */
public class SwipeLinearLayoutManager extends LinearLayoutManager {

    public SwipeLinearLayoutManager(Context context) {
        super(context);
    }

    public SwipeLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SwipeLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}