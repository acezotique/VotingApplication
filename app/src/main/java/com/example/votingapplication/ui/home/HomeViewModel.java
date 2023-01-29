package com.example.votingapplication.ui.home;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText = new MutableLiveData<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome, ");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String name){
        mText = new MutableLiveData<>();
        mText.setValue("Welcome, " + name);
    }
    public void setFaculty(String name){
        mText = new MutableLiveData<>();
        mText.setValue(name);
    }
}