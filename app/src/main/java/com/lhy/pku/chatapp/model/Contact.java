package com.lhy.pku.chatapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class Contact extends BaseObservable {

    private String userID;

    @Bindable
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
