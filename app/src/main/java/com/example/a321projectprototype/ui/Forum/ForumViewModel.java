package com.example.a321projectprototype.ui.Forum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ForumViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public ForumViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Forum");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
