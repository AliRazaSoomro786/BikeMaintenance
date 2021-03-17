package com.bike.maintenance.ars.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.Utils.DialogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    private EditText email, password;
    private DialogUtils mDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mDialog = new DialogUtils(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(v -> {
            if (getText(email).isEmpty())
                email.setError("Email Required");
            else if (getText(password).isEmpty())
                password.setError("Password Required");
            else {
                mDialog.show("Please wait ... ");
                getAuth().signInWithEmailAndPassword(getText(email), getText(password))
                        .addOnCompleteListener(task -> {
                            if (mDialog.isShowing()) mDialog.dismiss();
                            if (task.isSuccessful()) {
                                validateUser(task.getResult().getUser().getUid());
                            } else {
                                error(v, task.getException().getMessage());
                            }
                        });
            }
        });

        findViewById(R.id.dontHaveAccount).setOnClickListener(v -> {
            newActivity(SignUpActivity.class);
            finish();
        });
    }

    private void validateUser(String uid) {
        getReference(AppConstant.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot == null) return;
                String type = snapshot.child(AppConstant.USER_TYPE).getValue().toString();
                if (type.equals(AppConstant.CUSTOMER))
                    Toast.makeText(LoginActivity.this, "Login As a customer", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(LoginActivity.this, "Login As a mechanic", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
