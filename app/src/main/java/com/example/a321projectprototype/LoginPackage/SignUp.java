package com.example.a321projectprototype.LoginPackage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a321projectprototype.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private String fullNameInput, usernameInput, emailInput, password1Input, password2Input;
    private EditText fullNameEditText, usernameEditText, emailEditText, password1EditText, password2EditText;
    private Button register;
    private TextView returnText, registerText, registerAccountAlready;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String userFirebaseId;
    private FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;


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
        returnText = findViewById(R.id.registerSignIn);
        registerText = findViewById(R.id.registerText);
        registerAccountAlready = findViewById(R.id.registerAccountAlready);
        toolbar = findViewById(R.id.toolbar);

        progressBar = findViewById(R.id.signUpProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        toolbar.setOnClickListener(returnToolBar);
        returnText.setOnClickListener(returnMethod);
        register.setOnClickListener(registerMethod);

    }


    //returns user back to the login page
    private final View.OnClickListener returnToolBar = v -> finish();

    //check register  logic
    private final View.OnClickListener registerMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            progressBar.setVisibility(View.VISIBLE);
            setInvisiable();
            fullNameInput = fullNameEditText.getText().toString();
            usernameInput = usernameEditText.getText().toString();
            emailInput = emailEditText.getText().toString();
            password1Input = password1EditText.getText().toString();
            password2Input = password2EditText.getText().toString();


            //check user input for any errors
            if (fullNameInput.isEmpty()) {
                fullNameEditText.setError("First name is requried");
                fullNameEditText.requestFocus();
                setVisiable();
                setProgress();
            }

            if (usernameInput.isEmpty()) {
                usernameEditText.setError("Last name is requried");
                usernameEditText.requestFocus();
                setVisiable();
                setProgress();
            }

            if (emailInput.isEmpty()) {
                emailEditText.setError("Email is requried");
                emailEditText.requestFocus();
                setVisiable();
                setProgress();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                setVisiable();
                setProgress();
            }

            if (password1Input.isEmpty()) {
                password1EditText.setError("Password is required");
                password1EditText.requestFocus();
                setVisiable();
                setProgress();
            }

            if (password1Input.length() < 7) {
                password1EditText.setError("Password is required");
                password1EditText.requestFocus();
                setVisiable();
                setProgress();
            }

            if (password2Input.isEmpty()) {
                password2EditText.setError("Password is required");
                password2EditText.requestFocus();
                setVisiable();
                setProgress();

            }

            //password is correct then process for creating a user starts
            if (password1Input.matches(password2Input) && !password1Input.isEmpty()) {

                auth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();

                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
                String dataString = ft.format(date);

                //creates a user with firebase authentication and store their data on firebase firestore database
                auth.createUserWithEmailAndPassword(emailInput,password2Input).addOnCompleteListener((task -> {
                    if(task.isSuccessful()) {

                        //user data firebase information
                        userFirebaseId = auth.getCurrentUser().getUid();
                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userFirebaseId);
                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("userId",userFirebaseId);
                        userMap.put("fullname",fullNameInput);
                        userMap.put("username",usernameInput);
                        userMap.put("password",password1Input);
                        userMap.put("email", emailInput);
                        userMap.put("photo_Url","");
                        userMap.put("created_at",dataString);
                        userMap.put("updated_at",dataString);
                        documentReference.set(userMap).addOnSuccessListener(aVoid -> {

                            //user score data firebase information
                            DocumentReference documentReference1 = firebaseFirestore.collection("userScore").document(userFirebaseId);
                            HashMap<String,Object> scoreMap = new HashMap<>();
                            scoreMap.put("scoreThisMonth",0);
                            scoreMap.put("scoreThisWeek",0);
                            scoreMap.put("scoreThisYear",0);
                            scoreMap.put("totalScore",0);
                            scoreMap.put("created_at",dataString);
                            scoreMap.put("updated_at",dataString);
                            scoreMap.put("userId", userFirebaseId);
                            scoreMap.put("username", usernameInput);
                            documentReference1.set(scoreMap).addOnSuccessListener(unused -> returnToLogin());


                        });
                    } else {
                        setVisiable();
                        setProgress();
                        emailEditText.setError("Your Email Is Already In Use");
                        emailEditText.requestFocus();

                    }

                    if(task.isCanceled()){
                        returnToLogin();
                    }

                })).addOnFailureListener(e -> {
                    setVisiable();
                    setProgress();
                    emailEditText.setError("Your Email Is Already In Use");
                    emailEditText.requestFocus();
                });
            }
        }
    };




    private final View.OnClickListener returnMethod = v -> returnToLogin();

    //returns user back to the login
    private void returnToLogin()
    {
        Intent login = new Intent(this, Prototype.class);
        startActivity(login);
    }

    private void setProgress()
    {
        progressBar.setVisibility(View.INVISIBLE);
    }

    //shows what UI is available during the current processes
    private void setInvisiable() {
        fullNameEditText.setVisibility(View.INVISIBLE);
        usernameEditText.setVisibility(View.INVISIBLE);
        emailEditText.setVisibility(View.INVISIBLE);
        password1EditText.setVisibility(View.INVISIBLE);
        password2EditText.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);
        returnText.setVisibility(View.INVISIBLE);
        registerAccountAlready.setVisibility(View.INVISIBLE);
        registerText.setVisibility(View.INVISIBLE);
    }

    private void setVisiable() {
        fullNameEditText.setVisibility(View.VISIBLE);
        usernameEditText.setVisibility(View.VISIBLE);
        emailEditText.setVisibility(View.VISIBLE);
        password1EditText.setVisibility(View.VISIBLE);
        password2EditText.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        returnText.setVisibility(View.VISIBLE);
        registerAccountAlready.setVisibility(View.VISIBLE);
        registerText.setVisibility(View.VISIBLE);
    }

}