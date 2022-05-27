package com.example.a321projectprototype.ui.Flock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.FlockScoreModel;
import com.example.a321projectprototype.User.UserModel;
import com.example.a321projectprototype.User.UserScore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
//Coding Done By Brendan Marcolin
public class FlockInfoFragment extends Fragment
{

    private AdapaterMemberFlock adapaterMemberFlock;
    private RecyclerView recyclerView;
    private Button join,invite;
    private TextView groupName, groupScore;
    private ImageView flockImage,settings;
    private int currentTotalScore = 0;
    private String name;
    private HomePage homePage;
    private NavController navController;
    private List<String> userIds;
    private List<UserModel> userModelList;
    private List<UserScore> userScoreList;
    private FirebaseFirestore firebaseFirestore;
    private FlockModelData flockModelData;
    private ProgressBar progressBar;
    private UserModel userModel;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragement_flock_info, container, false);

        flockModelData = (FlockModelData) getArguments().getSerializable("flock");



        homePage = (HomePage) getActivity();
        navController = homePage.getNav();


        groupName = root.findViewById(R.id.flockInfoGroupName);
        flockImage = root.findViewById(R.id.flock_group_info_picture);
        recyclerView = root.findViewById(R.id.recycleFlockInfo);
        join = root.findViewById(R.id.flock_info_join);
        groupScore = root.findViewById(R.id.flock_info_score);
        groupScore.setVisibility(View.INVISIBLE);
        settings = root.findViewById(R.id.flockInfoSettingsImageView);
        invite = root.findViewById(R.id.flock_info_invite);
        progressBar = root.findViewById(R.id.flockMemebersListProgressBar);
        progressBar.setVisibility(View.VISIBLE);



        loadUsersFlock();
        checkFlockName();
        loadData();

        join.setOnClickListener(joinInfoButtonMethod);
        settings.setOnClickListener(settingsInfoMethod);
        invite.setOnClickListener(inviteInfoMethod);

        return root;
    }

    private void loadUsersFlock() {
        groupName.setText(flockModelData.getName());


        if(flockModelData.getUserId().equals(auth.getUid())){
            settings.setVisibility(View.VISIBLE);

        }else{
            settings.setImageResource(0);
            settings.setEnabled(false);

        }

        Glide.with(homePage.getApplicationContext())
                .load(flockModelData.getImageUrl())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_profile)
                .into(flockImage);

    }

    //loads the flock score and flock members from the firebase
    public void loadData() {
        userIds = new ArrayList<>();
        userModelList = new ArrayList<>();
        userScoreList  = new ArrayList<>();



        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("flockScore").document(flockModelData.getFlockId())
                .get()
                .addOnCompleteListener(task -> {
                            String result = task.getResult().get("totalScore").toString();
                    groupScore.setText("Total Points: "+ result);
                });



        firebaseFirestore.collection("flockMembers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(flockModelData.getFlockId().equals(document.get("flockId").toString())) {
                                userIds.add(document.get("userId").toString());
                            }

                        }
                        loadOtherData();
                    }
        });

    }

    //loads users information from that particular flock
    private void loadOtherData() {
        if(userIds != null) {
            firebaseFirestore.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            if(userIds.contains(documentSnapshot.get("userId").toString())){
                                userModel = documentSnapshot.toObject(UserModel.class);
                                userModelList.add(userModel);

                            }
                        }

                        setRecycleVeiw(userModelList);

                    });
        }
    }



    //sets the recycle view
    private void setRecycleVeiw(List<UserModel> userModel) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAlpha(0);

        adapaterMemberFlock = new  AdapaterMemberFlock(userModel,getContext(),flockModelData,userIds.size(), recyclerView, progressBar,groupScore);
        recyclerView.setAdapter(adapaterMemberFlock);
    }

    //Navigators
    private final View.OnClickListener joinInfoButtonMethod = v -> invitePopUpWindow(v);

    private final View.OnClickListener settingsInfoMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Create);

        }
    };

    //Pop up button
    private final View.OnClickListener inviteInfoMethod = v -> onButtonShowPopupWindowClick(v);

    //pop up button for user wanting to join a flock
    private void onButtonShowPopupWindowClick(View view) {


        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.flock_popup_accept,null);
        alert.setView(mView);

        Button yes  = (Button) mView.findViewById(R.id.flockFilterButtonYes);
        Button no = (Button) mView.findViewById(R.id.flockFilterButtonNo);



        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapaterMemberFlock.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });
        no.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    //invite pop for user in a group to invite another user
    private void invitePopUpWindow(View view) {


        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.flock_popup_accept,null);
        alert.setView(mView);

        Button yes  = (Button) mView.findViewById(R.id.flockFilterButtonYes);
        Button no = (Button) mView.findViewById(R.id.flockFilterButtonNo);



        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        yes.setOnClickListener(v -> {
            adapaterMemberFlock.notifyDataSetChanged();
            alertDialog.dismiss();
        });


        no.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.show();
    }

    //check to see if a user is in a flock
    private void checkFlockName() {
        FlockModelData flockModelData = homePage.getFlockModelData();

        if(flockModelData != null) {
            settings.setVisibility(View.VISIBLE);
            invite.setVisibility(View.VISIBLE);
            join.setVisibility(View.GONE);

            join.setOnClickListener(null);

        } else {
            settings.setVisibility(View.GONE);
            invite.setVisibility(View.GONE);
            join.setVisibility(View.VISIBLE);

            settings.setOnClickListener(null);
            invite.setOnClickListener(null);
        }
    }
}
