package com.example.mindsound.ui.dashboard;

import android.bluetooth.BluetoothDevice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public void setmText(String text) {
        mText.setValue(text);
    }

    public LiveData<String> getText() {
        return mText;
    }
}