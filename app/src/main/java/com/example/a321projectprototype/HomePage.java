package com.example.a321projectprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a321projectprototype.ui.home.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomePage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;
    private String name;
    private ImageView imageView;
    private TextView headName, headEmail;
    private Button recordButton, discoverButton,rewardButton;
    private  NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent homeIntent = getIntent();
        name = homeIntent.getStringExtra("userName");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        System.out.println(name.toString());

        recordButton = findViewById(R.id.homeButton1);
        recordButton.setOnClickListener(record);

        discoverButton = findViewById(R.id.homeButton2);
        discoverButton.setOnClickListener(discover);

        rewardButton = findViewById(R.id.homeButton3);
        rewardButton.setOnClickListener(reward);


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
        headName = (TextView) header.findViewById(R.id.navhead_name);
        headEmail = (TextView) header.findViewById(R.id.navhead_email);
        headName.setText(name);
        headEmail.setText("m@gmail.com");
    }




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public String getName() {
        return name;
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




}