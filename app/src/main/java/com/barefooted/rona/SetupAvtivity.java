package com.barefooted.rona;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAvtivity extends AppCompatActivity {

    CircleImageView circleImageView;
    Uri mainImageUri = null;
    EditText name;
    Button setup;
    ProgressBar progressbar;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_avtivity);

        circleImageView = findViewById(R.id.imageView);
        name= findViewById(R.id.editText);
        setup=findViewById(R.id.setup_Button);
        progressbar = findViewById(R.id.progress);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference= firebaseStorage.getReference();



        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names =  name.getText().toString().trim();

                if(!TextUtils.isEmpty(names) && mainImageUri !=null){
                    String user_id = firebaseAuth.getCurrentUser().getUid();

                    progressbar.setVisibility(View.VISIBLE);
                    StorageReference image_path = storageReference.child("profile_images");
                    final StorageReference path =image_path.child(user_id + ".jpg");

                   UploadTask uploadTask = path.putFile(mainImageUri);



                   uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){
                                 Uri download_uri = task.getResult().getUploadSessionUri();
                                Toast.makeText(SetupAvtivity.this, "picture uploaded", Toast.LENGTH_SHORT).show();

                            }else{
                                String error= task.getException().toString();
                                Toast.makeText(SetupAvtivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    });

                }else{
                    Toast.makeText(SetupAvtivity.this, "You have to enter a Name and pick an image", Toast.LENGTH_LONG).show();
                }
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Account Setup");

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetupAvtivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(SetupAvtivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(SetupAvtivity.this);

                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
               mainImageUri = result.getUri();
               circleImageView.setImageURI(mainImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
