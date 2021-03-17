package com.bike.maintenance.ars.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.MechanicActivity;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

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
                if (snapshot == null) return;
                String type = snapshot.child(AppConstant.USER_TYPE).getValue().toString();
                if (type.equals(AppConstant.CUSTOMER))
                    newActivity(LoginActivity.class); // todo customer activity
                else
                    newActivity(MechanicActivity.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error(null, error.getMessage());
            }
        });
    }

}
