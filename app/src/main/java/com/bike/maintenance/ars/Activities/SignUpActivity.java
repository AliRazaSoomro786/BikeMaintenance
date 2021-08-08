package com.bike.maintenance.ars.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bike.maintenance.ars.R;
import com.bike.maintenance.ars.Utils.AppConstant;
import com.bike.maintenance.ars.Utils.DialogUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    EditText name, phone, email, password;
    String userType = "";
    private DialogUtils mDialog;
    ImageView uploadimage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        uploadimage = findViewById(R.id.uploadimage);

        findViewById(R.id.signUp).setOnClickListener(this);
        findViewById(R.id.dontHaveAccount).setOnClickListener(this);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads/5645464356.jgp");
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1234);

                }
                selectImage();
            }
        });
        mDialog = new DialogUtils(this);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.mechanic) userType = AppConstant.MECHANIC;
            else userType = AppConstant.CUSTOMER;
        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUp) {
            if(getText(name).isEmpty())
                error(v, "Please Enter Name");
            else if(getText(phone).isEmpty())
                error(v, "Please Enter Phone Number");
            else if(getText(email).isEmpty())
                error(v,"Please Enter Email");
            else if(getText(password).isEmpty())
                error(v,"Please Enter Password");
            else if (userType.isEmpty()) {
                error(v, "Please Select User Type");
            }
          /*  else if(uploadimage.getDrawable()==null){
                error(v,"Please ADD Image");
            }*/
                else {
                mDialog.show("Please wait ... ");
                getAuth().createUserWithEmailAndPassword(getText(email), getText(password))
                        .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                           // uploadimage ();
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
    public void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                uploadimage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    public void uploadimage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(SignUpActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(SignUpActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }


}
