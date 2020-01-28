package com.sih2020.sih.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sih2020.sih.Adapters.ScoreAdapter;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Models.Judge;
import com.sih2020.sih.Models.Parameter;
import com.sih2020.sih.Models.Team;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AddNewScore extends AppCompatActivity {

    private TextView mToolbarText;
    private ImageView mBack, mDone;
    private int position;
    private Button mAdd;
    private ListView mList;
    private ProgressBar mProgress;
    private ScoreAdapter mAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Parameter> mParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_score);
        init();
        fetchParameters();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Double> scores = new HashMap<>();
                for (int i = 0; i < mParameters.size(); i++) {
                    View view = mList.getChildAt(i);
                    EditText editText = view.findViewById(R.id.editBox);
                    TextView textView = view.findViewById(R.id.parameter);
                    Log.e("Here", "Called");
                    Log.e("Log", editText.getText().toString().trim() + " hhh");
                    if (editText.getText().toString().trim().length() == 0)
                        break;
                    scores.put(textView.getText().toString(), Double.parseDouble(editText.getText().toString().trim()));
                }
                HashMap<String, HashMap<String, Double>> roundWise = new HashMap<>();
                roundWise.put("Round " + (RoundActivity.mRoundNames.size() + 1), scores);

                HashMap<String, HashMap<String, HashMap<String, Double>>> finalScores = new HashMap<>();
                finalScores.put(JudgeActivity.mUsername, roundWise);
                if (scores.size() < mParameters.size()) {
                    Toast.makeText(getApplicationContext(), "Please fill scores for all parameters", Toast.LENGTH_LONG).show();
                    return;
                }
                mList.setAlpha(0.2f);
                mBack.setClickable(false);
                mDone.setClickable(false);
                mProgress.setVisibility(View.VISIBLE);
                mDatabaseReference.child("Teams").child(JudgeActivity.mKeys.get(position)).setValue(new Team(
                        JudgeActivity.mTeams.get(position).getmTeamName(),
                        JudgeActivity.mTeams.get(position).getmTeamLead(),
                        finalScores
                )).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            finish();
                        else {
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void fetchParameters() {
        mDatabaseReference.child("Parameters").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mParameters.add(dataSnapshot.getValue(Parameter.class));
                mAdapter.notifyDataSetChanged();
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

    private void init() {
        position = getIntent().getIntExtra("Position", 0);
        mToolbarText = findViewById(R.id.toolbar);
        mDone = findViewById(R.id.done);
        mBack = findViewById(R.id.back);
        mList = findViewById(R.id.round_score);
        mAdd = findViewById(R.id.add);
        mProgress = findViewById(R.id.progress_circle);
        mToolbarText.setText(JudgeActivity.mTeams.get(position).getmTeamName());
        mDatabase = AppConstants.getmDatabase();
        mDatabaseReference = AppConstants.getmDatabaseReference();
        mParameters = new ArrayList<>();
        mAdapter = new ScoreAdapter(this, mParameters, 0);
        mList.setAdapter(mAdapter);
    }
}
