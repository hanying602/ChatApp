package com.lhy.pku.chatapp.Config;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserInfo {

    private static UserInfo userInfo = null;
    private DocumentSnapshot userSnapshot;
    private UserInfo() {

    }

    public static synchronized UserInfo getInstance() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return(userInfo);
    }

    public DocumentSnapshot getUserSnapshot() {
        return userSnapshot;
    }

    public void setUserSnapshot(DocumentSnapshot userSnapshot) {
        this.userSnapshot = userSnapshot;
    }

    public void clearAllData(){
        userSnapshot=null;
    }
}
