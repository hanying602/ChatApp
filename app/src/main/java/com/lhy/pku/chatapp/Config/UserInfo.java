package com.lhy.pku.chatapp.Config;

import com.google.firebase.firestore.DocumentReference;

public class UserInfo {

    private static UserInfo userInfo = null;
    private DocumentReference userReference;
    private UserInfo() {

    }

    public static synchronized UserInfo getInstance() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return(userInfo);
    }

    public DocumentReference getUserReference() {
        return userReference;
    }

    public void setUserReference(DocumentReference userReference) {
        this.userReference = userReference;
    }
}
