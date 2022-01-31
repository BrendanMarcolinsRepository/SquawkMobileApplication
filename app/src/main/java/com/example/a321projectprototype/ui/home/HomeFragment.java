package com.example.a321projectprototype.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        homePage = (HomePage) getActivity();


        navController = homePage.getNav();



        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        recordButton = root.findViewById(R.id.homeButton1);
        recordButton.setOnClickListener(record);

        discoverButton = root.findViewById(R.id.homeButton2);
        discoverButton.setOnClickListener(discover);

        rewardButton = root.findViewById(R.id.homeButton3);
        rewardButton.setOnClickListener(reward);



        greetings = root.findViewById(R.id.homeTextView);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = firebaseUser.getUid();


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);

                if(userModel != null)
                {
                    greetings.setText(userModel.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                System.out.println("=======================> did cancled");
            }


        });







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




}