package com.example.a321projectprototype.ui.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;


public class SettingsFragment extends Fragment
{
    private SettingViewModel settingViewModel;
    private ImageView permissionButton;
    private ImageView termsButton;
    private NavController navController;
    private HomePage homePage;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        homePage = (HomePage) getActivity();
        navController = homePage.getNav();

        permissionButton = root.findViewById(R.id.settings_triangleButton4);
        termsButton = root.findViewById(R.id.settings_triangleButton6);

        permissionButton.setOnClickListener(settingsPermissions);
        termsButton.setOnClickListener(settingsTerms);

        return root;
    }
    // Navigations
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
}
