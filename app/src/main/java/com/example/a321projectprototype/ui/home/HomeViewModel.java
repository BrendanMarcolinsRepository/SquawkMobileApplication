package com.example.a321projectprototype.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a321projectprototype.HomePage;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> welcome;
    private String name;

    private HomeFragment homeFragment;

    public HomeViewModel()
    {
        welcome = new MutableLiveData<>();
        homeFragment = new HomeFragment();

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