package com.bike.maintenance.ars;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bike.maintenance.ars.Activities.BaseActivity;
import com.bike.maintenance.ars.Activities.LoginActivity;
import com.bike.maintenance.ars.Utils.PermissionHelper;
import com.bike.maintenance.ars.fragments.HomeFragment;
import com.bike.maintenance.ars.fragments.OrdersActivity;
import com.bike.maintenance.ars.fragments.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.saharsh.chatapp.Fragments.ChatsFragment;
import com.saharsh.chatapp.Fragments.UsersFragment;
//import com.saharsh.chatapp.Fragments.UsersFragment;

public class CustomerActivity extends BaseActivity {
    private ImageView imgHome, imgOrders, imgChat, imgProfile;
    private TextView fragmentsLabel;

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        imgHome = findViewById(R.id.actionHome);
        imgOrders = findViewById(R.id.actionOrders);
        imgChat = findViewById(R.id.actionChat);
        imgProfile = findViewById(R.id.actionProfile);

        fragmentsLabel = findViewById(R.id.fragmentsLabel);

        permissionHelper = new PermissionHelper(this);


        imgHome.setOnClickListener(v -> {
            show(imgHome, new HomeFragment(), "Home");
        });

        imgOrders.setOnClickListener(v -> {
            startActivity(new Intent(CustomerActivity.this, OrdersActivity.class));
        });

        imgChat.setOnClickListener(v -> {
            show(imgChat, new ChatsFragment(), "Chat");
        });

        imgProfile.setOnClickListener(v -> {
            show(imgProfile, new ProfileFragment(), "Profile");
        });

        if (!permissionHelper.isGranted())
            permissionHelper.request();
        else showFragment(new HomeFragment(), "Home");

        findViewById(R.id.actionMore).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
            finish();
        });


    }

    private void showFragment(Fragment fragment, String fragTAG) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(fragment.getClass().getSimpleName());

        if (frag != null) return;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, fragment, fragTAG)
                .commit();

        fragmentsLabel.setText(fragment.getTag());


    }


    private void show(ImageView img, Fragment fragment, String fragTAG) {
        ImageView[] navigationList = {imgChat, imgHome, imgOrders, imgProfile};
        for (ImageView imageView : navigationList) {
            if (img.getTag().equals(imageView.getTag())) {
                img.setBackgroundResource(R.drawable.rectangle_bg);
                img.setColorFilter(Color.parseColor("#FFFFFFFF"), PorterDuff.Mode.SRC_IN);
            } else {
                imageView.setBackgroundResource(R.drawable.rectangle_bg_transparent);
                imageView.setColorFilter(Color.parseColor("#81FFFFFF"), PorterDuff.Mode.SRC_IN);

            }


        }

        showFragment(fragment, fragTAG);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionHelper.PERMISSION_CODE == requestCode && permissionHelper.isGranted())
            showFragment(new HomeFragment(), "Home");
    }
}