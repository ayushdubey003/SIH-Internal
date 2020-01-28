package com.sih2020.sih.Models;

import androidx.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

public class Parameter {
    private String mParameterName;
    public Parameter()
    {

    }

    public Parameter(String mParameterName) {
        this.mParameterName = mParameterName;
    }

    public String getmParameterName() {
        return mParameterName;
    }
}
