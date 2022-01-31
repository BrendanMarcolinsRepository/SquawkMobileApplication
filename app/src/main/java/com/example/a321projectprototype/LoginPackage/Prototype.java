package com.example.a321projectprototype.LoginPackage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Prototype extends AppCompatActivity
{
    private Button login;
    private TextView email, password, signUp;
    private String stringUserpassword, stringEmail;
    private UserDatabase userDatabase;
    private UserModel userModel;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getSupportActionBar().hide();


        userDatabase = new UserDatabase(this);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.button);
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        login.setOnClickListener(loginAccount);
        signUp.setOnClickListener(signUpAccount);

        firebaseAuth = FirebaseAuth.getInstance();

    }



    private final View.OnClickListener loginAccount = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            stringEmail = email.getText().toString();
            stringUserpassword = password.getText().toString();



            if (!Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches() || stringEmail.length() < 6 || stringEmail.isEmpty())
            {
                email.setError("Email is required");
                email.requestFocus();
            }

            if (stringUserpassword.isEmpty() || stringUserpassword.length() < 6)
            {
                password.setError("Password is required");
                password.requestFocus();
            }



           progressBar.setVisibility(View.VISIBLE);
           firebaseAuth.signInWithEmailAndPassword(stringEmail,stringUserpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
           {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task)
               {
                   if(task.isSuccessful())
                   {
                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                       if(user.isEmailVerified())
                       {
                           userModel = new UserModel();

                           System.out.println("worked");
                           Intent homepage  = new Intent(Prototype.this, HomePage.class);
                           progressBar.setVisibility(View.INVISIBLE);
                           startActivity(homepage);
                       }
                       else
                       {
                           user.sendEmailVerification();
                           progressBar.setVisibility(View.INVISIBLE);
                           Toast.makeText(Prototype.this,"Please Verify Your Account Through Your Email", Toast.LENGTH_LONG).show();
                       }
                   }
                   else
                   {
                       progressBar.setVisibility(View.INVISIBLE);
                       Toast.makeText(Prototype.this,"Failed to Login", Toast.LENGTH_LONG).show();
                   }

               }
           });


        }
    };

    private final View.OnClickListener signUpAccount = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent register = new Intent(Prototype.this, SignUp.class);
            startActivity(register);

        }
    };


}