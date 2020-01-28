package com.sih2020.sih.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih2020.sih.Adapters.JudgeAdapter;
import com.sih2020.sih.Adapters.TeamAdapter;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Models.Team;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamFragment extends Fragment {
    private EditText mTeamName, mTeamLead;
    private Button mAdd;
    private TextView mAddTv;
    private ProgressBar mProgress, mProgress2;
    private ListView mListView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Team> mTeams;
    private HashMap<Team, String> mHashMap;
    private TeamAdapter mAdapter;

    public TeamFragment() {
        // Required empty public constructor
    }

    public static TeamFragment newInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        listenToClicks();
        firebaseCalls();
    }

    private void firebaseCalls() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setVisibility(View.VISIBLE);
                mProgress2.setVisibility(View.GONE);
            }
        }, 2000);
        mDatabaseReference.child("Teams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Team team = dataSnapshot.getValue(Team.class);
                mTeams.add(team);
                mHashMap.put(team, dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
                mListView.setVisibility(View.VISIBLE);
                mProgress2.setVisibility(View.GONE);
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

    private void listenToClicks() {
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTeamName.getText().toString().trim().length() == 0 || mTeamLead.getText().toString().trim().length() == 0) {
                    Toast.makeText(getContext(), "Team name or lead is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                mAdd.setClickable(false);
                mAddTv.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mDatabaseReference.child("Teams").push().setValue(new Team(mTeamName.getText().toString().trim(), mTeamLead.getText().toString().trim(),null)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAdd.setClickable(true);
                        mAddTv.setVisibility(View.VISIBLE);
                        mProgress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            mTeamName.setText("");
                            mTeamLead.setText("");
                            Toast.makeText(getContext(), "Team added Successfully", Toast.LENGTH_LONG).show();
                            return;
                        }
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void init(View view) {
        mTeamName = view.findViewById(R.id.team_name);
        mTeamLead = view.findViewById(R.id.team_leader);
        mAdd = view.findViewById(R.id.add);
        mAddTv = view.findViewById(R.id.add_tv);
        mProgress = view.findViewById(R.id.progress);
        mListView = view.findViewById(R.id.list_view);
        mProgress2 = view.findViewById(R.id.progress2);
        mDatabase = AppConstants.getmDatabase();
        mDatabaseReference = AppConstants.getmDatabaseReference();
        mTeams = new ArrayList<>();
        mHashMap = new HashMap<>();
        mAdapter = new TeamAdapter(getContext(), mTeams, TeamFragment.this);
        mListView.setAdapter(mAdapter);
    }

    public void updateArrayList(final ArrayList<Team> arrayList, Team team) {
        mListView.setVisibility(View.GONE);
        mProgress2.setVisibility(View.VISIBLE);
        String key = mHashMap.get(team);
        mDatabaseReference.child("Teams").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mListView.setVisibility(View.VISIBLE);
                    mProgress2.setVisibility(View.GONE);
                    mTeams = arrayList;
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Task Completed Successfully", Toast.LENGTH_LONG).show();
                } else {
                    mListView.setVisibility(View.VISIBLE);
                    mProgress2.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Task Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
