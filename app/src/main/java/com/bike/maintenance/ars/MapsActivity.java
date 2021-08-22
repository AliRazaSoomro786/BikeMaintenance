package com.bike.maintenance.ars;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bike.maintenance.ars.Activities.BaseActivity;
import com.bike.maintenance.ars.Model.Mechanic;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.Utils.Helper;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.saharsh.chatapp.MessageActivity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bike.maintenance.ars.Utils.Helper.makeCall;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, Listener {
    private final ArrayList<Mechanic> mechanics = new ArrayList<>();
    private final ArrayList<Marker> mMechanicsMarkers = new ArrayList<>();
    private GoogleMap mMap;
    private Marker mMarker;

    private LatLng shopLocation;

    private static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    private EasyWayLocation easyWayLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onroad_activity);
        easyWayLocation = new EasyWayLocation(this, false, this);
        easyWayLocation.startLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.actionBackPress).setOnClickListener(v -> {
            finish();
        });
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); //added line
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true); //my code did not have t

        if (Helper.userType.equals(AppConstant.CUSTOMER)) {
            loadMechanics();
            mMap.setOnMarkerClickListener(marker -> {
                if (marker.getTag() == null) return false;
                mechanicDetailsDialog(marker.getTag().toString());
                return false;
            });
        } else {
            Button button = findViewById(R.id.addLocation);
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(v -> {
                if (shopLocation == null) {
                    Toast.makeText(this, "Please long press on your shop to register location", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(AppConstant.LNG, shopLocation.longitude);
                hashMap.put(AppConstant.LAT, shopLocation.latitude);

                getReference(AppConstant.USERS).child(getAuth().getCurrentUser().getUid()).updateChildren(hashMap)
                        .addOnCompleteListener(task ->
                                Toast.makeText(MapsActivity.this, "Shop locaiton updated successfully...", Toast.LENGTH_SHORT).show());

            });

            mMap.setOnMapLongClickListener(latLng -> {
                shopLocation = latLng;
                mMap.addMarker(new MarkerOptions().position(latLng).title("Shop Position"));
                Toast.makeText(this, "Shop location selected successfully", Toast.LENGTH_SHORT).show();
            });
        }


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

                                    Bitmap markerBitmap = scaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mechanic), 70, 70);

                                    for (Mechanic mechanic : mechanics) {
                                        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(mechanic.getLat(), mechanic.getLng())).
                                                icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)).title(mechanic.getName()));
                                        marker.setTag(mechanic.getUid());
                                        mMechanicsMarkers.add(marker);
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

    private void mechanicDetailsDialog(final String uid) {
        LayoutInflater factory = LayoutInflater.from(MapsActivity.this);

        final View mView = factory.inflate(R.layout.mechanic_info_dialog, null);
        final androidx.appcompat.app.AlertDialog deleteDialog = new androidx.appcompat.app.AlertDialog.Builder(MapsActivity.this).create();

        deleteDialog.setView(mView);

        TextView name = mView.findViewById(R.id.custom_dialoge_tv_name);
        TextView phone = mView.findViewById(R.id.custom_dialoge_tv_phone);
        TextView address = mView.findViewById(R.id.custom_dialoge_tv_address);


        final Mechanic item = mechanic(uid);

        if (item == null) return;

        name.setText(item.getName());

        phone.setText(item.getPhone());

        address.setText("Location : Lat : Lng" + item.getLat() + ":" + item.getLng());

        mView.findViewById(R.id.custom_dialoge_msg_icon).
                setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("uid", item.getUid());
                    startActivity(intent);
                });

        mView.findViewById(R.id.actionSendRequest).setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookingActivity.class);
            intent.putExtra("uid", item.getUid());
            startActivity(intent);
        });

        mView.findViewById(R.id.custom_dialoge_call_icon).
                setOnClickListener(v -> makeCall(item.getPhone(), this));

        TextView cancel = mView.findViewById(R.id.custom_dialge_tv_cancel);
        cancel.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(v -> deleteDialog.dismiss());

        deleteDialog.show();
    }

    private Mechanic mechanic(String uid) {
        for (Mechanic mechanic : mechanics)
            if (mechanic.getUid().equals(uid)) return mechanic;

        return null;
    }


    @Override
    public void currentLocation(Location location) {
        if (location == null) return;
        if (mMarker != null)
            mMarker.remove();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.f));
        easyWayLocation.endUpdates();

    }

    @Override
    public void locationCancelled() {

    }
}