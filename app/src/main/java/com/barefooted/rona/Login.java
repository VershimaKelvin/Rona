package com.barefooted.rona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText emailField,passwordField;
    Button login,createAccount;
    TextView forgotPassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();




    }


    public void login(View view) {
        emailField =findViewById(R.id.emailfield);
        passwordField=findViewById(R.id.passwordfield);
        progressBar = findViewById(R.id.progressBar);

        String email =emailField.getText().toString().trim();
        String password= passwordField.getText().toString().trim();




        if(email.isEmpty()&&password.isEmpty()){
            Toast.makeText(this, "Email and password required ", Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()){
            Toast.makeText(this, "Enter a valid Email", Toast.LENGTH_SHORT).show();
        }else if (password.isEmpty()){
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){
            progressBar.setVisibility(View.VISIBLE);
            signInUser(email,password);
        }
    }


    //This method takes the user to the create account screen/Activity
    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }



    //This method is to help the user reset his password
    public void forgotPassword(View view) {
    }


    //The overrided method checks if the user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }



    //this method signs the user in using the passes email and password
    public void signInUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    String messeage = "User Authentication failed, check Email and password";
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, messeage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
