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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.FlockScoreModel;
import com.example.a321projectprototype.User.UserModel;
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

public class FlockInfoFragment extends Fragment
{
    private FlockMembersModel flockMembersModel;
    private List<FlockMembersModel> flockMembersModelList;
    private AdapaterMemberFlock adapaterMemberFlock;
    private RecyclerView recyclerView;
    private Button join,settings,invite;
    private TextView groupName, groupScore;
    private int currentTotalScore = 0;
    private String name;
    private HomePage homePage;
    private NavController navController;
    private List<String> userIds;
    private List<UserModel> userModelList;
    private FirebaseFirestore firebaseFirestore;
    private FlockModelData flockModelData;
    private ProgressBar progressBar;
    private UserModel userModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragement_flock_info, container, false);

        flockModelData = (FlockModelData) getArguments().getSerializable("flock");



        homePage = (HomePage) getActivity();
        navController = homePage.getNav();


        groupName = root.findViewById(R.id.flockInfoGroupName);
        groupName.setText(flockModelData.getName());

        recyclerView = root.findViewById(R.id.recycleFlockInfo);
        join = root.findViewById(R.id.flock_info_join);
        groupScore = root.findViewById(R.id.flock_info_score);
        settings = root.findViewById(R.id.flockInfoSettings);
        invite = root.findViewById(R.id.flock_info_invite);
        progressBar = root.findViewById(R.id.flockMemebersListProgressBar);
        progressBar.setVisibility(View.VISIBLE);


        checkFlockName();



        loadData();



        join.setOnClickListener(joinInfoButtonMethod);
        settings.setOnClickListener(settingsInfoMethod);
        invite.setOnClickListener(inviteInfoMethod);



       // groupScore.setText("Current Flocks Points: " + Integer.toString(currentTotalScore));




        return root;
    }

    public void loadData()
    {
        userIds = new ArrayList<>();
        userModelList = new ArrayList<>();
        //assert getArguments() != null;


        System.out.println("=========== Flock :" + flockModelData.getName());

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("flockMembers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if(flockModelData.getFlockId().equals(document.get("flockId").toString())) {
                        System.out.println("User Here ==========================> worked 1" + document.get("userId").toString());
                        userIds.add(document.get("userId").toString());
                    }

                }
                loadOtherData();
            }
        });








    }

    private void loadOtherData()
    {
        if(userIds != null)
        {
            firebaseFirestore.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            if(userIds.contains(documentSnapshot.get("userId").toString())){
                                userModel = documentSnapshot.toObject(UserModel.class);
                                System.out.println("User Here ==========================> " + userModel.getUsername());
                                userModelList.add(userModel);

                            }
                        }

                        setRecycleVeiw(userModelList);
                    });
            progressBar.setVisibility(View.INVISIBLE);

        }
    }



    private void setRecycleVeiw(List<UserModel> userModel)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapaterMemberFlock = new  AdapaterMemberFlock(userModel,getContext());
        recyclerView.setAdapter(adapaterMemberFlock);
    }

    private final View.OnClickListener joinInfoButtonMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            invitePopUpWindow(v);

        }
    };

    private final View.OnClickListener settingsInfoMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.nav_Flock_Create);

        }
    };

    private final View.OnClickListener inviteInfoMethod = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            onButtonShowPopupWindowClick(v);

        }
    };

    private void onButtonShowPopupWindowClick(View view)
    {


        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.flock_popup_accept,null);
        alert.setView(mView);

        Button yes  = (Button) mView.findViewById(R.id.flockFilterButtonYes);
        Button no = (Button) mView.findViewById(R.id.flockFilterButtonNo);



        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // FlockMembersModel flockMembersModel1 = new FlockMembersModel("Sydney Flockers", "Brendan", 150);
                //flockMembersModelList.add(flockMembersModel1);

                adapaterMemberFlock.notifyDataSetChanged();

                //groupScore.setText("Current Flocks Points: "  + Integer.toString(currentTotalScore));

                alertDialog.dismiss();
            }
        });


        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void invitePopUpWindow(View view)
    {


        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.flock_popup_accept,null);
        alert.setView(mView);

        Button yes  = (Button) mView.findViewById(R.id.flockFilterButtonYes);
        Button no = (Button) mView.findViewById(R.id.flockFilterButtonNo);



        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // FlockMembersModel flockMembersModel1 = new FlockMembersModel("Sydney Flockers", "Brendan", 150);
                //flockMembersModelList.add(flockMembersModel1);

                adapaterMemberFlock.notifyDataSetChanged();

               // groupScore.setText("Current Flocks Points: "  + Integer.toString(currentTotalScore));

                alertDialog.dismiss();
            }
        });


        no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void checkFlockName()
    {
        FlockModelData flockModelData = homePage.getFlockModelData();

        if(flockModelData != null)
        {
            settings.setVisibility(View.VISIBLE);
            invite.setVisibility(View.VISIBLE);
            join.setVisibility(View.GONE);

            join.setOnClickListener(null);

        }
        else
        {
            settings.setVisibility(View.GONE);
            invite.setVisibility(View.GONE);
            join.setVisibility(View.VISIBLE);

            settings.setOnClickListener(null);
            invite.setOnClickListener(null);
        }
    }


}
