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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DataRetrievedFromRecord extends Fragment
{
    private RecyclerView recyclerView;
    private HomePage homePage;
    private List<Integer> numberList;
    private List<BirdRewardModel> birdRewardModelList;
    private List<RewardPointsModel> rewardPoints;
    private final int MAX_IDENTIFIER = 3;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore;
    private LinearLayoutManager linearLayoutManager;
    private RecordDataCardViewAdapter recordDataCardViewAdapter;
    private FlockScoreModel flockScoreModel;
    private boolean FlockExist = false;
    private String flockId;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragement_record_retrieved_data, container, false);

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
        while(counter < MAX_IDENTIFIER) {
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



        birdRewardModelList = null;
        birdRewardModelList.addAll(tempBirdList);
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

    public void setRewardPoints(){


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference;

        for(int i = 0; i < birdRewardModelList.size(); i++) {

            WriteBatch batchIdentifiedBird = firebaseFirestore.batch();



            documentReference = firebaseFirestore.collection("identified_bird").document();


            RecordByModel  recordByModel = new RecordByModel(birdRewardModelList.get(i).getBird_name(),firebaseAuth.getUid());

            batchIdentifiedBird.set(documentReference,recordByModel);

            batchIdentifiedBird.commit().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("++++++++++++++++++++++++ Didnt Worked: " + e);
                }
            });

            if(FlockExist){
                for(int k = 0; k < rewardPoints.size();k++) {

                    if(rewardPoints.get(k).getBird_status().matches(birdRewardModelList.get(i).getBird_status())){
                        flockScoreModel.setScorethisweek(rewardPoints.get(k).getReward_points() + flockScoreModel.getScorethisweek());
                        flockScoreModel.setScorethismonth(rewardPoints.get(k).getReward_points() + flockScoreModel.getScorethismonth());
                        flockScoreModel.setScorethisyear(rewardPoints.get(k).getReward_points() + flockScoreModel.getScorethisyear());
                        System.out.println("Worked Finished");
                    }
                }
            }
        }

        if(FlockExist){

            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
            String dataString = ft.format(date);

            WriteBatch batchFlockScore = firebaseFirestore.batch();
            documentReference = firebaseFirestore.collection("flockScore").document(flockId);
            batchFlockScore.update(documentReference,"scorethisweek",flockScoreModel.getScorethisweek());
            batchFlockScore.update(documentReference,"scorethismonth",flockScoreModel.getScorethismonth());
            batchFlockScore.update(documentReference,"scorethisyear",flockScoreModel.getScorethisyear());
            batchFlockScore.update(documentReference,"updated_at",dataString);
            batchFlockScore.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    System.out.println("Worked Finished");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Worked Failed : " + e);
                }
            });
        }
    }

    public void getFlockData(){

        firebaseFirestore.collection("flockMembers")
                .whereEqualTo("userId",auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        flockId = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().get("flockId").toString();
                        if(flockId == null){
                            setRewardPoints();
                            System.out.println("Worked Finished 1");
                            return;
                        }
                        firebaseFirestore
                                .collection("flocks")
                                .whereEqualTo("flockId",flockId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        String flockName = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().get("name").toString();
                                        firebaseFirestore
                                                .collection("flockScore")
                                                .whereEqualTo("flockname",flockName)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        FlockExist = true;
                                                        System.out.println("Worked Finished 2");
                                                        flockScoreModel = queryDocumentSnapshots.getDocumentChanges().get(0).getDocument().toObject(FlockScoreModel.class);
                                                        setRewardPoints();
                                                    }
                                                });


                                    }
                                });

                    }
                });
    }

    public void getCurrentFlockData(){

    }
}
