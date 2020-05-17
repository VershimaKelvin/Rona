package com.barefooted.rona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Rona News");

        firebaseAuth=FirebaseAuth.getInstance();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.log_out:
                logOut();

                return  true;

            case R.id.Account_settings:
                Intent intent = new Intent(this,SetupAvtivity.class);
                startActivity(intent);


                default:
                    return false;
        }

    }

    private void logOut() {
        firebaseAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }
}
