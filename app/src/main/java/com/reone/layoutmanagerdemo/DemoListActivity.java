package com.reone.layoutmanagerdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.reone.layoutmanagerpkg.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoListActivity extends AppCompatActivity {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);
        ButterKnife.bind(this);
    }
}