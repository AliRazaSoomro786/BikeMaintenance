package com.bike.maintenance.ars;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bike.maintenance.ars.Activities.BaseActivity;
import com.bike.maintenance.ars.Activities.SignUpActivity;
import com.bike.maintenance.ars.Model.Mechanic;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerActivity extends BaseActivity  {
    TextView help;
    Button appointment,onroad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        help= findViewById(R.id.help);
        appointment= findViewById(R.id.appointment);
        onroad = findViewById(R.id.onroad);
        onroad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity(onroad_activity.class);
                finish();
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity(appoitment_activity.class);
                finish();
            }
        });
    }
}