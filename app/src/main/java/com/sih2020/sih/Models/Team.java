package com.sih2020.sih.Models;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class Team {
    private String mTeamName;
    private String mTeamLead;
    private HashMap<String, HashMap<String, HashMap<String, Double>>> mRoundScores;

    public Team() {

    }

    public String getmTeamName() {
        return mTeamName;
    }

    public String getmTeamLead() {
        return mTeamLead;
    }

    public Team(String mTeamName, String mTeamLead, HashMap<String, HashMap<String, HashMap<String, Double>>> mRoundScores) {
        this.mTeamName = mTeamName;
        this.mTeamLead = mTeamLead;
        this.mRoundScores = mRoundScores;
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> getmRoundScores() {
        return mRoundScores;
    }
}
