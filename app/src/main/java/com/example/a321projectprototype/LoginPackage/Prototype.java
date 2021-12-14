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


import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Prototype extends AppCompatActivity
{
    private Button login;
    private TextView username, password, signUp;
    private String stringUserpassword, stringUsername;
    private UserDatabase userDatabase;
    private UserModel userModel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        getSupportActionBar().hide();


        userDatabase = new UserDatabase(this);
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

            userModel = userDatabase.getUser(stringUsername);
           // System.out.println("username :" + userModel.getUsername() + "\n  password :" + userModel.getPassword());

           if(userModel != null)
           {
               if(userDatabase.checkUser(userModel.getUsername(),userModel.getPassword()))
               {

                   System.out.println("worked");
                   Intent homepage  = new Intent(Prototype.this, HomePage.class);
                   Bundle b = new Bundle();
                   b.putSerializable("serialzable", (Serializable) userModel);
                   homepage.putExtras(b);
                   startActivity(homepage);

               }
               else
               {
                   System.out.println("didntwork");
               }
           }
           else
           {
               username.setError("Incorrect Username");
               password.setError("Incorrect Password");
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