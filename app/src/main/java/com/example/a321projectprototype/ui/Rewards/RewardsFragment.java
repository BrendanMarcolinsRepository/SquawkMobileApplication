package com.example.a321projectprototype.ui.Rewards;

import android.os.Bundle;
import android.util.EventLog;
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
import com.google.firebase.database.core.view.Event;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private Map<String, Long>rewardPointMap;

    private ArrayList<RewardDisplayBox>criticallyEndangered;
    private ArrayList<RewardDisplayBox>breedingEndemics;
    private ArrayList<RewardDisplayBox>endangered;
    private ArrayList<RewardDisplayBox>endemic;
    private ArrayList<RewardDisplayBox>introducedSpecies;
    private ArrayList<RewardDisplayBox>rareAccidental;
    private ArrayList<RewardDisplayBox>nearThreatened;
    private ArrayList<RewardDisplayBox>vulnerable;

    private long allTimeScore;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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

        rewardPointMap = new HashMap<>();
        criticallyEndangered = new ArrayList<>();
        breedingEndemics = new ArrayList<>();
        endangered = new ArrayList<>();
        endemic = new ArrayList<>();
        introducedSpecies = new ArrayList<>();
        rareAccidental = new ArrayList<>();
        nearThreatened = new ArrayList<>();
        vulnerable = new ArrayList<>();

        allTimeScore = 0;

        setScores();

        Log.d("AllTimeScoreOnCreateView", String.valueOf(allTimeScore));
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

    private void setScores() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("identified_bird").orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("recorded_by", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        populateRewardPoint(new onCallBackReward() {
                            @Override
                            public void callBack() {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String birdName = (String)document.get("bird_name");
                                    Calendar birdDate = (Calendar)document.get("date");

                                    getBirdStatus(birdName, new onCallBackStatus() {
                                        @Override
                                        public void callBack(String callbackStatus) {
                                            Log.d("Birdname", birdName);
                                            Log.d("Status", callbackStatus);

                                            addRewardDisplayBox(new RewardDisplayBox(birdName, birdDate), callbackStatus);

                                            long score = rewardPointMap.get(callbackStatus);
                                            allTimeScore += score;

                                            Log.d("Reward", String.valueOf(score));
                                        }
                                    });
                                }
                                Log.d("All Time Score", String.valueOf(allTimeScore));
                            }
                        });
                    }
                });
    }


    public interface onCallBackStatus {
    void callBack(String callbackStatus);
    }

    public interface onCallBackReward {
    void callBack();
    }

    private void getBirdStatus(String birdName, onCallBackStatus callback) {
        firebaseFirestore.collection("bird")
                .whereEqualTo("bird_name", birdName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String status = (String)document.get("bird_status");

                                callback.callBack(status);
                            }
                        } else {
                            Log.d("getBirdStatus", "Error");
                        }
                    }
                });
    }

    private void populateRewardPoint(onCallBackReward callback) {
        firebaseFirestore.collection("rewardPoint")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            long rewardPoint = (long)document.get("reward_points");
                            String status = (String) document.get("bird_status");

                            Log.d("RewardPoint", String.valueOf(rewardPoint));
                            Log.d("Status", status);

                            rewardPointMap.put(status, rewardPoint);
                        }
                        callback.callBack();
                    }
                });
        //return rewardPoint[0];
    }

    private void addRewardDisplayBox(RewardDisplayBox data, String status) {
        switch(status) {
            case "critically endangered":
                criticallyEndangered.add(data);
            case "breeding endemic":
                breedingEndemics.add(data);
            case "endangered":
                endangered.add(data);
            case "introduced species":
                introducedSpecies.add(data);
            case "rare/accidental":
                rareAccidental.add(data);
            case "near-threatened":
                nearThreatened.add(data);
            case "vulnerable":
                vulnerable.add(data);
            default:
                Log.d("Error in adding data to display box", status);
        }
    }
}
