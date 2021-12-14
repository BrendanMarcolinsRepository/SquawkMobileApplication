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
import com.example.a321projectprototype.User.UserModel;
import com.google.android.material.snackbar.Snackbar;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragement_flock_info, container, false);
//        System.out.println(getArguments().getInt("Position"));

        homePage = (HomePage) getActivity();
        navController = homePage.getNav();


        recyclerView = root.findViewById(R.id.recycleFlockInfo);
        join = root.findViewById(R.id.flock_info_join);
        groupName = root.findViewById(R.id.flock_groupname_info_textview);
        groupScore = root.findViewById(R.id.flock_info_score);
        settings = root.findViewById(R.id.flockInfoSettings);
        invite = root.findViewById(R.id.flock_info_invite);

        checkFlockName();


        flockMembersModelList = new ArrayList<>();
        loadData();

        setRecycleVeiw(flockMembersModelList);

        join.setOnClickListener(joinInfoButtonMethod);
        settings.setOnClickListener(settingsInfoMethod);
        invite.setOnClickListener(inviteInfoMethod);

        groupName.setText(name);
        currentTotalScore = getFlocksCurrentScore();
        groupScore.setText("Current Flocks Points: " + Integer.toString(currentTotalScore));




        return root;
    }

    public void loadData()
    {
        FlockMembersModel flockMembersModel1 = new FlockMembersModel("Sydney Flockers", "Neil", 150);
        FlockMembersModel flockMembersModel2 = new FlockMembersModel("Sydney Flockers", "Kim", 200);
        FlockMembersModel flockMembersModel3 = new FlockMembersModel("Sydney Flockers", "Ray", 230);
        FlockMembersModel flockMembersModel4 = new FlockMembersModel("Sydney Flockers", "Lachlan", 300);
        FlockMembersModel flockMembersModel5 = new FlockMembersModel("Sydney Flockers", "Shelby", 130);

        name = flockMembersModel1.getGroupName();
        flockMembersModelList.add(flockMembersModel1);
        flockMembersModelList.add(flockMembersModel2);
        flockMembersModelList.add(flockMembersModel3);
        flockMembersModelList.add(flockMembersModel4);
        flockMembersModelList.add(flockMembersModel5);
    }

    private int getFlocksCurrentScore()
    {
        int score = 0;

        for(FlockMembersModel f : flockMembersModelList)
        {
            score = score + f.getScore();
        }

        return score;
    }

    private void setRecycleVeiw(List<FlockMembersModel> members)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapaterMemberFlock = new  AdapaterMemberFlock(members,getContext());
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
                FlockMembersModel flockMembersModel1 = new FlockMembersModel("Sydney Flockers", "Brendan", 150);
                flockMembersModelList.add(flockMembersModel1);

                adapaterMemberFlock.notifyDataSetChanged();
                currentTotalScore = getFlocksCurrentScore();
                groupScore.setText("Current Flocks Points: "  + Integer.toString(currentTotalScore));

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
                FlockMembersModel flockMembersModel1 = new FlockMembersModel("Sydney Flockers", "Brendan", 150);
                flockMembersModelList.add(flockMembersModel1);

                adapaterMemberFlock.notifyDataSetChanged();
                currentTotalScore = getFlocksCurrentScore();
                groupScore.setText("Current Flocks Points: "  + Integer.toString(currentTotalScore));

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
        UserModel userModel = homePage.getUserModel();

        if(userModel.getUserFlock() != null)
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
