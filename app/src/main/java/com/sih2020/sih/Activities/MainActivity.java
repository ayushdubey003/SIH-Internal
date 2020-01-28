package com.sih2020.sih.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Helpers.Encryptor;
import com.sih2020.sih.R;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RadioButton mAdmin, mJudge;
    private EditText mUsername, mPassword;
    private Button mLogin;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private String mAdminUsername, mAdminPassword;
    private SharedPreferences mSharedPrefs;
    public static HashMap<String, String> mJudgeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fetchAdminData();
        fetchJudgesData();
        receiveClicks();
    }

    private void fetchJudgesData() {
    }

    private void fetchAdminData() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Initializing App data");
        if (!mSharedPrefs.getBoolean("isLoggedIn", false) && getIntent().getIntExtra("First Time", 0) == 0)
            progressDialog.show();

        mDatabaseReference.child("Admin").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equalsIgnoreCase("username"))
                    mAdminUsername = dataSnapshot.getValue(String.class);
                if (dataSnapshot.getKey().equalsIgnoreCase("password"))
                    mAdminPassword = dataSnapshot.getValue(String.class);
                if (mAdminPassword != null && mAdminUsername != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ProgressDialog progressDialog1 = new ProgressDialog(MainActivity.this);
        progressDialog1.setMessage("Initializing App data");
        if (!mSharedPrefs.getBoolean("isLoggedIn", false) && getIntent().getIntExtra("First Time", 0) == 0)
            progressDialog1.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing())
                    progressDialog.hide();
                if (progressDialog1.isShowing())
                    progressDialog1.hide();
            }
        }, 2000);
        mDatabaseReference.child("Judges").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String tempU = "", tempP = "";
                tempU = dataSnapshot.child("mUsername").getValue(String.class);
                tempP = dataSnapshot.child("mPassword").getValue(String.class);

                mJudgeData.put(tempU, tempP);
                if (progressDialog1.isShowing()) {
                    progressDialog1.hide();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String tempU = "", tempP = "";
                tempU = dataSnapshot.child("mUsername").getValue(String.class);
                tempP = dataSnapshot.child("mPassword").getValue(String.class);

                mJudgeData.remove(tempU);
                if (progressDialog1.isShowing())
                    progressDialog1.hide();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void receiveClicks() {
        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdmin.setChecked(true);
                mJudge.setChecked(false);
            }
        });

        mJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdmin.setChecked(false);
                mJudge.setChecked(true);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                if (username.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean admin = mAdmin.isChecked();
                boolean judge = mJudge.isChecked();

                //username : Admin
                //password : sih@2020

                if (admin) {
                    String enc = "";
                    try {
                        enc = Encryptor.toHexString(Encryptor.getSHA(password));
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!username.equals(mAdminUsername) || !enc.equals(mAdminPassword))
                        Toast.makeText(getApplicationContext(), "Entered username or password is invalid", Toast.LENGTH_LONG).show();
                    else if (username.equals(mAdminUsername) && enc.equals(mAdminPassword)) {
                        SharedPreferences.Editor editor = mSharedPrefs.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putInt("type", 0);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Verified", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, AdminActivity.class));
                        finish();
                    }
                } else {
                    String enc = "";
                    try {
                        enc = Encryptor.toHexString(Encryptor.getSHA(password));
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mJudgeData.containsKey(username)) {
                        if (mJudgeData.get(username).equals(enc)) {
                            SharedPreferences.Editor editor = mSharedPrefs.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putInt("type", 1);
                            editor.putString("username", username);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Verified", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, JudgeActivity.class));
                            finish();
                        }
                    } else
                        Toast.makeText(getApplicationContext(), "No such Judge", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void init() {
        mAdmin = findViewById(R.id.admin);
        mJudge = findViewById(R.id.judge);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mDatabase = AppConstants.getmDatabase();
//        mDatabase.setPersistenceEnabled(true);
//        if (getIntent().getIntExtra("First Time", 0) == 0)
//            mDatabase.setPersistenceEnabled(true);
        mDatabaseReference = AppConstants.getmDatabaseReference();
        mJudgeData = new HashMap<>();
        mSharedPrefs = getSharedPreferences("loggedIn", MODE_PRIVATE);

        boolean loggedIn = mSharedPrefs.getBoolean("isLoggedIn", false);
        if (loggedIn) {
            int type = mSharedPrefs.getInt("type", 0);
            if (type == 0) {
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, JudgeActivity.class));
                finish();
            }
        }
        mAdmin.setChecked(true);
    }
}
