package com.example.a321projectprototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a321projectprototype.LoginPackage.Prototype;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.UserModel;
import com.example.a321projectprototype.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements Serializable
{

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;
    private TextView headName, headEmail;
    private Button recordButton, discoverButton,rewardButton;
    private  NavController navController;
    private UserModel userModel;
    private String userID;
    private Button logout;
    private DrawerLayout drawer;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private FlockModelData flockModelData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_home_page);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        checkUserSession();





        recordButton = findViewById(R.id.homeButton1);
        recordButton.setOnClickListener(record);

        discoverButton = findViewById(R.id.homeButton2);
        discoverButton.setOnClickListener(discover);

        rewardButton = findViewById(R.id.homeButton3);
        rewardButton.setOnClickListener(reward);

        logout = findViewById(R.id.buttonLogout);
        logout.setOnClickListener(logoutMethod);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_Record, R.id.nav_PastRecording, R.id.nav_Discover, R.id.nav_Forum,
                R.id.nav_Flock, R.id.nav_Reward, R.id.nav_Settings, R.id.nav_PastRecording_Online)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View header = navigationView.getHeaderView(0);
        headName = (TextView) header.findViewById(R.id.navhead_name);
        headEmail = (TextView) header.findViewById(R.id.navhead_email);

        //use to display the users details in the navigation header

        getUserInformation();


    }

    private void checkUserSession()
    {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = auth.getCurrentUser().getUid();
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

    private void getUserInformation()
    {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = auth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists())
                        {
                            userModel = new UserModel(document.getString("fullname"),document.getString("username"),document.getString("email"));

                            headName.setText(userModel.getUsername());
                            headEmail.setText(userModel.getEmail());

                        }
                        else
                        {
                            System.out.println("no document");
                        }

                    }
                });

                task.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed Here ===================> " + e);
                    }
                });
            }
        });


    }

    public void disableMenuItems()
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem item1 = menu.findItem(R.id.nav_home);
        item1.setEnabled(false);
        MenuItem item2 = menu.findItem(R.id.nav_Discover);
        item2.setEnabled(false);
        MenuItem item3 = menu.findItem(R.id.nav_Flock);
        item3.setEnabled(false);
        MenuItem item4 = menu.findItem(R.id.nav_Forum);
        item4.setEnabled(false);
        MenuItem item5 = menu.findItem(R.id.nav_PastRecording);
        item5.setEnabled(false);
        MenuItem item6 = menu.findItem(R.id.nav_Record);
        item6.setEnabled(false);
        MenuItem item7 = menu.findItem(R.id.nav_Reward);
        item7.setEnabled(false);
        MenuItem item8 = menu.findItem(R.id.nav_Settings);
        item8.setEnabled(false);

    }

    public void enableMenuItems()
    {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem item1 = menu.findItem(R.id.nav_home);
        item1.setEnabled(true);
        MenuItem item2 = menu.findItem(R.id.nav_Discover);
        item2.setEnabled(true);
        MenuItem item3 = menu.findItem(R.id.nav_Flock);
        item3.setEnabled(true);
        MenuItem item4 = menu.findItem(R.id.nav_Forum);
        item4.setEnabled(true);
        MenuItem item5 = menu.findItem(R.id.nav_PastRecording);
        item5.setEnabled(true);
        MenuItem item6 = menu.findItem(R.id.nav_Record);
        item6.setEnabled(true);
        MenuItem item7 = menu.findItem(R.id.nav_Reward);
        item7.setEnabled(true);
        MenuItem item8 = menu.findItem(R.id.nav_Settings);
        item8.setEnabled(true);

    }

    public void setFlockModelData(FlockModelData flockModelData)
    {
        this.flockModelData = flockModelData;
    }

    public FlockModelData getFlockModelData() {
        return flockModelData;
    }


}

