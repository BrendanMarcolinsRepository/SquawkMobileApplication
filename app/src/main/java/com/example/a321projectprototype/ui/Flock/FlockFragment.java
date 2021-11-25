package com.example.a321projectprototype.ui.Flock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.a321projectprototype.R;


public class FlockFragment extends Fragment
{
    private FlockViewModel flockViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        flockViewModel = new ViewModelProvider(this).get(FlockViewModel.class);
        View root = inflater.inflate(R.layout.fragment_flock, container, false);
        final TextView textView = root.findViewById(R.id.text_flocks);

        flockViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
