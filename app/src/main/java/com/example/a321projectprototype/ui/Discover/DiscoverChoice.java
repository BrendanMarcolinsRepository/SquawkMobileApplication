package com.example.a321projectprototype.ui.Discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a321projectprototype.R;

public class DiscoverChoice extends Fragment
{

    private View root;
    private int bird;
    private TextView birdNameTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        root = inflater.inflate(R.layout.fragment_discover_choice, container, false);

        String s = getArguments().getString("birdName");

        System.out.println(s);


        birdNameTextView = root.findViewById(R.id.BirdTextview);
        birdNameTextView.setText(s);

        return root;
    }


}
