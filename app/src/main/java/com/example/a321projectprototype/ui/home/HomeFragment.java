package com.example.a321projectprototype.ui.home;

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
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsFragment;
import com.example.a321projectprototype.ui.Record.RecordFragment;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private Button recordButton, discoverButton,rewardButton;
    private HomePage homePage;
    private String username;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        homePage = (HomePage) getActivity();
        username = homePage.getName();
        System.out.println(username +"grafB");

        navController = homePage.getNav();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.homeTextView);

        recordButton = root.findViewById(R.id.homeButton1);
        recordButton.setOnClickListener(record);

        discoverButton = root.findViewById(R.id.homeButton2);
        discoverButton.setOnClickListener(discover);

        rewardButton = root.findViewById(R.id.homeButton3);
        rewardButton.setOnClickListener(reward);
        homeViewModel.setName(username);



        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
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