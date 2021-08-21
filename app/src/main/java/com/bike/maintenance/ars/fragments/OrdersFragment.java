package com.bike.maintenance.ars.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bike.maintenance.ars.Model.Bookings;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.adapters.BookingsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends BaseFragment {

    private BookingsAdapter mAdapter;
    private final List<Bookings> bookings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orders_fragment, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.ordersRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        mAdapter = new BookingsAdapter(bookings);

        mRecyclerView.setAdapter(mAdapter);

        loadBookings();

        return view;
    }


    private void loadBookings() {
        FirebaseDatabase.getInstance().getReference()
                .child(AppConstant.BOOKING_REQUESTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) return;

                        bookings.clear();

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Bookings item = snap.getValue(Bookings.class);

                            if (item.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                bookings.add(item);

                        }

                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}
