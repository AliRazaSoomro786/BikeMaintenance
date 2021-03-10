package com.bike.maintenance.ars.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bike.maintenance.ars.MainActivity;
import com.bike.maintenance.ars.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {
    private EditText email, password;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.login).setOnClickListener(v -> {
            if (getText(email).isEmpty())
                email.setError("Email Required");
            else if (getText(password).isEmpty())
                password.setError("Password Required");
            else
                mAuth.signInWithEmailAndPassword(getText(email), getText(password))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
        });

        findViewById(R.id.dontHaveAccount).setOnClickListener(v -> {
            // DON'T HAVE ACCOUNT
        });
    }
}
