package com.reone.layoutmanagerdemo.notify;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.javafaker.Cat;
import com.github.javafaker.Faker;
import com.reone.layoutmanagerdemo.R;
import com.reone.layoutmanagerdemo.utils.FakerData;
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
    @BindView(R.id.btn_group_view)
    BtnGroupView btnGroupView;
    private NotifyCardAdapter notifyCardAdapter;
    private List<NotifyCardAdapter.ItemBean> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_card);
        ButterKnife.bind(this);
        handleData();
        handleClick();
    }

    private void handleData() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(createItemBean());
        }
        notifyCardAdapter = new NotifyCardAdapter(this, data);
        notifyCardAdapter.setOnItemClickListener((item, position) -> Toast.makeText(this, "click " + item.name, Toast.LENGTH_SHORT).show());
        rvNotify.setLayoutManager(new NotifyCardLayoutManager()
                .maxCount(4)
                .needShadow(true)
                .padding(100, 100, 10, 10)
                .setOnItemRemoveListener(position -> {
                    data.remove(position);
                    notifyCardAdapter.notifyItemRemoved(position);
                }));
        rvNotify.setAdapter(notifyCardAdapter);
    }

    private void handleClick() {
        btnGroupView.setOnStrClickListener((view, clickStr) -> {
            switch (clickStr) {
                case "add":
                    data.add(createItemBean());
                    notifyCardAdapter.notifyItemInserted(data.size() - 1);
                    break;
                case "remove":
                    int lastIndex = data.size() - 1;
                    data.remove(lastIndex);
                    notifyCardAdapter.notifyItemRemoved(lastIndex);
                    break;
            }
        });
    }

    private int tempIndex = 0;

    private NotifyCardAdapter.ItemBean createItemBean() {
        Cat cat = Faker.instance().cat();
        NotifyCardAdapter.ItemBean itemBean = new NotifyCardAdapter.ItemBean();
        itemBean.name = cat.name();
        itemBean.breed = cat.breed();
        itemBean.registry = cat.registry();
        itemBean.img = FakerData.headers[tempIndex % FakerData.headers.length];
        tempIndex++;
        return itemBean;
    }

}