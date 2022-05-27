package com.example.a321projectprototype.ui.Flock;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdRewardModel;
import com.example.a321projectprototype.User.FlockModelData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FlockFragment extends Fragment
{
    private FlockModelData flockModelData;
    private FlockViewModel flockViewModel;
    private SearchView flockSearchView;
    private RecyclerView recyclerView;
    private List<FlockModelData> flockList;
    private AdapterFlock adapterFlock;
    private ConstraintLayout constraintLayout;
    private Button  createFlockButton,myflock,leaderboard;
    private TextView textViewFlockName;
    private ImageView flockImage;
    private View root;
    private HomePage homePage;
    private NavController navController;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private ImageView imageView;




    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        flockViewModel = new ViewModelProvider(this).get(FlockViewModel.class);
        root = inflater.inflate(R.layout.fragment_flock, container, false);

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        DrawerLayout drawerLayout = homePage.getDrawer();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        constraintLayout = root.findViewById(R.id.registerlayerFlock);
        constraintLayout.setVisibility(View.INVISIBLE);
        textViewFlockName = root.findViewById(R.id.text_flocks);
        flockSearchView = root.findViewById(R.id.flock_search_bar);
        recyclerView = root.findViewById(R.id.recycleFlock);
        createFlockButton = root.findViewById(R.id.flockCreateButton);
        myflock = root.findViewById(R.id.flockMyFlock);
        flockImage = root.findViewById(R.id.flockImage);
        leaderboard = root.findViewById(R.id.flockLeaderBoards);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = root.findViewById(R.id.flockListProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        imageView = root.findViewById(R.id.flockImage);
        flockList = new ArrayList<>();



        checkFlockName();





        EventChangeListener();
        createFlockButton.setOnClickListener(createFlockFragement);
        leaderboard.setOnClickListener(flockLeaderboardMethod);
        myflock.setOnClickListener(myFlockMethod);


        flockSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                for(int i = 0; i < flockList.size(); i++){
                    if(flockList.get(i).getName().contains(query)) {
                        adapterFlock.getFilter().filter(query);
                    } else {
                        Toast.makeText(getContext(), "No Match found",Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapterFlock.getFilter().filter(newText);
                return false;
            }
        });


        return root;
    }


    //gets all the flock groups
    private void EventChangeListener()
    {
        firebaseFirestore.collection("flocks")
                .orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    for(DocumentChange documentChange : value.getDocumentChanges()) {

                        if(documentChange.getType() == DocumentChange.Type.ADDED) {
                            progressBar.setVisibility(View.INVISIBLE);
                            flockList.add(documentChange.getDocument().toObject(FlockModelData.class));
                        }

                    }
                    setRecyclerView();
                });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    //checks to see if user is in a flock
    private void checkFlockName() {
        String userID = auth.getCurrentUser().getUid();


        firebaseFirestore.collection("flockMembers")
                .whereEqualTo("userId",userID)
                .get()
                .addOnCompleteListener(task -> {
                    if(!task.getResult().isEmpty()){
                        constraintLayout.setVisibility(View.VISIBLE);
                        createFlockButton.setVisibility(View.GONE);
                        createFlockButton.setOnClickListener(null);
                        myflock.setVisibility(View.VISIBLE);
                        flockImage.setVisibility(View.VISIBLE);
                        System.out.println("===========> in a flock");

                        task.getResult().forEach(i -> getFlockName(i.get("flockId").toString()));

                    }else{

                        myflock.setVisibility(View.GONE);
                        flockImage.setVisibility(View.GONE);
                        createFlockButton.setVisibility(View.VISIBLE);
                        System.out.println("===========> not in a flock");
                        System.out.println("no document");
                        constraintLayout.setVisibility(View.VISIBLE);

                    }
                });
    }

    //gets the flock if a user is in a flock
    private void getFlockName(String flockId) {


        firebaseFirestore
                .collection("flocks")
                .whereEqualTo("flockId", flockId)
                .addSnapshotListener((value, error) -> {

                    System.out.println("Bird Section +++++++++++++++++++++++++++++++++++++");
                    if(error != null)
                    {
                        System.out.println("Error ==========?>" +  error);
                        return;
                    }
                    for(DocumentChange documentChange : value.getDocumentChanges())
                    {
                        if(documentChange.getType() == DocumentChange.Type.ADDED)
                        {

                            flockModelData = documentChange.getDocument().toObject(FlockModelData.class);
                            System.out.println("Image +++++++++++++++++++++++++++++++++++++ " + flockModelData.getImageUrl());
                            homePage.setFlockModelData(flockModelData);
                            textViewFlockName.setText(flockModelData.getName());
                            Glide.with(homePage.getApplicationContext())
                                    .load(flockModelData.getImageUrl())
                                    .circleCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.user_profile)
                                    .into(imageView);
                        }

                    }
                });

    }
    //Navigators
    private final View.OnClickListener createFlockFragement = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Create);
        }
    };

    private final View.OnClickListener flockLeaderboardMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Leaderboards);

        }
    };

    private final View.OnClickListener myFlockMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("flock", flockModelData);
            navController.navigate(R.id.nav_Flock_Info,bundle);

        }
    };

    //Sets the recycle view
    private void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapterFlock = new  AdapterFlock(flockList,homePage,getContext(), root);
        recyclerView.setAdapter(adapterFlock);
    }




}
