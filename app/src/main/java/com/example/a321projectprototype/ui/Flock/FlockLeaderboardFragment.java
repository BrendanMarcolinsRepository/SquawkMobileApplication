package com.example.a321projectprototype.ui.Flock;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.FlockScoreModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FlockLeaderboardFragment  extends Fragment
{
    private String[] timeFrame = { "This Week","This Month", "This Year","All Time Scores"};
    private RecyclerView recyclerView;
    private HomePage homePage;
    private Spinner leaderBoardsSpinner;
    private List<FlockScoreModel> flockScoreList;
    private List<Integer>scoreList;
    private AdapterLeaderboard adapterFlock;
    private ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_flock_leaderboards, container, false);

        leaderBoardsSpinner = root.findViewById(R.id.leaderboards_Spinner);
        recyclerView = root.findViewById(R.id.recycleLeaderboard);
        progressBar = root.findViewById(R.id.progressBarLeaderboard);
        homePage = (HomePage) getActivity();
        scoreList = new ArrayList<>();

        leaderBoardsSpinner.setOnItemSelectedListener (spinnerLeaderBoardMethod);

        leaderBoardsSpinner.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        getData();






        return root;
    }


    private final AdapterView.OnItemSelectedListener  spinnerLeaderBoardMethod = new AdapterView.OnItemSelectedListener (){

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    sortDataByWeek();
                    break;
                case 1:
                    sortDataByMonth();
                    break;
                case 2:
                    sortDataByYear();
                    break;
                case 3:
                    sortDataByAllTime();
                    break;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData() {


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore
                .collection("flockScore")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    flockScoreList = new ArrayList<>();

                    for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if(documentChange.getType() == DocumentChange.Type.ADDED) {
                            flockScoreList.add(documentChange.getDocument().toObject(FlockScoreModel.class));
                        }
                    }

                    sortDataByAllTime();
                    setRecyclerViewMethod();

                });
    }

    private void setRecyclerViewMethod(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, timeFrame);
        leaderBoardsSpinner.setAdapter(adapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapterFlock = new  AdapterLeaderboard(flockScoreList,scoreList);
        recyclerView.setAdapter(adapterFlock);
        leaderBoardsSpinner.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortDataByWeek(){
        scoreList.clear();
        Collections.sort(flockScoreList, Collections.reverseOrder(Comparator.comparingInt(FlockScoreModel::getScorethismonth)));
        flockScoreList.forEach(i -> scoreList.add(i.getScorethisweek()));
        updateAdapterFlock();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortDataByMonth(){
        scoreList.clear();
        Collections.sort(flockScoreList, Collections.reverseOrder(Comparator.comparingInt(FlockScoreModel::getScorethismonth)));
        flockScoreList.forEach(i -> scoreList.add(i.getScorethismonth()));


        updateAdapterFlock();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortDataByYear(){
        scoreList.clear();
        Collections.sort(flockScoreList, Collections.reverseOrder(Comparator.comparingInt(FlockScoreModel::getScorethismonth)));
        flockScoreList.forEach(i -> scoreList.add(i.getScorethisyear()));
        updateAdapterFlock();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortDataByAllTime(){
        scoreList.clear();
        Collections.sort(flockScoreList, Collections.reverseOrder(Comparator.comparingInt(FlockScoreModel::getScorethismonth)));
        flockScoreList.forEach(i -> scoreList.add(i.getTotalScore()));
        updateAdapterFlock();
    }

    private void updateAdapterFlock() {
        if(adapterFlock != null){
            adapterFlock.notifyDataSetChanged();
        }
    }

}
