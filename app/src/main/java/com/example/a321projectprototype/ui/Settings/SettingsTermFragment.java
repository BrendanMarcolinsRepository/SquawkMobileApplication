package com.example.a321projectprototype.ui.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a321projectprototype.R;

public class SettingsTermFragment extends Fragment {

    public SettingsTermFragment() {
        // Required empty public constructor
    }

    public static SettingsTermFragment newInstance() {
        SettingsTermFragment fragment = new SettingsTermFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_term, container, false);
    }
}
