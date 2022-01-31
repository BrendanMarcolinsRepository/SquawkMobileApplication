package com.example.a321projectprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a321projectprototype.LoginPackage.Prototype;
import com.example.a321projectprototype.User.UserModel;
import com.example.a321projectprototype.ui.home.HomeViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;

public class HomePage extends AppCompatActivity implements Serializable
{

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;
    private String name;
    private ImageView imageView;
    private TextView headName, headEmail;
    private Button recordButton, discoverButton,rewardButton;
    private  NavController navController;
    private UserModel userModel;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);





        recordButton = findViewById(R.id.homeButton1);
        recordButton.setOnClickListener(record);

        discoverButton = findViewById(R.id.homeButton2);
        discoverButton.setOnClickListener(discover);

        rewardButton = findViewById(R.id.homeButton3);
        rewardButton.setOnClickListener(reward);

        logout = findViewById(R.id.buttonLogout);
        logout.setOnClickListener(logoutMethod);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = firebaseUser.getUid();




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_Record, R.id.nav_PastRecording,R.id.nav_Discover,R.id.nav_Forum,
                R.id.nav_Flock,R.id.nav_Reward,R.id.nav_Settings)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View header = navigationView.getHeaderView(0);
        //use to display the users details in the navigation header


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);

                if(userModel != null)
                {
                    headName = (TextView) header.findViewById(R.id.navhead_name);
                    headEmail = (TextView) header.findViewById(R.id.navhead_email);
                    headName.setText(userModel.getUsername());
                    headEmail.setText(userModel.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                System.out.println("=======================> did cancled");
            }


        });


    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getName() {
        return userModel.getUsername();
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


    public NavController getNav(){return navController;}

    public UserModel getUserModel()
    {
        return userModel;
    }

    private final View.OnClickListener logoutMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomePage.this, Prototype.class));
        }
    };


}