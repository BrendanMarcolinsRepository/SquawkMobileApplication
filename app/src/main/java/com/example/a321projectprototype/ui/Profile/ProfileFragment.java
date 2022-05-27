package com.example.a321projectprototype.ui.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdRewardModel;
import com.example.a321projectprototype.User.BirdScores;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.FlockScoreModel;
import com.example.a321projectprototype.User.IdentifiedBirds;
import com.example.a321projectprototype.User.RewardPointsModel;
import com.example.a321projectprototype.User.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
//Coding Done By Selby Malone
public class ProfileFragment extends Fragment
{

    private ProfileViewModel profileViewModel;
    private HomePage homePage;
    private NavController navController;
    private ImageView userImage,totalBirdImage,editProfileImage,privacyProfileImage,
                        vulnerableImageView, endemicImageView, rareAccidentalImageView, criticallyEndangeredImageView,
                        endangeredImageView, introducedSpeciesImageView,breedingEndemicImageView, nearThreatenedImageView;
    private TextView usersName,totalTextView, vulnerableTextView, endemicTextView, rareAccidentalTextView, criticallyEndangeredTextView, endangeredTextView, introducedSpeciesTextView,breedingEndemicTextView, nearThreatenedTextView;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private BirdRewardModel birdRewardModel;
    private RewardPointsModel rewardPointsModel;
    private IdentifiedBirds identifiedBirds;
    private List<BirdRewardModel> birdRewardModelList;
    private List<RewardPointsModel> rewardPointsModelList;
    private List<IdentifiedBirds> identifiedBirdsList;
    private HashMap<String,Integer> scoreHashMap;
    private View root;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_profile, container, false);


        DrawerLayout drawerLayout = homePage.getDrawer();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        AssignVariables(root);
        retrieveFireBaseData();



        editProfileImage.setOnClickListener(updateProfileNavigatorMethod);
        privacyProfileImage.setOnClickListener(updateProfileSettings);




        return root;
    }

    //Navigators
    private final View.OnClickListener updateProfileNavigatorMethod = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navController.navigate(R.id.action_nav_Profile_to_nav_Profile_Settings);
        }
    };

    private final View.OnClickListener updateProfileSettings = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navController.navigate(R.id.action_nav_Profile_to_nav_Settings);
        }
    };

    //displays user information and starts the retrieval of the firebase identifed by the user
    private void retrieveFireBaseData() {

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        identifiedBirdsList = new ArrayList<>();

        UserModel userModel = homePage.getUserModel();

        usersName.setText(userModel.getUsername());
        Glide.with(homePage.getApplicationContext())
                .load(userModel.getPhoto_url())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.user_profile)
                .into(userImage);


        //gets all the birds the user has identified
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

    //gets all the birds and their status
    private void firebaseBirdData(){

        birdRewardModelList = new ArrayList<>();
        scoreHashMap = new HashMap();

        firebaseFirestore.collection("bird")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        birdRewardModel = q.toObject(BirdRewardModel.class);
                        birdRewardModelList.add(birdRewardModel);
                    }

                    firebaseRewardPointData();
                });
    }

    //gets the reward points for each bird
    private void firebaseRewardPointData(){

        rewardPointsModelList = new ArrayList<>();

        firebaseFirestore.collection("rewardPoint")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot q : queryDocumentSnapshots) {
                        rewardPointsModel = q.toObject(RewardPointsModel.class);
                        scoreHashMap.put(rewardPointsModel.getBird_status(),0);
                        rewardPointsModelList.add(rewardPointsModel);
                    }
                    performCalculations();
                });
    }

    //performs the calculation to identify how many of each bird status has been collected
    private void performCalculations() {


        System.out.println("SCORE UPDATE JERE ++++++++ " + scoreHashMap.get("vulnerable"));

        for(IdentifiedBirds i : identifiedBirdsList){
            for(BirdRewardModel b : birdRewardModelList){
                if(i.getBird_name().matches(b.getBird_name())){
                    for(RewardPointsModel r : rewardPointsModelList){
                        if(b.getBird_status().matches(r.getBird_status())){
                            String status = r.getBird_status();
                            if(scoreHashMap.containsKey(status)){
                                scoreHashMap.put(status,scoreHashMap.get(status) + 1 );

                            }
                        }
                    }
                }
            }
        }

        loadDataToUI();
    }

    //loads the data retrived into the UI
    private void loadDataToUI() {

        vulnerableTextView.setText(String.valueOf(scoreHashMap.get("vulnerable")));
        endemicTextView.setText(String.valueOf(scoreHashMap.get("endemic")));
        rareAccidentalTextView.setText(String.valueOf(scoreHashMap.get("rare/accidental")));
        criticallyEndangeredTextView.setText(String.valueOf(scoreHashMap.get("critically endangered")));
        endangeredTextView.setText(String.valueOf(scoreHashMap.get("endangered")));
        introducedSpeciesTextView.setText(String.valueOf(scoreHashMap.get("introduced species")));
        breedingEndemicTextView.setText(String.valueOf(scoreHashMap.get("breeding endemic")));
        nearThreatenedTextView.setText(String.valueOf(scoreHashMap.get("near-threatened")));

        int total = 0;
        for(Map.Entry<String, Integer> set : scoreHashMap.entrySet()){
            total += set.getValue();
        }

        totalTextView.setText(String.valueOf(total));

    }

    //assigning all the variables needed
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