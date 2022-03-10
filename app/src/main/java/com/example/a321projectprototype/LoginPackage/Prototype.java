package com.example.a321projectprototype.LoginPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
//random

public class Prototype extends AppCompatActivity
{
    private Button login;
    private TextView email, password, signUp, passwordRest,textView;
    private ImageView logoImage;
    private String stringUserpassword, stringEmail;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getSupportActionBar().hide();

        email = findViewById(R.id.editLoginEmail);
        password = findViewById(R.id.editTextTextPassword);
        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.button);
        passwordRest = findViewById(R.id.ForgetPassword);
        progressBar = findViewById(R.id.loginProgressBar);
        logoImage = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        progressBar.setVisibility(View.INVISIBLE);


        login.setOnClickListener(loginAccount);
        signUp.setOnClickListener(signUpAccount);
        passwordRest.setOnClickListener(resetPassword);



    }



    private final View.OnClickListener loginAccount = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            loginMethod();
        }
    };

    private void loginMethod()
    {
        stringEmail = email.getText().toString();
        stringUserpassword = password.getText().toString();

        if(checkUserInput(stringEmail,stringUserpassword))
        {
            invisableSetOne();
            processingLogin();
        }
        else
        {
            return;
        }
    }

    public void processingLogin()
    {
        firebaseAuth = FirebaseAuth.getInstance();
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

                        Intent homepage  = new Intent(Prototype.this, HomePage.class);
                        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homepage);
                        finish();
                    }
                    else
                    {
                        invisableSetTwo();
                        Toast.makeText(Prototype.this,"Please Verify Your Email", Toast.LENGTH_LONG).show();
                        user.sendEmailVerification();
                    }
                }
                else
                {
                    invisableSetTwo();
                    Toast.makeText(Prototype.this,"Failed to Login:" + task.getResult(), Toast.LENGTH_LONG).show();


                }

            }
        });
    }

    private final View.OnClickListener signUpAccount = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent register = new Intent(Prototype.this, SignUp.class);
            startActivity(register);

        }
    };

    private final View.OnClickListener resetPassword = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent register = new Intent(Prototype.this, ResetPassword.class);
            startActivity(register);

        }
    };



    private boolean checkUserInput(String emailString, String passwordString)
    {

        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches() || emailString.isEmpty())
        {
            email.setError("Email Is Incorrect");
            email.requestFocus();
            return false;
        }

        if(passwordString.isEmpty() || passwordString.length() < 6)
        {
            password.setError("Password Is Incorrect");
            password.requestFocus();
            return false;
        }

        return true;
    }

    private void invisableSetOne()
    {
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        signUp.setVisibility(View.INVISIBLE);
        login.setVisibility(View.INVISIBLE);
        passwordRest.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        logoImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void invisableSetTwo()
    {
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        passwordRest.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        logoImage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }


    public void onBackPressed() {
        moveTaskToBack(false);
    }

}