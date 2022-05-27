package com.example.a321projectprototype.ui.Settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a321projectprototype.R;
//Code by Rui Cao
public class SettingsPermissionFragment extends Fragment {

    private ImageView mediaImageView, fileImageView, storageImageView, audioImageView;
    private int mediaClick, fileClick, storageClick, audioClick = 1;

    public SettingsPermissionFragment() {
        // Required empty public constructor
    }

    public static SettingsPermissionFragment newInstance() {
        SettingsPermissionFragment fragment = new SettingsPermissionFragment();
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
        View root = inflater.inflate(R.layout.fragment_settings_permission, container, false);
        mediaImageView = root.findViewById(R.id.permissions_slideButtonDisable1);
        fileImageView = root.findViewById(R.id.permissions_slideButtonEnable2);
        storageImageView = root.findViewById(R.id.permissions_slideButtonEnable3);
        audioImageView = root.findViewById(R.id.permissions_slideButtonDisable4);
        //set Listeners
        mediaImageView.setOnClickListener(mediaImageViewHandler);
        fileImageView.setOnClickListener(fileImageViewHandler);
        storageImageView.setOnClickListener(storageImageHandler);
        audioImageView.setOnClickListener(audioViewHandler);

        return root;
    }
    // handle slide bar click events
    private final View.OnClickListener mediaImageViewHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mediaClick%2 == 1){
                mediaImageView.setImageResource(R.drawable.slidebutton2);
            }else{
                mediaImageView.setImageResource(R.drawable.slidebutton1);
            }
            mediaClick++;
        }
    };

    private final View.OnClickListener fileImageViewHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(fileClick%2 == 1){
                fileImageView.setImageResource(R.drawable.slidebutton1);
            }else{
                fileImageView.setImageResource(R.drawable.slidebutton2);
            }
            fileClick++;
        }
    };

    private final View.OnClickListener storageImageHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(storageClick%2 == 1){
                storageImageView.setImageResource(R.drawable.slidebutton1);
            }else{
                storageImageView.setImageResource(R.drawable.slidebutton2);
            }
            storageClick++;
        }
    };

    private final View.OnClickListener audioViewHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(audioClick%2 == 1){
                audioImageView.setImageResource(R.drawable.slidebutton2);
            }else{
                audioImageView.setImageResource(R.drawable.slidebutton1);
            }
            audioClick++;
        }
    };
}
