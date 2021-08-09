package com.bike.maintenance.ars.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bike.maintenance.ars.Activities.BaseActivity;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.adapters.OrdersRequestsAdapter;

public class OrdersActivity extends BaseActivity {

    private OrdersRequestsAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        RecyclerView mRecyclerView = findViewById(R.id.ordersRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new OrdersRequestsAdapter(this);

        mRecyclerView.setAdapter(mAdapter);
    }

}
