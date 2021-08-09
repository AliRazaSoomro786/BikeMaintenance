package com.bike.maintenance.ars;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bike.maintenance.ars.Activities.BaseActivity;

public class BookingActivity extends BaseActivity {
    EditText name, number, emailaddress, address, selectcompany, powerengine, selectservice, selectdate, selecttime;
    Button submit;

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
        selectservice = findViewById(R.id.selectservice);
        selectdate = findViewById(R.id.selcetdate);
        selecttime = findViewById(R.id.selecttime);
        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                else if (getText(selectservice).isEmpty())
                    selectservice.setError("Please Enter ServiceType");
                else if (getText(selectdate).isEmpty())
                    selectdate.setError("Please Enter Date");
                else if (getText(selecttime).isEmpty())
                    selecttime.setError("Please Enter Time");
            }
        });
    }

}