package com.example.a321projectprototype.ui.Rewards;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    int iteratorInt;

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

        setScores(new onCallBack() {
            @Override
            public void callBack() {
                Log.d("All Time Score", String.valueOf(allTimeScore));

                /******************
                   Code Goes Here
                 ******************/
            }
        });

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

    private void setScores(onCallBack callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("identified_bird").orderBy("date", Query.Direction.ASCENDING)
                .whereEqualTo("recorded_by", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        populateRewardPoint(new onCallBack() {
                            @Override
                            public void callBack() {
                                QuerySnapshot snapshot = task.getResult();
                                iteratorInt = 0;
                                for (QueryDocumentSnapshot document : snapshot) {
                                    String birdName = (String)document.get("bird_name");
                                    Timestamp birdDate = (Timestamp)document.get("date");

                                    getBirdStatus(birdName, new onCallBackString() {
                                        @Override
                                        public void callBack(String callbackStatus) {
                                            Log.d("Birdname", birdName);
                                            Log.d("Status", callbackStatus);

                                            addRewardDisplayBox(new RewardDisplayBox(birdName, birdDate), callbackStatus);

                                            long score = rewardPointMap.get(callbackStatus);
                                            allTimeScore += score;

                                            Log.d("Reward", String.valueOf(score));

                                            if(iteratorInt++ == snapshot.size() - 1){ //last iteration
                                                callback.callBack();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
    }


    public interface onCallBackString {
    void callBack(String callbackStatus);
    }

    public interface onCallBack {
    void callBack();
    }

    private void getBirdStatus(String birdName, onCallBackString callback) {
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

    private void populateRewardPoint(onCallBack callback) {
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
            case "critically endangereds":
                criticallyEndangered.add(data);
                return;
            case "breeding endemic":
                breedingEndemics.add(data);
                return;
            case "endemic":
                endemic.add(data);
                return;
            case "endangered":
                endangered.add(data);
                return;
            case "introduced species":
                introducedSpecies.add(data);
                return;
            case "rare/accidental":
                rareAccidental.add(data);
                return;
            case "near-threatened":
                nearThreatened.add(data);
                return;
            case "vulnerable":
                vulnerable.add(data);
                return;
            default:
                Log.d("Error in adding data to display box", status);
        }
    }
}
