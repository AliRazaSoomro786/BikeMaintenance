package com.bike.maintenance.ars.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public String getText(EditText editText) {
        return editText.getText().toString();
    }

    public void error(View view, String message) {
//        Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

    }

    public DatabaseReference getReference(String child) {
        return FirebaseDatabase.getInstance().getReference().child(child);
    }

    public void success(View view, String message) {
//        Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void newActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
