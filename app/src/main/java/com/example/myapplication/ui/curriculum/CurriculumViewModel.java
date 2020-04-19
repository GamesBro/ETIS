package com.example.myapplication.ui.curriculum;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurriculumViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CurriculumViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}