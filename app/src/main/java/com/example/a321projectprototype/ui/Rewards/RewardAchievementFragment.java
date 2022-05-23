package com.example.a321projectprototype.ui.Rewards;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserScore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RewardAchievementFragment extends Fragment {
    private final int standardTotal1 = 1000;
    private final int standardTotal2 = 5000;
    private final int standardThisMonth = 100;
    private final int standardThisMonth2 = 500;
    TextView achievement1, achievement2, achievement3, achievement4;
    CardView card1, card2, card3, card4;
    CardView bar1, bar2, bar3, bar4;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private UserScore userRewardModel;
    private final String complete = "Congrats, you achieved it!";

    public RewardAchievementFragment() {
        // Required empty public constructor
    }

    public static RewardAchievementFragment newInstance() {
        RewardAchievementFragment fragment = new RewardAchievementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reward_achievement, container, false);
        initializeViews(root);
        loadUserScore();
        return root;
    }

    //load user reward points data from firebase
    public void loadUserScore() {
        firebaseFirestore.collection("userScore")
                .whereEqualTo("userId",auth.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        userRewardModel = documentChange.getDocument().toObject(UserScore.class);
                    }
                }
                checkScore(userRewardModel);
            }
        });
    }
    //load different card based on user scores and standard scores
    public void checkScore(UserScore userRewardModel){
        DecimalFormat format = new DecimalFormat("##.0");
        if(userRewardModel.getTotalScore() >= standardTotal1){
            achievement1.setText(complete);
            card1.setCardBackgroundColor(Color.parseColor("#ABF0A7"));
        }else{
            double percentage = (float)userRewardModel.getTotalScore()/standardTotal1;
            achievement1.setText("Progress:"+format.format(percentage*100)+"% Keep Going!");
            card1.setCardBackgroundColor(Color.parseColor("#9A98FF"));
            bar1.setCardBackgroundColor(Color.parseColor("#E56717"));
        }

        if(userRewardModel.getTotalScore() >= standardTotal2){
            achievement2.setText(complete);
            card2.setCardBackgroundColor(Color.parseColor("#ABF0A7"));
        }else{
            double percentage = (float)userRewardModel.getTotalScore()/standardTotal2;
            achievement2.setText("Progress:"+format.format(percentage*100)+"% Keep Going!");
            card2.setCardBackgroundColor(Color.parseColor("#9A98FF"));
            bar2.setCardBackgroundColor(Color.parseColor("#E56717"));
        }

        if(userRewardModel.getScoreThisMonth() >= standardThisMonth){
            achievement3.setText(complete);
            card3.setCardBackgroundColor(Color.parseColor("#ABF0A7"));
        }else{
            double percentage = (float)userRewardModel.getTotalScore()/standardThisMonth;
            achievement3.setText("Progress:"+format.format(percentage*100)+"% Keep Going!");
            card3.setCardBackgroundColor(Color.parseColor("#9A98FF"));
            bar3.setCardBackgroundColor(Color.parseColor("#E56717"));
        }

        if(userRewardModel.getScoreThisMonth() >= standardThisMonth2){
            achievement4.setText(complete);
            card4.setCardBackgroundColor(Color.parseColor("#ABF0A7"));
        }else{
            double percentage = (float)userRewardModel.getTotalScore()/standardThisMonth2;
            achievement4.setText("Progress:"+format.format(percentage*100)+"% Keep Going!");
            card4.setCardBackgroundColor(Color.parseColor("#9A98FF"));
            bar4.setCardBackgroundColor(Color.parseColor("#E56717"));
        }
    }
    //initialize views
    public void initializeViews(View root){
        achievement1 = root.findViewById(R.id.achievement_words);
        achievement2 = root.findViewById(R.id.achievement_words2);
        achievement3 = root.findViewById(R.id.achievement_words3);
        achievement4 = root.findViewById(R.id.achievement_words4);

        card1 = root.findViewById(R.id.reward_achievement_listItem);
        card2 = root.findViewById(R.id.reward_achievement_listItem2);
        card3 = root.findViewById(R.id.reward_achievement_listItem3);
        card4 = root.findViewById(R.id.reward_achievement_listItem4);

        bar1 = root.findViewById(R.id.reward_achievement_bar);
        bar2 = root.findViewById(R.id.reward_achievement_bar2);
        bar3 = root.findViewById(R.id.reward_achievement_bar3);
        bar4 = root.findViewById(R.id.reward_achievement_bar4);
    }
}