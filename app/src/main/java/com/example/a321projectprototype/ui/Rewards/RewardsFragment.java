package com.example.a321projectprototype.ui.Rewards;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    private ArrayList<rewardsStatusListObject>criticallyEndangered;
    private ArrayList<rewardsStatusListObject>breedingEndemics;
    private ArrayList<rewardsStatusListObject>endangered;
    private ArrayList<rewardsStatusListObject>endemic;
    private ArrayList<rewardsStatusListObject>introducedSpecies;
    private ArrayList<rewardsStatusListObject>rareAccidental;
    private ArrayList<rewardsStatusListObject>nearThreatened;
    private ArrayList<rewardsStatusListObject>vulnerable;

    private long dailyScore;
    private long weeklyScore;
    private long monthlyScore;
    private long yearlyScore;
    private long allTimeScore;
    int iteratorInt;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: Hide UI, Show Loading screen
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

        //stores reward points with bird status as key
        rewardPointMap = new HashMap<>();

        //a seperate list for each bird status which will be loaded with all the birds in that status the user has identified
        criticallyEndangered = new ArrayList<>();
        breedingEndemics = new ArrayList<>();
        endangered = new ArrayList<>();
        endemic = new ArrayList<>();
        introducedSpecies = new ArrayList<>();
        rareAccidental = new ArrayList<>();
        nearThreatened = new ArrayList<>();
        vulnerable = new ArrayList<>();

        dailyScore = 0;
        weeklyScore = 0;
        monthlyScore = 0;
        yearlyScore = 0;
        allTimeScore = 0;

        setScores(new onCallBack() {
            @Override
            public void callBack() {
                Log.d("All Time Score", String.valueOf(allTimeScore));
                Log.d("Daily Score", String.valueOf(dailyScore));
                Log.d("Weekly Score", String.valueOf(weeklyScore));
                Log.d("Monthly Score", String.valueOf(monthlyScore));
                Log.d("Yearly Score", String.valueOf(yearlyScore));


                //Code for all loaded data goes here
                //TODO: Hide Loading Screen, Show UI
                //TODO: Load lists from critically endangered, breeding endemics, endangered, etc into UI
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

    //load identified birds and their scores from firebase and calculate scores
    private void setScores(onCallBack callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("identified_bird").orderBy("date", Query.Direction.ASCENDING) //ordering list makes it easier to search through later
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

                                            addRewardDisplayBox(new rewardsStatusListObject(birdName, birdDate), callbackStatus);

                                            long score = rewardPointMap.get(callbackStatus);
                                            allTimeScore += score;

                                            Log.d("Reward", String.valueOf(score));

                                            if(iteratorInt++ == snapshot.size() - 1){ //last iteration
                                                //calculate daily/weekly/monthly/yearly scores
                                                getTimePeriodScores();

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

    private void addRewardDisplayBox(rewardsStatusListObject data, String status) {
        switch(status) {
            case "critically endangered":
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

    //populates each time periods score with each bird status score list
    private void getTimePeriodScores() {
        getTimePeriodStatusScore(criticallyEndangered, "critically endangered");
        getTimePeriodStatusScore(breedingEndemics, "breeding endemic");
        getTimePeriodStatusScore(endemic, "endemic");
        getTimePeriodStatusScore(endangered, "endangered");
        getTimePeriodStatusScore(introducedSpecies, "introduced species");
        getTimePeriodStatusScore(rareAccidental, "rare/accidental");
        getTimePeriodStatusScore(nearThreatened, "near-threatened");
        getTimePeriodStatusScore(vulnerable, "vulnerable");
    }

    private void getTimePeriodStatusScore(ArrayList<rewardsStatusListObject> list, String status) {
        long statusScore = rewardPointMap.get(status);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        for(rewardsStatusListObject i:list) {
            //switch statements don't work with Calendar objects so nested if statements used
            if(i.getTimestamp().toDate().after(cal.getTime())) { //if in yearly period
                yearlyScore+=statusScore;

                cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);

                if(i.getTimestamp().toDate().after(cal.getTime())) { //if in monthly period
                    monthlyScore+=statusScore;

                    cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -7);
                    if(i.getTimestamp().toDate().after(cal.getTime())) { //if in weekly period
                        weeklyScore+=statusScore;

                        cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        if(i.getTimestamp().toDate().after(cal.getTime())) { //if in daily period
                            dailyScore+=statusScore;
                        } else { //return if nothing in daily range
                            return;
                        }
                    }
                }
            }
        }
    }
}
