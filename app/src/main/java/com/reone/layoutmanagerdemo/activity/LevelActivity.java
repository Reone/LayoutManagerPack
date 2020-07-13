package com.reone.layoutmanagerdemo.activity;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.reone.layoutmanagerdemo.R;
import com.reone.layoutmanagerdemo.adapter.NotifyCardAdapter2;
import com.reone.layoutmanagerdemo.bean.ItemBean;
import com.reone.layoutmanagerdemo.utils.FakerData;
import com.reone.layoutmanagerdemo.utils.ToastUtils;
import com.reone.layoutmanagerdemo.view.BtnGroupView;
import com.reone.layoutmanagerpkg.level.LevelLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangxingsheng on 2020/6/19.
 * desc:
 */
public class LevelActivity extends AppCompatActivity {
    @BindView(R.id.rv_level)
    RecyclerView rvLevel;
    @BindView(R.id.btn_group_view)
    BtnGroupView btnGroupView;
    private LevelLayoutManager levelLayoutManager;
    private NotifyCardAdapter2 adapter;
    @Nullable
    private List<ItemBean> data;

    private int[] direction = {
            LevelLayoutManager.Direction.UP,
            LevelLayoutManager.Direction.UP_RIGHT,
            LevelLayoutManager.Direction.RIGHT,
            LevelLayoutManager.Direction.DOWN_RIGHT,
            LevelLayoutManager.Direction.DOWN,
            LevelLayoutManager.Direction.DOWN_LEFT,
            LevelLayoutManager.Direction.LEFT,
            LevelLayoutManager.Direction.UP_LEFT,
    };
    private int directionIndex = direction.length - 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        ButterKnife.bind(this);
        handleClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleData();
    }

    private void handleData() {
        if (data == null) {
            data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                data.add(FakerData.createItemBean());
            }
            initManager();
            adapter = new NotifyCardAdapter2(this, data);
            adapter.setOnItemClickListener((item, position) -> ToastUtils.showShort("notify 2 click " + item.getName()));
            rvLevel.setLayoutManager(levelLayoutManager);
            rvLevel.setAdapter(adapter);
        }
    }

    private void initManager() {
        levelLayoutManager = new LevelLayoutManager();
        levelLayoutManager.setMaxCount(9);
        if (Build.VERSION.SDK_INT >= 21) {
            levelLayoutManager.setElevation(2);
        }
        levelLayoutManager.setDebug(true);
        levelLayoutManager.setDirection(direction[directionIndex]);
    }

    private void handleClick() {
        btnGroupView.setOnStrClickListener((view, clickStr) -> {
            if (data != null) {
                switch (clickStr) {
                    case "add":
                        data.add(FakerData.createItemBean());
                        adapter.notifyItemInserted(data.size() - 1);
                        break;
                    case "remove":
                        if (data.size() > 0) {
                            int lastIndex = data.size() - 1;
                            data.remove(lastIndex);
                            adapter.notifyItemRemoved(lastIndex);
                        }
                        break;
                    case "turn direction":
                        levelLayoutManager.setDirection(direction[++directionIndex % direction.length]);
                }
            }
        });
    }

}