package com.lhy.pku.chatapp.model;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Message implements Comparable<Message>{

    private String content;
    private DocumentReference from;
    private Date time;

    public Date getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public DocumentReference getFrom() {
        return from;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFrom(DocumentReference from) {
        this.from = from;
    }
    @Override
    public int compareTo(@NonNull Message o) {
        return getTime().compareTo(o.getTime());
    }

}
