package com.example.a321projectprototype.ui.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a321projectprototype.ui.home.HomeFragment;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> welcome;
    private String name;

    private ProfileFragment profileFragment;

    public ProfileViewModel()
    {
        welcome = new MutableLiveData<>();
        profileFragment = new ProfileFragment();

    }



    public void setName(String name)
    {
        this.name = name;
        welcome.setValue("Welcome: " + name);
    }

    public LiveData<String> getText() {
        return welcome;
    }
}