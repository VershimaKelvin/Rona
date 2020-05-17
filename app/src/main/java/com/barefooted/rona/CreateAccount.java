package com.barefooted.rona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccount extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser =  firebaseAuth.getCurrentUser();

    ProgressBar progressBar;
    EditText emailfield,passwordfield,confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
    }

    public void createMyAccount(View view) {
        emailfield=findViewById(R.id.email);
        passwordfield=findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        progressBar=findViewById(R.id.progressBar1);


        String  email = emailfield.getText().toString().trim();
        String  password = passwordfield.getText().toString().trim();
        String   conPassword = confirmpassword.getText().toString().trim();




        if(email.isEmpty()){
            Toast.makeText(this, "Please Enter an email address.", Toast.LENGTH_SHORT).show();
        }if (password.isEmpty()){
            Toast.makeText(this, "Enter your desired Password", Toast.LENGTH_SHORT).show();
        }if (conPassword.isEmpty()||!conPassword.equals(password)){
            Toast.makeText(this, "Confirm Password again", Toast.LENGTH_SHORT).show();
        }if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(conPassword)){
            if(conPassword.equals(password)){
                progressBar.setVisibility(View.VISIBLE);
                creatUser(email,password);
            }
        }
    }


    //This methods takes the user back to the login page if he already has an account
    public void haveAccount(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
    }




    //This method creates the User account using the passed email and password
    public void creatUser(String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Intent intent = new Intent(CreateAccount.this, SetupAvtivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(CreateAccount.this, "Password mismath, check and try again", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
