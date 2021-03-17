package com.bike.maintenance.ars;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MechanicActivity extends AppCompatActivity {
    private TextView currentLocation;
//    private E

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);

        currentLocation = findViewById(R.id.currentLocation);

        findViewById(R.id.selectLocation).setOnClickListener(v -> {
        });
    }
}