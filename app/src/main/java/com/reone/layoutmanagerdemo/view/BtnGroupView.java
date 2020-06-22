package com.reone.layoutmanagerdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.reone.layoutmanagerdemo.R;

/**
 * Created by wangxingsheng on 2020/6/22.
 * desc:
 */
public class BtnGroupView extends LinearLayout {

    private OnStrClickListener onStrClickListener = null;

    public BtnGroupView(Context context) {
        this(context, null);
    }

    public BtnGroupView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BtnGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BtnGroupView);
        parseClickRegister(typedArray);
        typedArray.recycle();
    }

    private void parseClickRegister(TypedArray typedArray) {
        String clickArray = typedArray.getString(R.styleable.BtnGroupView_clickArray);
        if (TextUtils.isEmpty(clickArray)) {
            return;
        }
        String[] clickConstants = clickArray.split(":");
        removeAllViews();
        for (String click : clickConstants) {
            AppCompatButton btn = new AppCompatButton(getContext());
            btn.setText(click);
            btn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btn.setTextColor(getResources().getColor(R.color.colorText));
            btn.setOnClickListener(v -> {
                if (onStrClickListener != null) {
                    onStrClickListener.onClick(v, click);
                }
            });
            addView(btn, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void setOnStrClickListener(OnStrClickListener onStrClickListener) {
        this.onStrClickListener = onStrClickListener;
    }

    public interface OnStrClickListener {
        void onClick(View view, String clickStr);
    }
}