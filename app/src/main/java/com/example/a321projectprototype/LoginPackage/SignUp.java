package com.example.a321projectprototype.LoginPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private String fullNameInput, usernameInput, emailInput, password1Input, password2Input, userID;
    private int id;
    private EditText fullNameEditText, usernameEditText, emailEditText, password1EditText, password2EditText;
    private Button register;
    private TextView returnText, registerText, registerAccountAlready;
    private UserModel user;
    private ConstraintLayout registerLayout;
    private UserDatabase userDatabase;
    private int amountOfUsers;
    private ArrayList<UserModel> usersArrayList;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String userFirebaseId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private final String USERS_KEY = "qbPjDm73CVIUz63gDu8D";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        System.out.println(amountOfUsers);





        fullNameEditText = findViewById(R.id.fullNameRegister);
        usernameEditText = findViewById(R.id.usernameRegister);
        emailEditText = findViewById(R.id.emailRegister);
        password1EditText = findViewById(R.id.passwordOneRegister);
        password2EditText = findViewById(R.id.passwordTwoRegister);
        register = findViewById(R.id.registerButton);
        registerLayout = findViewById(R.id.registerlayer);
        returnText = findViewById(R.id.registerSignIn);
        registerText = findViewById(R.id.registerText);
        registerAccountAlready = findViewById(R.id.registerAccountAlready);
        progressBar = findViewById(R.id.signUpProgressBar);
        progressBar.setVisibility(View.INVISIBLE);


        returnText.setOnClickListener(returnMethod);
        register.setOnClickListener(registerMethod);

    }

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
            if (password2Input.isEmpty()) {
                password2EditText.setError("Password is required");
                password2EditText.requestFocus();
                setVisiable();
                setProgress();

            }

            if (password1Input.matches(password2Input) && !password1Input.isEmpty())
            {

                auth = FirebaseAuth.getInstance();
                firebaseFirestore = FirebaseFirestore.getInstance();

                auth.createUserWithEmailAndPassword(emailInput,password2Input).addOnCompleteListener((task -> {



                    if(task.isSuccessful())
                    {

                        userFirebaseId = auth.getCurrentUser().getUid();
                        DocumentReference documentReference = firebaseFirestore.collection("users").document(userFirebaseId);
                        HashMap<String,Object> userMap = new HashMap<>();
                        userMap.put("fullname",fullNameInput);
                        userMap.put("username",usernameInput);
                        userMap.put("password",password1Input);
                        userMap.put("email", emailInput);
                        documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                System.out.println("worked");
                            }
                        });

                        returnToLogin();
                    }
                    else
                    {
                        setVisiable();
                        setProgress();
                        emailEditText.setError("Your Email Is Already In Use");
                        emailEditText.requestFocus();

                    }
                }));
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

    private void setProgress()
    {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setInvisiable()
    {
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

    private void setVisiable()
    {
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