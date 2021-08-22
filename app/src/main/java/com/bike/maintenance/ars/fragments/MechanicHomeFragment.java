package com.bike.maintenance.ars.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.MapsActivity;
import com.bike.maintenance.ars.Model.Mechanic;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.example.easywaylocation.GetLocationDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MechanicHomeFragment extends BaseFragment {
    private GetLocationDetail getLocationDetail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mechanic_home_fragment, container, false);

        TextView shopLocation = view.findViewById(R.id.currentLocation);

        view.findViewById(R.id.actionAddLocation).setOnClickListener(v -> {
            startActivity(new Intent(inflater.getContext(), MapsActivity.class));
        });

        getLocationDetail = new GetLocationDetail(locationData -> {
            if (locationData == null || locationData.getFull_address() == null) return;

            shopLocation.setText(locationData.getFull_address());

        }, inflater.getContext());


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDetails();

    }

    private void loadUserDetails() {
        FirebaseDatabase.getInstance().getReference()
                .child(AppConstant.USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) return;
                        Mechanic mechanic = snapshot.getValue(Mechanic.class);

                        if (mechanic == null) return;

                        getLocationDetail.getAddress(mechanic.getLat(), mechanic.getLng(), "address");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
