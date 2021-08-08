package com.bike.maintenance.ars;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.bike.maintenance.ars.Activities.BaseActivity;
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

public class onroad_activity extends BaseActivity implements OnMapReadyCallback, Listener {
    private GoogleMap mMap;
    private ArrayList<Mechanic> mechanics = new ArrayList<>();
    private ArrayList<Marker> mMechanicsMarkers = new ArrayList<>();
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onroad_activity);
        EasyWayLocation easyWayLocation = new EasyWayLocation(this, false, this);
        easyWayLocation.startLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMechanics();
    }

    private void loadMechanics() {
        getReference(AppConstant.USERS).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                if (snapshot.child(snap.getKey()).child(AppConstant.USER_TYPE).getValue().toString().equals(AppConstant.MECHANIC)) {
                                    mechanics.add(new Mechanic(
                                            Double.parseDouble(snapshot.child(snap.getKey()).child(AppConstant.LAT).getValue().toString()),
                                            Double.parseDouble(snapshot.child(snap.getKey()).child(AppConstant.LNG).getValue().toString()),
                                            snapshot.child(snap.getKey()).child(AppConstant.NAME).getValue().toString(),
                                            snapshot.child(snap.getKey()).child(AppConstant.UID).getValue().toString(),
                                            snapshot.child(snap.getKey()).child(AppConstant.USER_TYPE).getValue().toString(),
                                            snapshot.child(snap.getKey()).child(AppConstant.PHONE).getValue().toString()
                                    ));

                                    if (mechanics.isEmpty()) return;

                                    if (!mMechanicsMarkers.isEmpty())
                                        for (Marker marker : mMechanicsMarkers)
                                            marker.remove();

                                    for (Mechanic mechanic : mechanics) {
                                        mMechanicsMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(mechanic.getLat(), mechanic.getLng())).title(mechanic.getName())));
                                    }
                                }

                            }
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: ");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        if (location == null) return;
        if (mMarker != null) mMarker.remove();
        mMarker = mMap.addMarker(new MarkerOptions().title("Current location").position(new LatLng(location.getLatitude(), location.getLongitude())));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.f));

    }

    @Override
    public void locationCancelled() {

    }
}