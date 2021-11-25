package com.example.a321projectprototype.ui.Discover;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DiscoverViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public DiscoverViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Discover");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
