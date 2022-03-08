package com.example.a321projectprototype.LoginPackage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ResetPassword extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String email;
    private Button confirmPasswordChange;
    private TextView header;
    private EditText emailEditText, currentPasswordText, newPasswordText;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.currentEmailEdittext);
        confirmPasswordChange = findViewById(R.id.confirmRestPasswordButton);

        progressBar = findViewById(R.id.progressBarResetPassword);

        confirmPasswordChange.setOnClickListener(confirmResetPassword);

    }

    private final View.OnClickListener confirmResetPassword = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            updatePassword();
        }
    };

    private void updatePassword()
    {
        email = emailEditText.getText().toString();

        if(checkEmail(email))
        {
            header = findViewById(R.id.resetTextviewHeader);
            header.setVisibility(View.INVISIBLE);
            emailEditText.setVisibility(View.INVISIBLE);
            confirmPasswordChange.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            sendEmailToUpdatePassword(email);
        }
        else
        {
            return;
        }

    }

    private void sendEmailToUpdatePassword(String email)
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    finish();
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    header.setVisibility(View.VISIBLE);
                    emailEditText.setVisibility(View.VISIBLE);
                    confirmPasswordChange.setVisibility(View.VISIBLE);

                    Toast.makeText(ResetPassword.this,"There was an error that has occured,please try again",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean checkEmail(String email)
    {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if(email.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;

        }

        return true;
    }
}
