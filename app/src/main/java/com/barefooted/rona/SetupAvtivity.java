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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAvtivity extends AppCompatActivity {

    Uri downloadUri=null;
    String UserName;
    String user_id;
    CircleImageView circleImageView;
    Uri mainImageUri = null;
    EditText EditTextForName;
    Button setupButton;
    Boolean isChanged=false;
    ProgressBar progressbar;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    String collectionName = "Users";
    String nameKey = "name";
    String imageKey = "image";
    private String photoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_avtivity);


        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Account Setup");

        circleImageView = findViewById(R.id.imageView);
        EditTextForName= findViewById(R.id.editText);
        setupButton=findViewById(R.id.setup_Button);
        progressbar = findViewById(R.id.progress);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference= firebaseStorage.getReference();

        user_id = firebaseAuth.getCurrentUser().getUid();

       AutomaticLoadImageAndNane();


        //I put an onclick listener on the image
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

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = EditTextForName.getText().toString();

                if(!TextUtils.isEmpty(UserName)){
                    progressbar.setVisibility(View.VISIBLE);
                    if (isChanged) {
                        storeImageInFirebaseStorage();
                    }


                   // fireStoreOperation(isChanged);
                }else if(TextUtils.isEmpty(UserName)){
                    Toast.makeText(SetupAvtivity.this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void fireStoreOperation(boolean isChanged) {
        Map<String,String> map = new HashMap<>();
        map.put(nameKey,UserName);
        if(isChanged){
            map.put(imageKey,downloadUri.toString());
        }
        firebaseFirestore.collection("Users").document(user_id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent intent = new Intent(SetupAvtivity.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public void storeImageInFirebaseStorage(){

        user_id = firebaseAuth.getCurrentUser().getUid();
        final StorageReference image_path = storageReference.child("profile_Images").child(user_id + ".jpg");

        UploadTask uploadTask = image_path.putFile(mainImageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUri=uri;
                                photoLink = downloadUri.toString();
                            }
                        });
                        Toast.makeText(SetupAvtivity.this, photoLink, Toast.LENGTH_SHORT).show();
            }
        });

        progressbar.setVisibility(View.INVISIBLE);

    }


    private void AutomaticLoadImageAndNane() {
        firebaseFirestore.collection(collectionName).document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        String gottenname= task.getResult().getString(nameKey);
                        String image = task.getResult().getString(imageKey);
                        mainImageUri=Uri.parse(image);

                        EditTextForName.setText(gottenname);
                        RequestOptions placeholder = new RequestOptions();
                        placeholder.placeholder(R.drawable.pp);
                        Glide.with(SetupAvtivity.this).setDefaultRequestOptions(placeholder).load(image).into(circleImageView);
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

               isChanged=true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
