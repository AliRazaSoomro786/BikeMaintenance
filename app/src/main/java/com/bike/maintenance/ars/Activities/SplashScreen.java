package com.bike.maintenance.ars.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.CustomerActivity;
import com.bike.maintenance.ars.MainActivity;
import com.bike.maintenance.ars.MechanicActivity;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SplashScreen extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

//
        if (isNewUser()) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }, 3000);
        } else {
            validateUser(getAuth().getCurrentUser().getUid());
        }

    }

    private void validateUser(String uid) {
        getReference(AppConstant.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) return;

                String type = snapshot.child(AppConstant.USER_TYPE).getValue().toString();

                if (type.equals(AppConstant.CUSTOMER))
                    newActivity(CustomerActivity.class); // todo customer activity
                else
                    newActivity(MechanicActivity.class);

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error(getWindow().getDecorView().getRootView(), error.getMessage());
            }
        });
    }


}
