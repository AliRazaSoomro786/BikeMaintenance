package com.bike.maintenance.ars;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.bike.maintenance.ars.Activities.BaseActivity;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class MechanicActivity extends
        BaseActivity implements Listener, LocationData.AddressCallBack {
    private TextView currentLocation;
    private EasyWayLocation easyWayLocation;
    private GetLocationDetail getLocationDetail;
//    private E

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        easyWayLocation = new EasyWayLocation(this, false, this);
        easyWayLocation.startLocation();
        getLocationDetail = new GetLocationDetail(this, this);

        currentLocation = findViewById(R.id.currentLocation);
        findViewById(R.id.selectLocation).setOnClickListener(v -> {
        });

        findViewById(R.id.actionSignOut).setOnClickListener(v -> FirebaseAuth.getInstance().signOut());
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        getLocationDetail.getAddress(location.getLatitude(), location.getLongitude(), "address");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(AppConstant.LNG, location.getLongitude());
        hashMap.put(AppConstant.LAT, location.getLatitude());

        getReference(AppConstant.USERS).child(getAuth().getCurrentUser().getUid()).updateChildren(hashMap);


    }

    @Override
    public void locationCancelled() {

    }

    @Override
    public void locationData(LocationData locationData) {
        if (locationData == null) return;

        currentLocation.setText(locationData.getFull_address());
    }
}