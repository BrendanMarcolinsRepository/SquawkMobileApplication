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
import androidx.appcompat.widget.Toolbar;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    private EditText emailEditText;
    private Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        getSupportActionBar().hide();

        emailEditText = findViewById(R.id.currentEmailEdittext);
        confirmPasswordChange = findViewById(R.id.confirmRestPasswordButton);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBarResetPassword);
        progressBar.setVisibility(View.INVISIBLE);

        confirmPasswordChange.setOnClickListener(confirmResetPassword);

        toolbar.setOnClickListener(returnToolBar);

    }

    private final View.OnClickListener confirmResetPassword = v -> updatePassword();

    private final View.OnClickListener returnToolBar = v -> finish();

    private void updatePassword() {
        email = emailEditText.getText().toString();

        if(checkEmail(email)) {
            header = findViewById(R.id.resetTextviewHeader);
            header.setVisibility(View.INVISIBLE);
            emailEditText.setVisibility(View.INVISIBLE);
            confirmPasswordChange.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            sendEmailToUpdatePassword(email);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

    }

    private void sendEmailToUpdatePassword(String email)
    {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            } else {

                progressBar.setVisibility(View.INVISIBLE);
                header.setVisibility(View.VISIBLE);
                emailEditText.setVisibility(View.VISIBLE);
                confirmPasswordChange.setVisibility(View.VISIBLE);
                emailEditText.setError("Email Incorrect Or Account Does Not Exist");
                emailEditText.requestFocus();
                Toast.makeText(ResetPassword.this,"Email Incorrect Or Account Does Not Exist",Toast.LENGTH_SHORT).show();
            }

            if(task.isCanceled()){
                finish();
            }
        });
    }

    private boolean checkEmail(String email) {

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if(email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;

        }
        return true;
    }
}
