package com.reone.layoutmanagerdemo.notify;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.reone.layoutmanagerdemo.R;
import com.reone.layoutmanagerdemo.bean.ItemBean;
import com.reone.layoutmanagerdemo.utils.FakerData;
import com.reone.layoutmanagerdemo.utils.ToastUtils;
import com.reone.layoutmanagerdemo.view.BtnGroupView;
import com.reone.layoutmanagerpkg.notifycard.NotifyCardLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangxingsheng on 2020/6/19.
 * desc:
 */
public class NotifyCardActivity extends AppCompatActivity {
    @BindView(R.id.rv_notify)
    RecyclerView rvNotify;
    @BindView(R.id.rv_notify2)
    RecyclerView rvNotify2;
    @BindView(R.id.btn_group_view)
    BtnGroupView btnGroupView;
    private NotifyCardAdapter notifyCardAdapter;
    private NotifyCardAdapter2 notifyCardAdapter2;
    @Nullable
    private List<ItemBean> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_card);
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
            for (int i = 0; i < 5; i++) {
                data.add(FakerData.createItemBean());
            }
            notifyCardAdapter = new NotifyCardAdapter(this, data);
            notifyCardAdapter.setOnItemClickListener((item, position) -> {
                ToastUtils.showShort("notify 1 click " + item.getName(), Toast.LENGTH_SHORT);
            });
            rvNotify.setLayoutManager(new NotifyCardLayoutManager.Builder()
                    .maxCount(4)
                    .showShadow(true)
                    .elevation(1)
                    .padding(100, 10, 100, 10)
                    .onItemRemoveListener(position -> {
                        data.remove(position);
                        notifyCardAdapter.notifyItemRemoved(position);
                        notifyCardAdapter2.notifyItemRemoved(position);
                    }).create());
            rvNotify.setAdapter(notifyCardAdapter);
            notifyCardAdapter2 = new NotifyCardAdapter2(this, data);
            notifyCardAdapter2.setOnItemClickListener((item, position) -> ToastUtils.showShort("notify 2 click " + item.getName()));
            rvNotify2.setLayoutManager(new NotifyCardLayoutManager.Builder()
                    .maxCount(3)
                    .elevation(5)
                    .debug(true)
                    .direction(NotifyCardLayoutManager.Direction.LAYOUT_DIRECTION_DOWN_RIGHT)
                    .padding(100, 10, 100, 10)
                    .onItemRemoveListener(position -> {
                        data.remove(position);
                        notifyCardAdapter.notifyItemRemoved(position);
                        notifyCardAdapter2.notifyItemRemoved(position);
                    }).create());
            rvNotify2.setAdapter(notifyCardAdapter2);
        }
    }

    private void handleClick() {
        btnGroupView.setOnStrClickListener((view, clickStr) -> {
            if (data != null) {
                switch (clickStr) {
                    case "add":
                        data.add(FakerData.createItemBean());
                        notifyCardAdapter.notifyItemInserted(data.size() - 1);
                        notifyCardAdapter2.notifyItemInserted(data.size() - 1);
                        break;
                    case "remove":
                        int lastIndex = data.size() - 1;
                        data.remove(lastIndex);
                        notifyCardAdapter.notifyItemRemoved(lastIndex);
                        notifyCardAdapter2.notifyItemRemoved(lastIndex);
                        break;
                }
            }
        });
    }

}