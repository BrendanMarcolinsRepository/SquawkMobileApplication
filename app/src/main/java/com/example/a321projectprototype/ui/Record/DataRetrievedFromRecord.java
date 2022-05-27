package com.example.a321projectprototype.ui.Record;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdRewardModel;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.FlockScoreModel;
import com.example.a321projectprototype.User.ItemDataModel;
import com.example.a321projectprototype.User.RecordByModel;
import com.example.a321projectprototype.User.RewardPointsModel;
import com.example.a321projectprototype.User.UserScore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
//Code by Brendan Marcolin
public class DataRetrievedFromRecord extends Fragment
{
    private RecyclerView recyclerView;
    private HomePage homePage;
    private List<Integer> numberList;
    private List<BirdRewardModel> birdRewardModelList;
    private List<RewardPointsModel> rewardPoints;
    private int maxIdentifer;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager linearLayoutManager;
    private RecordDataCardViewAdapter recordDataCardViewAdapter;
    private FlockScoreModel flockScoreModel;
    private UserScore userScore;
    private boolean FlockExist = false;
    private String flockId;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragement_record_retrieved_data, container, false);

        auth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.recycleRecordData);
        homePage = (HomePage) getActivity();

        birdRewardModelList = new ArrayList<>();
        rewardPoints = new ArrayList<>();

        setData();



        return  root;

    }

    //loads the bird data from the firebase database
    private void setData()
    {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("bird").addSnapshotListener((value, error) -> {

            //loops through all the documents and retrieves them and store them in the objects
            for(DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {

                    BirdRewardModel birdRewardModel = documentChange.getDocument().toObject(BirdRewardModel.class);
                    birdRewardModelList.add(birdRewardModel);
                }

            }
            generator();
            loadRewardData();

        });
    }

    //loads the reward data from the firebase database
    public void loadRewardData() {

        firebaseFirestore.collection("rewardPoint").addSnapshotListener((value, error) -> {

            //loops through all the documents and retrieves them and store them in the objects
            for(DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    RewardPointsModel rewardPointsModel = documentChange.getDocument().toObject(RewardPointsModel.class);
                    rewardPoints.add(rewardPointsModel);
                }

            }
            setRecyclerView();
        });

    }

    //randomise the amount of output of birds the user will get from the simulation
    public void generator(){
        //random object for getting random number
        Random randomObject = new Random();

        //list needed to store the data
        List<Integer> numberBirds = new ArrayList<>();
        List<BirdRewardModel> tempBirdList = new ArrayList<>();


        //loops through the amount of birds found
        int counter = 0;
        while(counter < randomObject.nextInt(4) + 1) {
            //gets a random bird (23 birds total)
            int randomInteger = randomObject.nextInt(23);

            //first check if the list is empty
            if(numberBirds.isEmpty()){
                //if it is will store the bird number found so it cant be used again
                //and add it to the bird tmep list
                numberBirds.add(randomInteger);
                tempBirdList.add(birdRewardModelList.get(randomInteger));
            }else{
                for(int i = 0; i < numberBirds.size(); i++) {
                    //checks to see if the bird number has already been used
                    if(numberBirds.get(i) != randomInteger) {
                        //if it is will store the bird number found so it cant be used again
                        //and add it to the bird tmep list
                        numberBirds.add(randomInteger);
                        tempBirdList.add(birdRewardModelList.get(randomInteger));
                    }
                }
            }

            counter++;
        }


        birdRewardModelList.clear();

        if(!tempBirdList.isEmpty()){
            birdRewardModelList.addAll(tempBirdList);
        }

    }

    //sets the recycle view
    public void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recordDataCardViewAdapter = new RecordDataCardViewAdapter(birdRewardModelList,homePage,rewardPoints);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recordDataCardViewAdapter);

        getFlockData();
    }

    //gets the current user score
    public void getUserScore(){
        firebaseFirestore.collection("userScore")
            .document(auth.getUid())
            .get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult() != null){
                    //loops through all the documents and retrieves them and store them in the objects
                    userScore = task.getResult().toObject(UserScore.class);
                    setRewardPoints();
            }
        });
    }

    //updates the user score in the database
    public void setRewardPoints(){


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference;

        //loops through the amount of birds detected
        for(int i = 0; i < birdRewardModelList.size(); i++) {

            //to get each object
            RecordByModel  recordByModel = new RecordByModel(birdRewardModelList.get(i).getBird_name(),firebaseAuth.getUid());

            //stores it in "identified_bird"
            documentReference = firebaseFirestore.collection("identified_bird").document();
            documentReference.set(recordByModel)
                    .addOnFailureListener(e -> {

            });



            //loops through the reward point list
            for(int k = 0; k < rewardPoints.size();k++) {

                //if status match
                if(rewardPoints.get(k).getBird_status().matches(birdRewardModelList.get(i).getBird_status())){

                    //updates the user score object
                    int rewardPoint = rewardPoints.get(k).getReward_points();
                    userScore.setScoreThisWeek(rewardPoint + userScore.getScoreThisWeek());
                    userScore.setScoreThisMonth(rewardPoint + userScore.getScoreThisMonth());
                    userScore.setScoreThisYear(rewardPoint + userScore.getScoreThisYear());
                    userScore.setTotalScore(rewardPoint + userScore.getTotalScore());

                    //updates the flock score object if exist
                    if(FlockExist){
                        flockScoreModel.setScorethisweek(rewardPoint + flockScoreModel.getScorethisweek());
                        flockScoreModel.setScorethismonth(rewardPoint + flockScoreModel.getScorethismonth());
                        flockScoreModel.setScorethisyear(rewardPoint + flockScoreModel.getScorethisyear());
                        flockScoreModel.setTotalScore(rewardPoint + flockScoreModel.getTotalScore());
                    }
                }
            }

        }

        //determines if just the user score gets updated or not
        if(FlockExist){
            updateFlockScore();
        }else{
            updateUserScore();
        }
    }

    //updates the flock score in the database
    public void updateFlockScore(){


        //hash map to push flock data onto the firebase
        Map<String, Object> flockScoreUpdateHashMap =  new HashMap<>();
        flockScoreUpdateHashMap.put("scorethisweek",flockScoreModel.getScorethisweek());
        flockScoreUpdateHashMap.put("scorethismonth",flockScoreModel.getScorethismonth());
        flockScoreUpdateHashMap.put("scorethisyear",flockScoreModel.getScorethisyear());
        flockScoreUpdateHashMap.put("totalScore",flockScoreModel.getTotalScore());
        flockScoreUpdateHashMap.put("updated_at",getDate());

        //hash map stored in "flockscore", either on failure or success will process to user score next
        DocumentReference documentReferenceFlockScore = firebaseFirestore.collection("flockScore").document(flockId);
        documentReferenceFlockScore.update(flockScoreUpdateHashMap)
                .addOnCompleteListener(task -> {
                    updateUserScore();
                })
                .addOnFailureListener(e -> {
                    updateUserScore();
                });

    }

    //updates the user score in the database

    public void updateUserScore(){

        //hash map to push user score data onto the firebase
        Map<String, Object> userScoreUpdateHashMap =  new HashMap<>();
        userScoreUpdateHashMap.put("scoreThisWeek",userScore.getScoreThisWeek());
        userScoreUpdateHashMap.put("scoreThisMonth",userScore.getScoreThisMonth());
        userScoreUpdateHashMap.put("scoreThisYear",userScore.getScoreThisYear());
        userScoreUpdateHashMap.put("totalScore",userScore.getTotalScore());
        userScoreUpdateHashMap.put("updated_at",getDate());


        //hash map stored in "userScore"
        DocumentReference documentReferenceUserScore = firebaseFirestore.collection("userScore").document(auth.getUid());

        documentReferenceUserScore
                .update(userScoreUpdateHashMap)
                .addOnCompleteListener(task -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    //gets the flock data information from the database
    public void getFlockData(){

        //gets all the flock members
        firebaseFirestore.collection("flockMembers")
                .whereEqualTo("userId",auth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.getDocumentChanges().isEmpty()){
                        //stores the flock id
                        flockId = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().get("flockId").toString();

                        //gets the flock where it equals the flock id
                        firebaseFirestore
                                .collection("flocks")
                                .whereEqualTo("flockId",flockId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> firebaseFirestore
                                        .collection("flockScore").document(flockId)
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            //if the flock exist it will set boolean true and store the data
                                            FlockExist = true;
                                            flockScoreModel = task.getResult().toObject(FlockScoreModel.class);
                                            //gets the current user score
                                            getUserScore();
                                        }));
                    }else{
                        //if no flock exist for this user it will just gets the currnet user score
                        getUserScore();
                    }
                });
    }


    //provides a data in a format
    public String  getDate(){

      Date date = new Date();
      SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
      String dataString = ft.format(date);

      if(dataString == null || dataString.isEmpty()){
          dataString = "";
      }

      return  dataString;

  }
}
