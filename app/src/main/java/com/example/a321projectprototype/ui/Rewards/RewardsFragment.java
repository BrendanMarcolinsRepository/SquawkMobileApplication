package com.example.a321projectprototype.ui.Rewards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Flock.FlockViewModel;

public class RewardsFragment extends Fragment
{
    private RewardsViewModel rewardsViewModel;
    private CardView compareButton;
    private CardView achievementButton;
    private NavController navController;
    private HomePage homePage;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rewardsViewModel = new ViewModelProvider(this).get(RewardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        compareButton = root.findViewById(R.id.reward_compare_button);
        achievementButton = root.findViewById(R.id.reward_achievement_button);

        compareButton.setOnClickListener(rewardCompare);
        achievementButton.setOnClickListener(rewardAchievement);

        return root;
    }
    // Navigations
    private final View.OnClickListener rewardCompare = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Reward_to_rewardCompareFragment);
        }
    };

    private final View.OnClickListener rewardAchievement  = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Reward_to_rewardAchievementFragment);
        }
    };
}
