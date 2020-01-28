package com.sih2020.sih.Constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppConstants {
    private static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mDatabaseReference;

    public static FirebaseDatabase getmDatabase() {
        return mDatabase;
    }

    public static DatabaseReference getmDatabaseReference() {
        mDatabaseReference = mDatabase.getReference();
        return mDatabaseReference;
    }
}
