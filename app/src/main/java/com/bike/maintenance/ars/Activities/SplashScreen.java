package com.bike.maintenance.ars.Activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bike.maintenance.ars.CustomerActivity;
import com.bike.maintenance.ars.MechanicActivity;
import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.musicplayer.MusicPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SplashScreen extends BaseActivity implements Response.ErrorListener, Response.Listener<String> {
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
        String url = "https://firebasestorage.googleapis.com/v0/b/bikemaintenance-da47e.appspot.com/o/bikemaintenance-da47e-default-rtdb-export.json?alt=media&token=4e01d9f8-f8a5-47d5-a6ca-4b8bf64c2c0c";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this);
        Volley.newRequestQueue(this).add(stringRequest);


   /*     MusicPlayer musicPlayer = new MusicPlayer(this);

        try {
            AssetFileDescriptor descriptor = getAssets().openFd("office_phone_ringigng.mp3");
            musicPlayer.play(descriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    private void validateUser(String uid) {
        getReference(AppConstant.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("TAG", "onErrorResponse: " + error.getMessage());
//        showDialog(error.getMessage());

    }

    @Override
    public void onResponse(String response) {
        Log.d("TAG", "onResponse: " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject users = jsonObject.getJSONObject("users");
            JSONObject user_1 = users.getJSONObject("U0LNwWlpuWgEbZHkjmB0SNJLjF43");
            double lat = user_1.getDouble("lat");
            String name = user_1.getString("name");
            String phone = user_1.getString("phone");
            String string = "Lat :" + lat + "\n Name :" + name + "\n Phone :" + phone;
//            showDialog(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        showDialog(response);
    }


//    void showDialog(String error) {
//        newActivity(CustomerActivity.class);
//       // new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog).setPositiveButton("ok", null).setMessage(error).show();
//    }

}
