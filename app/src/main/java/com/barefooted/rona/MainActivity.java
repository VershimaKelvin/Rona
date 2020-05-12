package com.barefooted.rona;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        updateUI(firebaseUser);
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser!=null){

            int splashtime=3000;
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              Intent next = new Intent(MainActivity.this, Home.class);
                                              startActivity(next);
                                              finish();
                                          }
                                      },splashtime
            );
        }else{
            int splashtime=5000;
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              Intent next = new Intent(MainActivity.this, Login.class);
                                              startActivity(next);
                                              finish();
                                          }
                                      },splashtime
            );
        }
    }
}
