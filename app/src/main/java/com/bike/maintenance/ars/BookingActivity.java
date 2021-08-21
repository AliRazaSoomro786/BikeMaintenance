package com.bike.maintenance.ars;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bike.maintenance.ars.Activities.BaseActivity;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.Utils.DialogUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class BookingActivity extends BaseActivity {
    private EditText name, number, emailaddress, address, selectcompany, powerengine, repairDescription, selectdate, selecttime;
    private Button submit;

    private DialogUtils mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoitment_activity);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        emailaddress = findViewById(R.id.emailaddress);
        address = findViewById(R.id.address);
        selectcompany = findViewById(R.id.selectcompany);
        powerengine = findViewById(R.id.powerengine);
        repairDescription = findViewById(R.id.selectservice);
        submit = findViewById(R.id.submit);

        mDialog = new DialogUtils(this);


        submit.setOnClickListener(v -> {
            if (getText(name).isEmpty())
                name.setError("Please Enter Name");

            else if (getText(number).isEmpty())
                number.setError("Please Enter Number");

            else if (getText(emailaddress).isEmpty())
                emailaddress.setError("Please Enter EmialAddress");

            else if (getText(address).isEmpty())
                address.setError("Please Enter Address");

            else if (getText(selectcompany).isEmpty())
                selectcompany.setError("Please Enter CompanyName");

            else if (getText(powerengine).isEmpty())
                powerengine.setError("Please Enter PowerEngine(cc)");

            else if (getText(repairDescription).isEmpty())
                repairDescription.setError("Please Enter ServiceType");

            else {

                String uid = getIntent().getStringExtra("uid");
                String key = UUID.randomUUID().toString();
                mDialog.show("Please wait....");

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", getText(name));
                hashMap.put("number", getText(number));
                hashMap.put("email", getText(emailaddress));
                hashMap.put("address", getText(address));
                hashMap.put("company", getText(selectcompany));
                hashMap.put("powerengine", getText(powerengine));
                hashMap.put("repairdescription", getText(repairDescription));
                hashMap.put("timestamp", new Date(System.currentTimeMillis()) + "");
                hashMap.put("mechanicuid", uid);
                hashMap.put("staus", false);
                hashMap.put("key", key);
                hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());

                FirebaseDatabase.getInstance().getReference()
                        .child(AppConstant.BOOKING_REQUESTS)
                        .child(key)
                        .setValue(hashMap).addOnCompleteListener(task -> {
                    if (mDialog.isShowing()) mDialog.dismiss();
                    Toast.makeText(this, "Booking request sent successfully...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(this::finish, 300);

                });
            }
        });
    }

}