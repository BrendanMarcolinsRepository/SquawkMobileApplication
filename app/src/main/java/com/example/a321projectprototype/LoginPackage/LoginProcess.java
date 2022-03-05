package com.example.a321projectprototype.LoginPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginProcess extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String password, email;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_page);
        getSupportActionBar().hide();

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        TextView textView = findViewById(R.id.processpagetextview);
        textView.setText("Chirping you in...");

        progressBar = findViewById(R.id.loginProgressBar);


        processingLogin();


    }

    public void processingLogin()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified())
                    {


                        System.out.println("=================> User" + user.getEmail());
                        Intent homepage  = new Intent(LoginProcess.this, HomePage.class);
                        progressBar.setVisibility(View.INVISIBLE);

                        startActivity(homepage);
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Intent intent = new Intent();
                        intent.putExtra("value", "Please verify your email");
                        setResult(RESULT_OK, intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginProcess.this,"Failed to Login", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, null);
                    finish();

                }

            }
        });
    }
}
