package com.example.a321projectprototype.ui.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;


public class SettingsFragment extends Fragment
{
    private SettingViewModel settingViewModel;
    private ImageView permissionButton;
    private ImageView termsButton;
    private NavController navController;
    private HomePage homePage;
    private ImageView backupImageView, autoImageView, securityImageView, usageImageView;
    private int backupClickNum, autoClickNum, securityClickNum, imageClickNum = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        permissionButton = root.findViewById(R.id.settings_triangleButton4);
        termsButton = root.findViewById(R.id.settings_triangleButton6);

        permissionButton.setOnClickListener(settingsPermissions);
        termsButton.setOnClickListener(settingsTerms);

        backupImageView = root.findViewById(R.id.settings_slideButtonDisable1);
        autoImageView = root.findViewById(R.id.settings_slideButtonEnable2);
        securityImageView = root.findViewById(R.id.settings_slideButtonEnable3);
        usageImageView = root.findViewById(R.id.settings_slideButtonDisable5);

        //set Listeners
        backupImageView.setOnClickListener(backupImageViewHandler);
        autoImageView.setOnClickListener(autoImageViewHandler);
        securityImageView.setOnClickListener(securityImageHandler);
        usageImageView.setOnClickListener(imageViewHandler);

        return root;
    }
    // Navigation
    private final View.OnClickListener settingsPermissions = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Settings_to_settingsPermissionFragment);
        }
    };

    private final View.OnClickListener settingsTerms = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            navController.navigate(R.id.action_nav_Settings_to_settingsTermFragment);
        }
    };
    // handle slide bar click events
    private final View.OnClickListener backupImageViewHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(backupClickNum%2 == 1){
                backupImageView.setImageResource(R.drawable.slidebutton2);
            }else{
                backupImageView.setImageResource(R.drawable.slidebutton1);
            }
            backupClickNum++;
        }
    };

    private final View.OnClickListener autoImageViewHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(autoClickNum%2 == 1){
                autoImageView.setImageResource(R.drawable.slidebutton1);
            }else{
                autoImageView.setImageResource(R.drawable.slidebutton2);
            }
            autoClickNum++;
        }
    };

    private final View.OnClickListener securityImageHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(securityClickNum%2 == 1){
                securityImageView.setImageResource(R.drawable.slidebutton1);
            }else{
                securityImageView.setImageResource(R.drawable.slidebutton2);
            }
            securityClickNum++;
        }
    };

    private final View.OnClickListener imageViewHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(imageClickNum%2 == 1){
                usageImageView.setImageResource(R.drawable.slidebutton2);
            }else{
                usageImageView.setImageResource(R.drawable.slidebutton1);
            }
            imageClickNum++;
        }
    };
}
