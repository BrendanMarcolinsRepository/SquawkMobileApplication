package com.example.a321projectprototype.ui.Rewards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RewardsViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public RewardsViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Rewards");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
