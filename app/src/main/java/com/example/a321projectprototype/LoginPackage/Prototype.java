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
import com.google.api.Backend;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Coding Done By Brendan Marcolin

public class Prototype extends AppCompatActivity {
    private Button login;
    private TextView email, password, signUp, passwordRest,textView;
    private ImageView logoImage;
    private String stringUserpassword, stringEmail;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    @Override
    //checks if user is already logged in
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //checks if current user exit
       if(firebaseUser != null && firebaseUser.isEmailVerified()){
           //if that their email is verified
           if(firebaseUser.isEmailVerified()){

               //started home activity
               Intent homepage  = new Intent(Prototype.this, HomePage.class);
               homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(homepage);
               finish();
           }
       }
    }

    //button input to login user
    private final View.OnClickListener loginAccount = v -> loginMethod();

    private void loginMethod() {
        stringEmail = email.getText().toString();
        stringUserpassword = password.getText().toString();

        if(checkUserInput(stringEmail,stringUserpassword)) {
            invisableSetOne();
            processingLogin();
        } else {
            return;
        }
    }

    //process to log user in
    public void processingLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(stringEmail,stringUserpassword).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //check if email is verified before login user in for the first time
                if(user.isEmailVerified()){

                    Intent homepage  = new Intent(Prototype.this, HomePage.class);
                    homepage.putExtra("password",stringUserpassword);
                    homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homepage);
                    finish();
                }
                else {
                    invisableSetTwo();
                    Toast.makeText(Prototype.this,"Please Verify Your Email", Toast.LENGTH_LONG).show();
                    user.sendEmailVerification();
                }
            } else
            {
                invisableSetTwo();
                Toast.makeText(Prototype.this,"Failed to Login, Please Try Again! ", Toast.LENGTH_LONG).show();


            }

        });
    }

    //Intent Navigator
    private final View.OnClickListener signUpAccount = v -> {
        Intent register = new Intent(Prototype.this, SignUp.class);
        startActivity(register);

    };

    private final View.OnClickListener resetPassword = v -> {
        Intent register = new Intent(Prototype.this, ResetPassword.class);
        startActivity(register);

    };


// checks user input
    private boolean checkUserInput(String emailString, String passwordString) {

        //if an error an output will be displayed
        if (!Patterns.EMAIL_ADDRESS.matcher(emailString).matches() || emailString.isEmpty()) {
            email.setError("Email Is Incorrect");
            email.requestFocus();
            return false;
        }

        if(passwordString.isEmpty() || passwordString.length() < 6) {
            password.setError("Password Is Incorrect");
            password.requestFocus();
            return false;
        }

        return true;
    }

    //shows what the user can see depending on the current proccess at the time
    private void invisableSetOne() {
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        signUp.setVisibility(View.INVISIBLE);
        login.setVisibility(View.INVISIBLE);
        passwordRest.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        logoImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void invisableSetTwo() {
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        passwordRest.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        logoImage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }




}