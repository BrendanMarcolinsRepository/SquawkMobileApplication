package com.example.a321projectprototype.LoginPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.material.snackbar.Snackbar;

public class SignUp extends AppCompatActivity {
    private String fullNameInput, usernameInput, emailInput, password1Input, password2Input;
    private EditText fullNameEditText, usernameEditText, emailEditText, password1EditText, password2EditText;
    private Button register;
    private TextView returnText;
    private UserModel user;
    private ConstraintLayout registerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();



        fullNameEditText = findViewById(R.id.fullNameRegister);
        usernameEditText = findViewById(R.id.usernameRegister);
        emailEditText = findViewById(R.id.emailRegister);
        password1EditText = findViewById(R.id.passwordOneRegister);
        password2EditText = findViewById(R.id.passwordTwoRegister);
        register = findViewById(R.id.registerButton);
        registerLayout = findViewById(R.id.registerlayer);
        returnText = findViewById(R.id.registerSignIn);

        returnText.setOnClickListener(returnMethod);
        register.setOnClickListener(registerMethod);

    }

    private final View.OnClickListener registerMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            fullNameInput = fullNameEditText.getText().toString();
            usernameInput = usernameEditText.getText().toString();
            emailInput = emailEditText.getText().toString();
            password1Input = password1EditText.getText().toString();
            password2Input = password2EditText.getText().toString();


            if (fullNameInput.isEmpty()) {
                fullNameEditText.setError("First name is requried");
                fullNameEditText.requestFocus();
            }

            if (usernameInput.isEmpty()) {
                usernameEditText.setError("Last name is requried");
                usernameEditText.requestFocus();
            }

            if (emailInput.isEmpty()) {
                emailEditText.setError("Email is requried");
                emailEditText.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
            }

            if (password1Input.isEmpty()) {
                password1EditText.setError("Password is required");
                password1EditText.requestFocus();
            }
            if (password2Input.isEmpty()) {
                password2EditText.setError("Password is required");
                password2EditText.requestFocus();

            }

            if(password1Input.matches(password2Input))
            {
                password1EditText.setError("Please Enter the same password!");
                password1EditText.requestFocus();
                password2EditText.setError("Please Enter the same password!");
                password2EditText.requestFocus();
            }
            else {

                user = new UserModel(2, usernameInput, password1Input, emailInput);
                Snackbar.make(registerLayout, "Contact Has Been Saved ", Snackbar.LENGTH_LONG).show();
                returnToLogin();
            }

        }

    };

    private final View.OnClickListener returnMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            returnToLogin();
        }

    };

    private void returnToLogin()
    {
        finish();
    }
}