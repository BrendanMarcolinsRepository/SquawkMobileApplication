package com.example.a321projectprototype.ui.Rewards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RewardsFragment extends Fragment
{
    private RewardsViewModel rewardsViewModel;
    private CardView compareButton;
    private CardView achievementButton;
    private NavController navController;
    private HomePage homePage;
    private Spinner rewardSpinner;
    private ArrayList<String> times;
    private ArrayAdapter<String> adaptor;
    private int allTimeScore;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rewardsViewModel = new ViewModelProvider(this).get(RewardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        compareButton = root.findViewById(R.id.reward_compare_button);
        achievementButton = root.findViewById(R.id.reward_achievement_button);

        compareButton.setOnClickListener(rewardCompare);
        achievementButton.setOnClickListener(rewardAchievement);

        rewardSpinner = root.findViewById(R.id.reward_compare_spinner);
        times = new ArrayList<>();
        times.add("Today");
        times.add("This Week");
        times.add("This Month");
        times.add("Last Week");
        times.add("This Year");
        adaptor = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, times);
        rewardSpinner.setAdapter(adaptor);

        allTimeScore = 0;

        setAllTimeScore();
        Log.d("AllTimeScore", String.valueOf(allTimeScore));
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

    private void setAllTimeScore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("identified_bird").orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("recorded_by", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("AllTimeScore", (String)document.get("bird_name"));
                            allTimeScore += getRewardPoint(getBirdStatus((String)document.get("bird_name")));
                        }
                    }
                });
    }

    private String getBirdStatus(String birdName) {
        String[] birdStatus = {""};
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("bird")
                .orderBy("bird_name", Query.Direction.ASCENDING)
                .whereEqualTo("bird_name", birdName)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            birdStatus[0] = (String)document.get("bird_status");
                            return; //return after first update as only one bird document should be found
                        }
                    }
                });
        Log.d("BirdStatus", birdStatus[0]);
        return birdStatus[0];
    }

    private int getRewardPoint(String birdStatus) {
        int[] rewardPoint = {0};
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("rewardPoint")
                .whereEqualTo("name", birdStatus)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            rewardPoint[0] = (int)document.get("reward_points");
                        }
                    }
                });
        Log.d("RewardPoint", String.valueOf(rewardPoint[0]));
        return rewardPoint[0];
    }
}
