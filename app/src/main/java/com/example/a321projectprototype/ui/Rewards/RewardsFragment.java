package com.example.a321projectprototype.ui.Rewards;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdIdentifyModel;
import com.example.a321projectprototype.User.BirdRewardModel;
import com.example.a321projectprototype.User.RewardPointModel;
import com.example.a321projectprototype.User.RewardPointsModel;
import com.example.a321projectprototype.User.UserScore;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;

public class RewardsFragment extends Fragment
{
    private RewardsViewModel rewardsViewModel;
    private CardView compareButton,achievementButton;
    private NavController navController;
    private HomePage homePage;
    private Spinner rewardSpinner;
    private ArrayList<String> times;
    private ArrayAdapter<String> adaptor;
    private TextView rewardContent, rewardDisplay;
    private ArrayList<RewardPointModel> birdRewardPoints = new ArrayList<>();
    private ArrayList<BirdRewardModel> birds = new ArrayList<>();
    private ArrayList<RewardPointsModel> rewardPoints = new ArrayList<>();
    private ArrayList<UserScore> userRewards = new ArrayList<>();
    private ArrayList<BirdIdentifyModel> identifiedBirds = new ArrayList<>();
    private RecyclerView birdListRecycler;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String rewardContentWords = "Congradulations, your all time score is";
    private final String rewardDisplayWords = "Your score for ";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rewardsViewModel = new ViewModelProvider(this).get(RewardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();
        //initialize views
        compareButton = root.findViewById(R.id.reward_compare_button);
        achievementButton = root.findViewById(R.id.reward_achievement_button);
        compareButton.setOnClickListener(rewardCompare);
        achievementButton.setOnClickListener(rewardAchievement);
        rewardContent = root.findViewById(R.id.reward_content);
        rewardDisplay = root.findViewById(R.id.reward_display);
        //load firebase data
        loadUserRewardData();
        loadIdentifiedBirds();
        //Initialize Spinner
        rewardSpinner = root.findViewById(R.id.reward_compare_spinner);
        loadSpinner();
        //when the spinner date change
        rewardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                initializeContent(time);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing Selected");
            }
        });
        //initialize recycler view
        birdListRecycler = root.findViewById(R.id.bird_list_items);
        return root;
    }
    // navigation
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
    //load bird list from firebase
    private void loadBirds(){
        firebaseFirestore.collection("bird").addSnapshotListener((value, error) -> {
            birds.clear();
            for(DocumentChange documentChange : value.getDocumentChanges())
            {
                if(documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    BirdRewardModel birdRewardModel = documentChange.getDocument().toObject(BirdRewardModel.class);
                    birds.add(birdRewardModel);
                }
            }
            generateList(birds,null);
        });
    }
    //add bird status and reward points into birdRewardPoints
    private void generateList(ArrayList<BirdRewardModel> birds, ArrayList<RewardPointsModel> rewardPoints)
    {
        if(birds != null){
            for(RewardPointModel rp: birdRewardPoints){
                for(BirdRewardModel bird: birds){
                    if(bird.getBird_name().equalsIgnoreCase(rp.getBirdName())){
                        rp.setBirdStatus(bird.getBird_status());
                        break;
                    }
                }
            }
            loadBirdRewardData();
        }
        if(rewardPoints != null){
            for(RewardPointModel rp: birdRewardPoints){
                for(RewardPointsModel rpm: rewardPoints){
                    if(rp.getBirdStatus().equalsIgnoreCase(rpm.getBird_status())){
                        rp.setRewardPoints(rpm.getReward_points());
                        rp.setTotalRewardPoints(rpm.getReward_points() * rp.getBirdCount());
                        break;
                    }
                }
            }
            initializeRecycler(birdRewardPoints);
        }
    }
    //load user reward points data from firebase
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadUserRewardData()
    {
        firebaseFirestore.collection("userScore").addSnapshotListener((value, error) -> {
            userRewards.clear();
            for(DocumentChange documentChange : value.getDocumentChanges())
            {
                if(documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    UserScore userRewardModel = documentChange.getDocument().toObject(UserScore.class);
                    if(userRewardModel != null){
                        userRewards.add(userRewardModel);
                    }
                }
            }
            initializeContent("This Month");
        });
    }
    //load bird reward point data
    public void loadBirdRewardData()
    {
        firebaseFirestore.collection("rewardPoint").addSnapshotListener((value, error) -> {
            rewardPoints.clear();
            for(DocumentChange documentChange : value.getDocumentChanges())
            {
                if(documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    RewardPointsModel rewardPointsModel = documentChange.getDocument().toObject(RewardPointsModel.class);
                    rewardPoints.add(rewardPointsModel);
                }
            }
            generateList(null,rewardPoints);
        });
    }
    //load list of identified bird
    public void loadIdentifiedBirds()
    {
        firebaseFirestore.collection("identified_bird").
                whereEqualTo("recorded_by",auth.getUid()).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                identifiedBirds.clear();
                for(DocumentChange documentChange : value.getDocumentChanges())
                {
                    if(documentChange.getType() == DocumentChange.Type.ADDED)
                    {
                        String birdName = (String)documentChange.getDocument().get("bird_name");
                        String userName = (String)documentChange.getDocument().get("recorded_by");
                        //Timestamp birdDate = (Timestamp)documentChange.getDocument().get("date");
                        BirdIdentifyModel identifiedBirdie = new BirdIdentifyModel();
                        identifiedBirdie.setBird_name(birdName);
                        identifiedBirdie.setRecorded_by(userName);
                        //identifiedBirdie.setDate(birdDate);
                        identifiedBirds.add(identifiedBirdie);
                    }
                }
                sortBirdCount(identifiedBirds);
                loadBirds();
            }
        });
    }
    //add and sort bird name and count
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortBirdCount(ArrayList<BirdIdentifyModel> identifiedBirds){
//        filter identified bird list by userid
//        List<BirdIdentifyModel> filteredList = identifiedBirds.stream().
//            filter(item -> item.getRecorded_by().equalsIgnoreCase(auth.getUid())).
//            collect(Collectors.toList());
        //count bird number by bird name
        Map<String, Long> birdCounts = identifiedBirds.stream()
                .collect(Collectors.groupingBy(item -> item.getBird_name(), Collectors.counting()));
        //clean bird reward points
        birdRewardPoints.clear();
        //map count into birdRewardPoint list
        for(String birdName: birdCounts.keySet()){
              RewardPointModel rp = new RewardPointModel();
              rp.setBirdName(birdName);
              rp.setBirdCount(birdCounts.get(birdName).intValue());
              birdRewardPoints.add(rp);
        }
    }
    //create date time spinner
    private void loadSpinner()
    {
        times = new ArrayList<>();
        times.add("Total");
        times.add("This Week");
        times.add("This Month");
        times.add("This Year");
        times.add("Today");
        adaptor = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, times);
        rewardSpinner.setAdapter(adaptor);
        rewardSpinner.setSelection(2); //set the default as this month
    }
    //initialize reward content
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initializeContent(String time)
    {
        UserScore userReward = userRewards.stream().
                filter(urm -> auth.getUid().equalsIgnoreCase(urm.getUserId())).findAny().orElse(null);

        System.out.println("userrrrrrrrrrrrrrrrrr" + userReward.getUserId());
        System.out.println("userrrrrrrrrrrrrrrrrr" + userReward.getTotalScore());

        String rewardText = rewardContentWords + " " + String.valueOf(userReward.getTotalScore());
        rewardContent.setText(rewardText);
        String rewardContent = rewardDisplayWords;
        switch(time){
            case "This Month":
                rewardContent += "this month is " + userReward.getScoreThisMonth();
                break;
            case "This Year":
                rewardContent += "this year is " + userReward.getScoreThisYear();
                break;
            case "This Week":
                rewardContent += "this week is " + userReward.getScoreThisWeek();
                break;
            case "Total":
                rewardContent += "all time is " + userReward.getTotalScore();
                break;
            default:
                rewardContent += "today is " + 0;
        }
        rewardDisplay.setText(rewardContent);
    }
    //initialize recycler view
    private void initializeRecycler(ArrayList<RewardPointModel> birdRewardPoints)
    {
        RewardPointAdapter listAdaptor = new RewardPointAdapter(birdRewardPoints);
        birdListRecycler.setAdapter(listAdaptor);
        birdListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
