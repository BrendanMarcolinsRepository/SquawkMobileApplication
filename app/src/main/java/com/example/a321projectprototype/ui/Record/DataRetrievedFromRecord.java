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
import com.example.a321projectprototype.User.ItemDataModel;
import com.example.a321projectprototype.User.RewardPointsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
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
        firebaseFirestore.collection("bird").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override

                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

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
                    }
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
        while(counter < MAX_IDENTIFIER)
        {
            int randomInteger = randomObject.nextInt(23);

            System.out.println("list size here +++++++++++++++ : " + birdRewardModelList.size());

            if(counter == 0)
            {
                numberBirds.add(randomInteger);
                tempBirdList.add(birdRewardModelList.get(randomInteger));
            }

            for(int i = 0; i < numberBirds.size(); i++)
            {
                if(numberBirds.get(i) != randomInteger)
                {
                    numberBirds.add(randomInteger);
                    tempBirdList.add(birdRewardModelList.get(randomInteger));
                }
            }

            counter++;
        }

        birdRewardModelList = null;
        birdRewardModelList = tempBirdList;
    }

    public void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recordDataCardViewAdapter = new RecordDataCardViewAdapter(birdRewardModelList,homePage,rewardPoints);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recordDataCardViewAdapter);
    }
}
