package com.reone.layoutmanagerdemo.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reone.layoutmanagerdemo.R;
import com.reone.layoutmanagerdemo.adapter.ImagesAdapter;
import com.reone.layoutmanagerdemo.bean.ItemBean;
import com.reone.layoutmanagerdemo.utils.FakerData;
import com.reone.layoutmanagerdemo.utils.ToastUtils;
import com.reone.layoutmanagerdemo.view.BtnGroupView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangxingsheng on 2021/2/3.
 * desc:
 */
public class PreViewerActivity extends AppCompatActivity {
    @BindView(R.id.rv_mid)
    RecyclerView rvMid;
    @BindView(R.id.btn_group_view)
    BtnGroupView btnGroupView;

    @Nullable
    private List<ItemBean> data;
    private ImagesAdapter imagesAdapter1;
    private LinearLayoutManager layoutManager1;
    private boolean lmVertical = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_viewer);
        ButterKnife.bind(this);
        handleClick();
        initList();
    }

    private void initList() {
        handleData();
        imagesAdapter1 = new ImagesAdapter(data, this);
        layoutManager1 = new LinearLayoutManager(this);
        rvMid.setLayoutManager(layoutManager1);
        rvMid.setAdapter(imagesAdapter1);
        imagesAdapter1.setOnItemClickListener((item, position) -> {
            ToastUtils.showShort("image click " + item.getName(), Toast.LENGTH_SHORT);
        });
    }

    private void handleData() {
        if (data == null) {
            data = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                data.add(FakerData.createItemBean());
            }
        }
    }

    private void handleClick() {
        btnGroupView.setOnStrClickListener((view, clickStr) -> {
            if (data != null) {
                switch (clickStr) {
                    case "turn":
                        lmVertical = !lmVertical;
                        layoutManager1.setOrientation(lmVertical ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL);
                        break;
                    case "add":
                        data.add(FakerData.createItemBean());
                        imagesAdapter1.notifyItemInserted(data.size() - 1);
                        break;
                    case "remove":
                        if (data.size() > 0) {
                            int lastIndex = data.size() - 1;
                            data.remove(lastIndex);
                            imagesAdapter1.notifyItemRemoved(lastIndex);
                        }
                        break;
                }
            }
        });
    }
}
