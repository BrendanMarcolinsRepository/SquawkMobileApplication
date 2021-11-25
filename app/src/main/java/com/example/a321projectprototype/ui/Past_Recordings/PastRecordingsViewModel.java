package com.example.a321projectprototype.ui.Past_Recordings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PastRecordingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PastRecordingsViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Past Recordings");
    }

    public LiveData<String> getText() {
        return mText;
    }
}