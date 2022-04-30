package com.example.a321projectprototype.ui.Profile;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdRewardModel;
import com.example.a321projectprototype.User.BirdScores;
import com.example.a321projectprototype.User.FlockScoreModel;
import com.example.a321projectprototype.User.IdentifiedBirds;
import com.example.a321projectprototype.User.RewardPointsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment
{

    private ProfileViewModel profileViewModel;
    private HomePage homePage;
    private NavController navController;
    private ImageView userImage,totalBirdImage,editProfileImage,privacyProfileImage,
                        vulnerableImageView, endemicImageView, rareAccidentalImageView, criticallyEndangeredImageView,
                        endangeredImageView, introducedSpeciesImageView,breedingEndemicImageView, nearThreatenedImageView;
    private TextView totalTextView, vulnerableTextView, endemicTextView, rareAccidentalTextView, criticallyEndangeredTextView, endangeredTextView, introducedSpeciesTextView,breedingEndemicTextView, nearThreatenedTextView;
    private EditText usersName;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private BirdRewardModel birdRewardModel;
    private RewardPointsModel rewardPointsModel;
    private IdentifiedBirds identifiedBirds;
    private BirdScores birdScores;
    private List<BirdRewardModel> birdRewardModelList;
    private List<RewardPointsModel> rewardPointsModelList;
    private List<IdentifiedBirds> identifiedBirdsList;
    private HashMap<String,Integer> scoreHashMap;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);



        AssignVariables(root);
        retrieveFireBaseData();





        return root;
    }

    private void retrieveFireBaseData() {

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        identifiedBirdsList = new ArrayList<>();



        firebaseFirestore.collection("identified_bird")
                .whereEqualTo("recorded_by",auth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        identifiedBirds = q.toObject(IdentifiedBirds.class);
                        identifiedBirdsList.add(identifiedBirds);
                    }
                    firebaseBirdData();
                });
    }

    private void firebaseBirdData(){

        birdRewardModelList = new ArrayList<>();
        scoreHashMap = new HashMap();

        firebaseFirestore.collection("bird")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        birdRewardModel = q.toObject(BirdRewardModel.class);
                        System.out.println("=================Status : " + birdRewardModel.getBird_status());
                        scoreHashMap.put(birdRewardModel.getBird_status(),0);
                        birdRewardModelList.add(birdRewardModel);
                    }

                    firebaseRewardPointData();
                });
    }

    private void firebaseRewardPointData(){

        rewardPointsModelList = new ArrayList<>();

        firebaseFirestore.collection("rewardPoint")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        rewardPointsModel = q.toObject(RewardPointsModel.class);
                        rewardPointsModelList.add(rewardPointsModel);
                    }
                    performCalculations();
                });
    }

    private void performCalculations() {

        for(IdentifiedBirds i : identifiedBirdsList){
            for(BirdRewardModel b : birdRewardModelList){
                for(RewardPointsModel r : rewardPointsModelList){
                    if(i.getBird_name().matches(b.getBird_name())){
                        if(b.getBird_status().matches(r.getBird_status())){
                            scoreHashMap.put(b.getBird_status(),scoreHashMap.get(b.getBird_status()+r.getReward_points()));
                        }
                    }
                }
            }
        }

        loadDataToUI();
    }

    private void loadDataToUI() {

        vulnerableTextView.setText(scoreHashMap.get("vulnerable"));
        endemicTextView.setText(scoreHashMap.get("endemic"));
        rareAccidentalTextView.setText(scoreHashMap.get("rare/accidental"));
        criticallyEndangeredTextView.setText(scoreHashMap.get("critically endangered"));
        endangeredTextView.setText(scoreHashMap.get("endangered"));
        introducedSpeciesTextView.setText(scoreHashMap.get("introduced species"));
        breedingEndemicTextView.setText(scoreHashMap.get("breeding endemic"));
        nearThreatenedTextView.setText(scoreHashMap.get("near-threatened"));

        int total = 0;
        for(Map.Entry<String, Integer> set : scoreHashMap.entrySet()){
            total += set.getValue();
        }

        totalTextView.setText(total);

    }

    private void AssignVariables(View root){
        userImage = root.findViewById(R.id.profileImage);
        usersName = root.findViewById(R.id.profileNameEdittext);


        editProfileImage = root.findViewById(R.id.profileEditImage);
        privacyProfileImage = root.findViewById(R.id.profileSettings);

        totalBirdImage = root.findViewById(R.id.totalBirdCountImage);
        vulnerableImageView = root.findViewById(R.id.vulnerableImage);
        endemicImageView  = root.findViewById(R.id.endemicImage);
        rareAccidentalImageView  = root.findViewById(R.id.rareAccidentalImage);
        criticallyEndangeredImageView  = root.findViewById(R.id.criticallyEndangeredImage);
        endangeredImageView  = root.findViewById(R.id.endangeredImage);
        introducedSpeciesImageView  = root.findViewById(R.id.introducedSpeciesImage);
        breedingEndemicImageView  = root.findViewById(R.id.breedingEndemicImage);
        nearThreatenedImageView  = root.findViewById(R.id.nearThreatenedImage);

        totalTextView = root.findViewById(R.id.totalBirdCountTextView);
        vulnerableTextView = root.findViewById(R.id.vulnerableTextView);
        endemicTextView  = root.findViewById(R.id.endemicTextView);
        rareAccidentalTextView  = root.findViewById(R.id.rareAccidentaTextView);
        criticallyEndangeredTextView  = root.findViewById(R.id.criticallyEndangeredTextView);
        endangeredTextView  = root.findViewById(R.id.endangeredImageTextView);
        introducedSpeciesTextView  = root.findViewById(R.id.introducedSpeciesTextView);
        breedingEndemicTextView  = root.findViewById(R.id.breedingEndemicTextView);
        nearThreatenedTextView  = root.findViewById(R.id.nearThreatenedTextView);
    }
}