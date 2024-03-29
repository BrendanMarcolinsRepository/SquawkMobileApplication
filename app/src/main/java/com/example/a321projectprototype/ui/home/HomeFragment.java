package com.example.a321projectprototype.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;


import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.LoginPackage.Prototype;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserModel;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsFragment;
import com.example.a321projectprototype.ui.Record.RecordFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private Button recordButton, discoverButton,rewardButton;
    private TextView greetings;
    private HomePage homePage;
    private String username;
    private NavController navController;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private UserModel userModel;
    private Button logout;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        homePage = (HomePage) getActivity();


        navController = homePage.getNav();




        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        progressBar = root.findViewById(R.id.usernameHomepageProgressBar);


        recordButton = root.findViewById(R.id.homeButton1);
        recordButton.setOnClickListener(record);

        discoverButton = root.findViewById(R.id.homeButton2);
        discoverButton.setOnClickListener(discover);

        rewardButton = root.findViewById(R.id.homeButton3);
        rewardButton.setOnClickListener(reward);


        greetings = root.findViewById(R.id.homeTextView);


        getUserInformation();







        System.out.println("jefesf");
        return root;
    }

    private final View.OnClickListener record = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Home_to_nav_Record);
        }
    };

    private final View.OnClickListener discover = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Home_to_nav_Discover);
        }
    };

    private final View.OnClickListener reward = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Home_to_nav_Reward);
        }
    };

    public void getUserInformation() {
        progressBar.setVisibility(View.VISIBLE);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = auth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        greetings.setText("Welcome: " + document.getString("username"));

                    } else {
                        System.out.println("no document");
                    }
                } else {
                    System.out.println("not successfull");

                }
            }
        });
    }




}