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

import com.example.a321projectprototype.Database.UserDatabase;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    private String fullNameInput, usernameInput, emailInput, password1Input, password2Input;
    private EditText fullNameEditText, usernameEditText, emailEditText, password1EditText, password2EditText;
    private Button register;
    private TextView returnText;
    private UserModel user;
    private ConstraintLayout registerLayout;
    private UserDatabase userDatabase;
    private int amountOfUsers;
    private ArrayList<UserModel> usersArrayList;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        userDatabase = new UserDatabase(this);
        usersArrayList = userDatabase.getAllUsers();

        amountOfUsers = getUserCount(usersArrayList) + 2;

        System.out.println(amountOfUsers);

        auth = FirebaseAuth.getInstance();

        fullNameEditText = findViewById(R.id.fullNameRegister);
        usernameEditText = findViewById(R.id.usernameRegister);
        emailEditText = findViewById(R.id.emailRegister);
        password1EditText = findViewById(R.id.passwordOneRegister);
        password2EditText = findViewById(R.id.passwordTwoRegister);
        register = findViewById(R.id.registerButton);
        registerLayout = findViewById(R.id.registerlayer);
        returnText = findViewById(R.id.registerSignIn);
        progressBar = findViewById(R.id.signUpProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

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


            if (fullNameInput.isEmpty())
            {
                fullNameEditText.setError("First name is requried");
                fullNameEditText.requestFocus();
            }

            if (usernameInput.isEmpty())
            {
                usernameEditText.setError("Last name is requried");
                usernameEditText.requestFocus();
            }

            if (emailInput.isEmpty())
            {
                emailEditText.setError("Email is requried");
                emailEditText.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches())
            {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
            }

            if (password1Input.isEmpty())
            {
                password1EditText.setError("Password is required");
                password1EditText.requestFocus();
            }
            if (password2Input.isEmpty())
            {
                password2EditText.setError("Password is required");
                password2EditText.requestFocus();

            }

            if(password1Input.matches(password2Input) && !password1Input.isEmpty())
            {
                auth.createUserWithEmailAndPassword(emailInput,password2Input).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            user = new UserModel(amountOfUsers, fullNameInput, usernameInput, password1Input, emailInput);
                            FirebaseDatabase.getInstance().getReference("Users").child(auth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    Snackbar.make(registerLayout, "Contact Has Been Saved ", Snackbar.LENGTH_LONG).show();
                                    returnToLogin();
                                }
                            });

                        }
                    }
                });




            }
            else
            {

                password1EditText.setError("Please Enter the same password!");
                password1EditText.requestFocus();
                password2EditText.setError("Please Enter the same password!");
                password2EditText.requestFocus();
                System.out.println("noworked");

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

    private int getUserCount(ArrayList<UserModel> usersArrayList)
    {
        for(int i = 0; i <= usersArrayList.size();i++)
        {
            if(i == usersArrayList.size() && usersArrayList.size() > 0)
            {
                return usersArrayList.get(i - 1).getId() + 1;
            }
            else
            {
                return  1;
            }
        }

        return 0;
    }
}