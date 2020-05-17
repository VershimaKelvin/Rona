package com.barefooted.rona;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAvtivity extends AppCompatActivity {

    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_avtivity);

        circleImageView = findViewById(R.id.imageView);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Account Setup");

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetupAvtivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(SetupAvtivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        Toast.makeText(SetupAvtivity.this, "You already have permission ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
