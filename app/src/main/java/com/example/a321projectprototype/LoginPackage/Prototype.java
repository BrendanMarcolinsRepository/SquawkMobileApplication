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


public class Prototype extends AppCompatActivity
{
    private Button login;
    private TextView email, password, signUp, passwordRest;
    private String stringUserpassword, stringEmail;
    private final static int MY_REQUEST_CODE = 1;
    private FirebaseAuth firebaseAuth;


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


        login.setOnClickListener(loginAccount);
        signUp.setOnClickListener(signUpAccount);
        passwordRest.setOnClickListener(resetPassword);



    }



    private final View.OnClickListener loginAccount = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            stringEmail = email.getText().toString();
            stringUserpassword = password.getText().toString();



            if (!Patterns.EMAIL_ADDRESS.matcher(stringEmail).matches())
            {
                email.setError("Email is required");
                email.requestFocus();
            }

            if(stringEmail.isEmpty())
            {
                email.setError("Email is required");
                email.requestFocus();

            }
            if (stringUserpassword.isEmpty() || stringUserpassword.length() < 6)
            {
                password.setError("Password is required");
                password.requestFocus();
            }
            else
            {
                Intent loginProcessPage  = new Intent(Prototype.this, LoginProcess.class);
                loginProcessPage.putExtra("email", stringEmail);
                loginProcessPage.putExtra("password", stringUserpassword);
                startActivity(loginProcessPage);
            }
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

    private final View.OnClickListener resetPassword = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            Intent register = new Intent(Prototype.this, ResetPassword.class);
            startActivity(register);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE)
            {
                if (data == null)
                {
                    email.setError("Please Check Your Email");
                    email.requestFocus();
                    password.setError("Please Check Your Password");
                    password.requestFocus();
                }
                else
                {
                    email.setError(data.toString());
                    email.requestFocus();
                }

            }
        }
    }



}