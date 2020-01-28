package com.sih2020.sih.Models;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

public class Judge {
    private String mName;
    private String mUsername;
    private String mPassword;

    public Judge() {

    }

    public Judge(String mName, String mUsername, String mPassword) {
        this.mName = mName;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }

    public String getmName() {
        return mName;
    }

    public String getmUsername() {
        return mUsername;
    }

    public String getmPassword() {
        return mPassword;
    }

}
