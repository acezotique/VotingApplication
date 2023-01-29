package com.example.votingapplication.ui.candidate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CandidateViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CandidateViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}