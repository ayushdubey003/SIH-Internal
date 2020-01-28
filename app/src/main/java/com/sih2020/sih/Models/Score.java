package com.sih2020.sih.Models;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

public class Score {
    private String mParameter;
    private double mScore;

    public Score(String mParameter, double mScore) {
        this.mParameter = mParameter;
        this.mScore = mScore;
    }

    public String getmParameter() {
        return mParameter;
    }

    public double getmScore() {
        return mScore;
    }
}
