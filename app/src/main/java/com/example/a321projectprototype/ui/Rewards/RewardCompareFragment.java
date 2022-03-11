package com.example.a321projectprototype.ui.Rewards;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.cardview.widget.CardView;

import com.example.a321projectprototype.R;

import java.util.ArrayList;

public class RewardCompareFragment extends Fragment {

    private Spinner rewardSpinner;
    private ArrayList<String> times;
    private ArrayAdapter<String> adaptor;
    private CardView cardView;
    private View root;

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
        times = new ArrayList<>();
        times.add("Today");
        times.add("This Week");
        times.add("This Month");
        times.add("Last Week");
        times.add("This Year");
        adaptor = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, times);
        rewardSpinner.setAdapter(adaptor);
        return root;
    }

    public void showCompareBar(View view){
        cardView = root.findViewById(R.id.reward_compare_userTxt);
        cardView.setVisibility(View.VISIBLE);
    }
}
