package com.sih2020.sih.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih2020.sih.Adapters.JudgeActivityAdapter;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Models.Judge;
import com.sih2020.sih.Models.Team;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.HashMap;

public class JudgeActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageView mLogOut;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    public static ArrayList<Team> mTeams;
    public static ArrayList<String> mKeys;
    public static String mUsername;
    private JudgeActivityAdapter mAdapter;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge);
        mListView = findViewById(R.id.list);
        mLogOut = findViewById(R.id.log_out);
        mProgress = findViewById(R.id.progress);
        mDatabase = AppConstants.getmDatabase();
        mDatabaseReference = AppConstants.getmDatabaseReference();
        mTeams = new ArrayList<>();
        mAdapter = new JudgeActivityAdapter(this, mTeams);
        mListView.setAdapter(mAdapter);
        mKeys = new ArrayList<>();

        final SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        mUsername = sharedPreferences.getString("username", "username");

        firebaseCalls();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JudgeActivity.this, RoundActivity.class);
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(JudgeActivity.this, MainActivity.class);
                intent.putExtra("First Time", 1);
                startActivity(intent);
                finish();
            }
        });
    }

    private void firebaseCalls() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
            }
        }, 5000);
        mDatabaseReference.child("Teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Team team = dataSnapshot.getValue(Team.class);
                mTeams.add(team);
                mKeys.add(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
                mListView.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
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
    }
}
