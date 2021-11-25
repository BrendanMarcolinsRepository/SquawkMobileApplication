package com.example.a321projectprototype.ui.Flock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FlockViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public FlockViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Flock");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
