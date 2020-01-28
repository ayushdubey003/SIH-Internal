package com.sih2020.sih.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.sih2020.sih.Adapters.RoundAdapter;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Models.Team;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RoundActivity extends AppCompatActivity {

    private TextView mToolbarText;
    private ImageView mBack;
    private int position;
    private Button mAdd;
    private ListView mList;
    public static ArrayList<String> mRoundNames;
    public static Team team;
    private RoundAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        init();
        receiveClicks();
        AppConstants.getmDatabaseReference().child("Teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(JudgeActivity.mKeys.get(position))) {
                    team = dataSnapshot.getValue(Team.class);
                    try {
                        Set<String> set = team.getmRoundScores().get(JudgeActivity.mUsername).keySet();
                        for (String item : set) mRoundNames.add(item);
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {

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
    }

    private void receiveClicks() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoundActivity.this, AddNewScore.class);
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(RoundActivity.this, SeeScores.class);
                intent.putExtra("Round Position", mRoundNames.get(pos));
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });
    }

    private void init() {
        position = getIntent().getIntExtra("Position", 0);
        mToolbarText = findViewById(R.id.toolbar);
        mBack = findViewById(R.id.back);
        mList = findViewById(R.id.round_scores);
        mAdd = findViewById(R.id.add);
        mRoundNames = new ArrayList<>();
        mToolbarText.setText(JudgeActivity.mTeams.get(position).getmTeamName());
        mAdapter = new RoundAdapter(getApplicationContext(), mRoundNames);
        mList.setAdapter(mAdapter);
    }
}
