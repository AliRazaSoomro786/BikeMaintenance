package com.bike.maintenance.ars.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bike.maintenance.ars.BookingActivity;
import com.bike.maintenance.ars.Model.Mechanic;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.adapters.MechanicsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saharsh.chatapp.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class MechanicsFragment extends BaseFragment {
    private MechanicsAdapter mAdapter;
    private final List<Mechanic> mechanics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mechanics, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.mechanicsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        mAdapter = new MechanicsAdapter(mechanics, new MechanicsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(inflater.getContext(), BookingActivity.class);
                intent.putExtra("uid", mechanics.get(position).getUid());
                startActivity(intent);
            }

            @Override
            public void onSendMessage(int position) {
                Intent intent = new Intent(inflater.getContext(), MessageActivity.class);
                intent.putExtra("uid", mechanics.get(position).getUid());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(mAdapter);

        loadMechanics();

        return view;
    }


    private void loadMechanics() {
        FirebaseDatabase.getInstance().getReference()
                .child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) return;

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Mechanic mechanic = snap.getValue(Mechanic.class);
                    if (mechanic == null) return;

                    if (mechanic.getUserType().equals(AppConstant.MECHANIC))
                        mechanics.add(mechanic);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
