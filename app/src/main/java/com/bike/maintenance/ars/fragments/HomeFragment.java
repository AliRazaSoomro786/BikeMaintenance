package com.bike.maintenance.ars.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.BookingActivity;
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

        view.findViewById(R.id.actionOnBooking).
                setOnClickListener(v ->
                        startActivity(new Intent(v.getContext(), BookingActivity.class)));
        // todo me keh raha hoo net slow hai screenshare krny se aur slow ho raha mery screenshot bhi jaa rhy to me kaam to kr raha
        // todo hoo krky apk share krdeta hoo okay ?

        return view;
    }
}
