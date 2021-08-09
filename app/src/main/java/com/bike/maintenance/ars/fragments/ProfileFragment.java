package com.konnnect.walletdroid.fragments.fragments_navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.konnnect.walletdroid.PasswordView;
import com.konnnect.walletdroid.R;
import com.konnnect.walletdroid.activities.MainActivity;
import com.konnnect.walletdroid.models.Account;
import com.konnnect.walletdroid.models.Group;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class userprofile extends Fragment {

    private static final int PERMISSION_CODE = 10101;
    private static final int IMAGE_PICK_CODE = 10102;
    FirebaseAuth mAuth;
    Uri mImageUri;
    private UserprofileViewModel mViewModel;
    private CircleImageView userProfile;
    private StorageReference mStorageRef;

    public static userprofile newInstance() {
        return new userprofile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.userprofile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserprofileViewModel.class);
        // TODO: Use the ViewModel

        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("ProfileImages");
        TextView tvname = getView().findViewById(R.id.user_profile_name);
        TextView tvEmail = getView().findViewById(R.id.user_profile_email);

        tvname.setText(mAuth.getCurrentUser().getDisplayName());
        tvEmail.setText(mAuth.getCurrentUser().getEmail());

        getView().findViewById(R.id.actionPickImage).setOnClickListener(v -> {
            loadImage();
        });


        userProfile = getView().findViewById(R.id.userProfile);
        loadImaProfileImage();

        getView().findViewById(R.id.actionChangePassword).setOnClickListener(v -> {
            openDialog();
        });
    }


    private void openDialog() {
        View view = this.getLayoutInflater().inflate(R.layout.change_password, null);

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(view);

        PasswordView edtPassword = view.findViewById(R.id.edtEmail);


        view.findViewById(R.id.actionCancel).setOnClickListener(v1 -> dialog.dismiss());

        view.findViewById(R.id.applyPassword).setOnClickListener(v12 -> {
            String password = edtPassword.getText().toString().trim();
            if (password.isEmpty() || password.length() < 6) {
                Toast.makeText(getActivity(), "Please enter valid Password", Toast.LENGTH_SHORT).show();
                return;
            }
            final ProgressDialog mPB = new ProgressDialog(getActivity());
            mPB.setMessage("Please wait..");
            mPB.setCancelable(false);
            mPB.show();

            FirebaseAuth.getInstance().getCurrentUser().updatePassword(password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            mPB.dismiss();
                            Toast.makeText(getActivity(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(() -> {
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }, 500);
                        } else {
                            mPB.dismiss();
                            Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        dialog.setCancelable(false);
        dialog.show();

    }

    private void loadImage() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, PERMISSION_CODE);

        } else {
            pickImageFromGallery();

        }

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();

            } else {
                Toast.makeText(getActivity(), "Permission Denied.....", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (mImageUri != null) {
                final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(mImageUri));

                fileReference.putFile
                        (mImageUri)
                        .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri ->
                                addCityToDb(uri.toString())));
            } else {
                Toast.makeText(getActivity(), "No file selected",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addCityToDb(String url) {

        FirebaseFirestore.getInstance().collection("Accounts").whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot accountSnapshot = task.getResult();
                if (null != accountSnapshot) {
                    Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                    if (account.isPresent()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("imageURL", url);
                        hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirebaseDatabase.getInstance().
                                getReference().
                                child("ProfileImages")
                                .child(account.get().getId()).
                                setValue(hashMap);
                    }
                }
            }
        });

    }

    private void loadImaProfileImage() {

        FirebaseFirestore.getInstance().collection("Accounts").whereEqualTo("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot accountSnapshot = task.getResult();
                if (null != accountSnapshot) {
                    Optional<Account> account = accountSnapshot.toObjects(Account.class).stream().findFirst();
                    account.ifPresent(value -> FirebaseDatabase.getInstance().getReference()
                            .child("ProfileImages")
                            .child(value.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.hasChild("imageURL")) return;
                                    loadGlide(snapshot.child("imageURL").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }));
                }
            }
        });

        Executors.newSingleThreadExecutor().execute(() -> {
            FirebaseDatabase.getInstance().getReference().child("ProfileImages").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("imageURL")) {
                                getActivity().runOnUiThread(() -> loadGlide(dataSnapshot.child("imageURL").getValue().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        });


    }

    private void loadGlide(String imageURL) {
        Glide.with(this)
                .load(imageURL)
                .addListener(new RequestListener<Drawable>() {
                    @SuppressLint("NewApi")
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        userProfile.setImageResource(R.drawable.user_profile);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(userProfile);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            mImageUri = data.getData();
            userProfile.setImageURI(mImageUri);
            try {
                uploadFile();

            } catch (Exception e) {
                System.out.println("Exception Screen " + e.toString());
            }
        }
    }

}
