package com.lhy.pku.chatapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.lhy.pku.chatapp.Util.DateHelper;

import java.util.Date;

public class LatestMessage extends BaseObservable{

    private String content;
    private Date time;
    private String userID;

    @Bindable
    public String getContent() {
        return content;
    }

    @Bindable
    public Date getTime() {
        return time;
    }

    @Bindable
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(Date time) {
        this.time = time;
    }



    public String dateStr() {
        return time == null ? "" : DateHelper.formatDateToString(time);
    }
}
