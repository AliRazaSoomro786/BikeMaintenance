package com.bike.maintenance.ars.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.MapsActivity;
import com.bike.maintenance.ars.R;

public class HomeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.actionOnRoad).
                setOnClickListener(v ->
                        startActivity(new Intent(v.getContext(), MapsActivity.class)));

        view.findViewById(R.id.actionOnBooking).setOnClickListener(v -> {
            open(new MechanicsFragment());
        });

        return view;
    }
}
