package com.example.votingapplication.ui.vote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VoteViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public VoteViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String name){
        mText.setValue(name);
    }
}