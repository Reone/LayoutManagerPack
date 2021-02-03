package com.reone.layoutmanagerdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reone.layoutmanagerdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoListActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private DemoItem[] demoList = new DemoItem[]{
            new DemoItem("卡片式通知", NotifyCardActivity.class),
            new DemoItem("层级式", LevelActivity.class),
            new DemoItem("预览式", PreViewerActivity.class),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new DemoListAdapter());
    }

    static class DemoListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public DemoListViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    static class DemoItem {
        String title;
        Class<? extends AppCompatActivity> activityClass;

        public DemoItem(String title, Class<? extends AppCompatActivity> activityClass) {
            this.title = title;
            this.activityClass = activityClass;
        }
    }

    public class DemoListAdapter extends RecyclerView.Adapter<DemoListViewHolder> {

        @NonNull
        @Override
        public DemoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DemoListViewHolder(LayoutInflater.from(DemoListActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final DemoListViewHolder holder, int position) {
            final DemoItem item = demoList[holder.getAdapterPosition()];
            holder.textView.setText(item.title);
            holder.itemView.setOnClickListener(v -> startActivity(new Intent(DemoListActivity.this, item.activityClass)));
        }

        @Override
        public int getItemCount() {
            return demoList.length;
        }
    }
}