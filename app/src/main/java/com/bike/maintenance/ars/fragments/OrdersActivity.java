package com.bike.maintenance.ars.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.adapters.OrdersRequestsAdapter;

public class OrdersFragment extends Fragment {

    private OrdersRequestsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.ordersRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        mAdapter = new OrdersRequestsAdapter(getActivity());

        mRecyclerView.setAdapter(mAdapter);



        return view;
    }
}
