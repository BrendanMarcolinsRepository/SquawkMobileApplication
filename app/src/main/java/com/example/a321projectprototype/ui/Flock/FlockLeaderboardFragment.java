package com.example.a321projectprototype.ui.Flock;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.Database.FlockDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FlockLeaderboardFragment  extends Fragment
{
    private String[] timeFrame = { "Today", "This Week","This Month",
            "This Year"};
    private List<String> dateList;
    private RecyclerView recyclerView;
    private HomePage homePage;
    private FlockDatabase flockDatabase;
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
        flockDatabase = new FlockDatabase(homePage);



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

        flockList = flockDatabase.getAllUsers();
        FlockModelData flockModelData = flockDatabase.getFlock(homePage.getUserModel().getUserFlock());
       // System.out.println("Flock name 1 " + flockModelData.getName());

        adapterFlock = new  AdapterLeaderboard(flockList);
        recyclerView.setAdapter(adapterFlock);




        return root;
    }

}
