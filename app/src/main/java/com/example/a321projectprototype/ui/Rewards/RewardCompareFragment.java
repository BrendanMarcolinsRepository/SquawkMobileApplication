package com.example.a321projectprototype.ui.Rewards;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserScore;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RewardCompareFragment extends Fragment {

    private Spinner rewardSpinner;
    private ArrayList<String> times;
    private ArrayAdapter<String> adaptor;
    private View root;
    private ArrayList<UserScore> userRewards = new ArrayList<>();
    TextView rewardCompare1, rewardCompare2, rewardCompare3;
    private ArrayList<TextView> topThree;
    private RecyclerView userListRecycler;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public RewardCompareFragment() {
        // Required empty public constructor
    }

    public static RewardCompareFragment newInstance(String param1, String param2) {
        RewardCompareFragment fragment = new RewardCompareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_reward_compare, container, false);
        rewardSpinner = root.findViewById(R.id.reward_compare_spinner);
        loadSpinner();
        topThree = new ArrayList<>();

        rewardCompare1 = root.findViewById(R.id.reward_compare_name1);
        rewardCompare2 = root.findViewById(R.id.reward_compare_name2);
        rewardCompare3 = root.findViewById(R.id.reward_compare_name3);

        userListRecycler = root.findViewById(R.id.userListRecycler);

        loadUserRewardData();

        //spinner change
        rewardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                loadUserBars(time);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing Selected");
            }
        });

        return root;
    }
    //load user reward points data from firebase
    private void loadUserRewardData()
    {
        firebaseFirestore.collection("userScore").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : value.getDocumentChanges())
                {
                    if(documentChange.getType() == DocumentChange.Type.ADDED)
                    {
                        UserScore userRewardModel = documentChange.getDocument().toObject(UserScore.class);
                        userRewards.add(userRewardModel);
                    }
                }
                sortUsers();
                loadTopThree();
                loadUserBars("This Month");
            }
        });
    }
    //sort users by score
    public void sortUsers(){
        Collections.sort(userRewards, Collections.reverseOrder());
    }
    //load top three user profiles
    public void loadTopThree(){
        rewardCompare1.setText("#1 "+userRewards.get(0).getUsername()+"\n"+userRewards.get(0).getTotalScore());
        rewardCompare2.setText("#2 "+userRewards.get(1).getUsername()+"\n"+userRewards.get(1).getTotalScore());
        rewardCompare3.setText("#3 "+userRewards.get(2).getUsername()+"\n"+userRewards.get(2).getTotalScore());
        topThree.add(rewardCompare1);
        topThree.add(rewardCompare2);
        topThree.add(rewardCompare3);
    }
    //load time spinner
    public void loadSpinner(){
        times = new ArrayList<>();
        times.add("Total");
        times.add("This Week");
        times.add("This Month");
        times.add("This Year");
        adaptor = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, times);
        rewardSpinner.setAdapter(adaptor);
        rewardSpinner.setSelection(2);
    }
    //load list of user bars
    public void loadUserBars(String time)
    {
        RewardRecyclerAdaptor listAdaptor = new RewardRecyclerAdaptor(userRewards,time);
        userListRecycler.setAdapter(listAdaptor);
        userListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
