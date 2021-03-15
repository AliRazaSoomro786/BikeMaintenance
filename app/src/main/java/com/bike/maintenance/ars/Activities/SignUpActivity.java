package com.bike.maintenance.ars.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.Utils.DialogUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    EditText name, phone, email, password;
    String userType = "";
    private DialogUtils mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.dontHaveAccount).setOnClickListener(this);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        mDialog = new DialogUtils(this);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.mechanic) userType = AppConstant.MECHANIC;
            else userType = AppConstant.CUSTOMER;
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUp) {
            if (userType.isEmpty()) {
                error(v, "Please select user type");
            } else {
                mDialog.show("Please wait ... ");
                getAuth().createUserWithEmailAndPassword(getText(email), getText(password))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                mDialog.updateMessage("Creating user profile ...");
                                addUserDetails(task.getResult().getUser().getUid());
                            } else {
                                error(v, task.getException().getMessage());
                            }
                        });
            }
        } else {
            newActivity(LoginActivity.class);
            finish();
        }
    }

    private void addUserDetails(String uid) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(AppConstant.NAME, getText(name));
        hashMap.put(AppConstant.PHONE, getText(phone));
        hashMap.put(AppConstant.USER_TYPE, userType);
        hashMap.put(AppConstant.UID, uid);
        hashMap.put(AppConstant.LAT, 0.0);
        hashMap.put(AppConstant.LNG, 0.0);

        getReference(AppConstant.USERS).child(uid).setValue(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                if (mDialog.isShowing()) mDialog.dismiss();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }
}
