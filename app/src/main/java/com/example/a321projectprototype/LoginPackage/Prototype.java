package com.example.a321projectprototype.LoginPackage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;


import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;

public class Prototype extends AppCompatActivity
{
    private Button login;
    private TextView username, password, signUp;
    private String stringUserpassword, stringUsername;
    private UserModel userModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getSupportActionBar().hide();


        userModel = new  UserModel(1,"Brendan","s","m@gmail.com");
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.button);
        login.setOnClickListener(loginAccount);
        signUp.setOnClickListener(signUpAccount);

    }

    private final View.OnClickListener loginAccount = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            stringUsername = username.getText().toString();
            stringUserpassword = password.getText().toString();
            if(stringUsername.equals(userModel.getName()))
            {
                if(userModel.getPassword().equals(stringUserpassword))
                {
                    Intent homepage  = new Intent(Prototype.this, HomePage.class);
                    homepage.putExtra("userName", stringUsername);
                    startActivity(homepage);
                }
                else
                {
                    username.setError("Incorrect Password");
                }
            }
            else
            {
                username.setError("Incorrect Username");
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


}