package com.example.a321projectprototype.ui.Flock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

import java.util.ArrayList;
import java.util.List;

public class FlockLeaderboardFragment  extends Fragment
{
    private String[] timeFrame = { "Today", "This Week","This Month",
            "This Year"};
    private List<String> dateList;
    private RecyclerView recyclerView;
    private HomePage homePage;

    private List<FlockModelData> flockList;
    private AdapterLeaderboard adapterFlock;


    private Spinner spinner;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_flock_leaderboards, container, false);

        spinner = root.findViewById(R.id.leaderboards_Spinner);
        recyclerView = root.findViewById(R.id.recycleLeaderboard);
        homePage = (HomePage) getActivity();




        dateList = new ArrayList<>();
        dateList.add("Today");
        dateList.add("This week");
        dateList.add("THis Month");
        dateList.add("This Year");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.flock_leaderboard_selected, timeFrame);
        adapter.setDropDownViewResource(R.layout.flock_leaderboard_spinner);
        spinner.setAdapter(adapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


       // System.out.println("Flock name 1 " + flockModelData.getName());

        adapterFlock = new  AdapterLeaderboard(flockList);
        recyclerView.setAdapter(adapterFlock);




        return root;
    }

}
