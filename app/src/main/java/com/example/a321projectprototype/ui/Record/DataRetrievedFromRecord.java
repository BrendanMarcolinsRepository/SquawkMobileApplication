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

    private void setData()
    {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("bird").addSnapshotListener((value, error) -> {

            System.out.println("Bird Section +++++++++++++++++++++++++++++++++++++");
            if(error != null)
            {
                System.out.println("Error ==========?>" +  error);
                return;
            }


            for(DocumentChange documentChange : value.getDocumentChanges())
            {
                if(documentChange.getType() == DocumentChange.Type.ADDED)
                {

                    BirdRewardModel birdRewardModel = documentChange.getDocument().toObject(BirdRewardModel.class);
                    birdRewardModelList.add(birdRewardModel);
                }

            }
            generator();
            loadRewardData();

        });
    }

    public void loadRewardData() {

        firebaseFirestore.collection("rewardPoint").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override


                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                       System.out.println("Reward Section +++++++++++++++++++++++++++++++++++++");

                        if(error != null)
                        {
                            System.out.println("Error ==========?>" +  error);
                            return;
                        }

                        for(DocumentChange documentChange : value.getDocumentChanges())
                        {
                            if(documentChange.getType() == DocumentChange.Type.ADDED)
                            {
                                RewardPointsModel rewardPointsModel = documentChange.getDocument().toObject(RewardPointsModel.class);
                                rewardPoints.add(rewardPointsModel);
                            }

                        }


                        setRecyclerView();
                    }
                });

    }

    public void generator(){
        Random randomObject = new Random();
        List<Integer> numberBirds = new ArrayList<>();
        List<BirdRewardModel> tempBirdList = new ArrayList<>();


        int counter = 0;
        while(counter < randomObject.nextInt(4) + 1) {
            int randomInteger = randomObject.nextInt(23);

            if(numberBirds.isEmpty()){
                numberBirds.add(randomInteger);
                tempBirdList.add(birdRewardModelList.get(randomInteger));
            }else{
                for(int i = 0; i < numberBirds.size(); i++) {
                    if(numberBirds.get(i) != randomInteger) {
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

    public void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recordDataCardViewAdapter = new RecordDataCardViewAdapter(birdRewardModelList,homePage,rewardPoints);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recordDataCardViewAdapter);

        getFlockData();
    }

    public void getUserScore(){
        firebaseFirestore.collection("userScore")
            .document(auth.getUid())
            .get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful() && task.getResult() != null){
                    System.out.println("User Score Week" + auth.getUid());

                    userScore = task.getResult().toObject(UserScore.class);
                    setRewardPoints();
            }
        });
    }

    public void setRewardPoints(){




        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference;



        for(int i = 0; i < birdRewardModelList.size(); i++) {


            RecordByModel  recordByModel = new RecordByModel(birdRewardModelList.get(i).getBird_name(),firebaseAuth.getUid());

            documentReference = firebaseFirestore.collection("identified_bird").document();
            documentReference.set(recordByModel)
                    .addOnFailureListener(e -> {

            });



            for(int k = 0; k < rewardPoints.size();k++) {

                if(rewardPoints.get(k).getBird_status().matches(birdRewardModelList.get(i).getBird_status())){

                    userScore.setScoreThisWeek(rewardPoints.get(k).getReward_points() + userScore.getScoreThisWeek());
                    userScore.setScoreThisMonth(rewardPoints.get(k).getReward_points() + userScore.getScoreThisMonth());
                    userScore.setScoreThisYear(rewardPoints.get(k).getReward_points() + userScore.getScoreThisYear());

                    if(FlockExist){
                        flockScoreModel.setScorethisweek(rewardPoints.get(k).getReward_points() + flockScoreModel.getScorethisweek());
                        flockScoreModel.setScorethismonth(rewardPoints.get(k).getReward_points() + flockScoreModel.getScorethismonth());
                        flockScoreModel.setScorethisyear(rewardPoints.get(k).getReward_points() + flockScoreModel.getScorethisyear());
                    }

                    System.out.println("Worked Finished");
                }
            }

        }

        if(FlockExist){
            updateFlockScore();
        }else{
            updateUserScore();
        }
    }

    public void updateFlockScore(){


        Map<String, Object> flockScoreUpdateHashMap =  new HashMap<>();

        flockScoreUpdateHashMap.put("scorethisweek",flockScoreModel.getScorethisweek());
        flockScoreUpdateHashMap.put("scorethismonth",flockScoreModel.getScorethismonth());
        flockScoreUpdateHashMap.put("scorethisyear",flockScoreModel.getScorethisyear());
        flockScoreUpdateHashMap.put("totalScore",flockScoreModel.getTotalScore());
        flockScoreUpdateHashMap.put("updated_at",getDate());

        DocumentReference documentReferenceFlockScore = firebaseFirestore.collection("flockScore").document(flockId);
        documentReferenceFlockScore.update(flockScoreUpdateHashMap)
                .addOnCompleteListener(task -> {
                    updateUserScore();
                })
                .addOnFailureListener(e -> {
                    updateUserScore();
                });

    }


    public void updateUserScore(){
        Map<String, Object> userScoreUpdateHashMap =  new HashMap<>();

        userScoreUpdateHashMap.put("scoreThisWeek",userScore.getScoreThisWeek());
        userScoreUpdateHashMap.put("scoreThisMonth",userScore.getScoreThisMonth());
        userScoreUpdateHashMap.put("scoreThisYear",userScore.getScoreThisYear());
        userScoreUpdateHashMap.put("totalScore",userScore.getTotalScore());
        userScoreUpdateHashMap.put("updated_at",getDate());


        DocumentReference documentReferenceUserScore = firebaseFirestore.collection("userScore").document(auth.getUid());

        documentReferenceUserScore
                .update(userScoreUpdateHashMap)
                .addOnCompleteListener(task -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    public void getFlockData(){

        firebaseFirestore.collection("flockMembers")
                .whereEqualTo("userId",auth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.getDocumentChanges().isEmpty()){
                        flockId = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().get("flockId").toString();

                        firebaseFirestore
                                .collection("flocks")
                                .whereEqualTo("flockId",flockId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> firebaseFirestore
                                        .collection("flockScore").document(flockId)
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            FlockExist = true;
                                            System.out.println("Worked Finished 2");
                                            flockScoreModel = task.getResult().toObject(FlockScoreModel.class);
                                            getUserScore();
                                        }));
                    }else{
                        getUserScore();
                    }
                });
    }

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
