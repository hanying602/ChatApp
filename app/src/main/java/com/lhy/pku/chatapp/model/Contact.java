package com.lhy.pku.chatapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.firestore.DocumentReference;

public class Contact extends BaseObservable {

    private String userID;
    private DocumentReference userRef;

    @Bindable
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DocumentReference userRef) {
        this.userRef = userRef;
    }
}
