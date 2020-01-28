package com.sih2020.sih.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sih2020.sih.Adapters.AnotherAdapter;
import com.sih2020.sih.Adapters.ScoreAdapter;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Models.Parameter;
import com.sih2020.sih.Models.Score;
import com.sih2020.sih.Models.Team;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class SeeScores extends AppCompatActivity {
    private TextView mToolbarText;
    private ImageView mBack;
    private int position;
    private String roundPosition;
    private Button mAdd;
    private ListView mList;
    private ProgressBar mProgress;
    private AnotherAdapter mAdapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Score> mParameters;
    private TextView mScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_scores);
        init();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        position = getIntent().getIntExtra("Position", 0);
        roundPosition = getIntent().getStringExtra("Round Position");
        mToolbarText = findViewById(R.id.toolbar);
        mBack = findViewById(R.id.back);
        mList = findViewById(R.id.round_score);
        mAdd = findViewById(R.id.add);
        mProgress = findViewById(R.id.progress_circle);
        mScores = findViewById(R.id.score);
        mToolbarText.setText(JudgeActivity.mTeams.get(position).getmTeamName());
        mDatabase = AppConstants.getmDatabase();
        mDatabaseReference = AppConstants.getmDatabaseReference();
        mParameters = new ArrayList<>();
        Set<String> set = RoundActivity.team.getmRoundScores().get(JudgeActivity.mUsername).get(roundPosition).keySet();
        Iterator value = set.iterator();
        double ts = 0.0;
        while (value.hasNext()) {
            String key = (String) value.next();
            mParameters.add(new Score(key, (Double) RoundActivity.team.getmRoundScores().get(JudgeActivity.mUsername).get(roundPosition).get(key)));
            ts = ts + (Double) RoundActivity.team.getmRoundScores().get(JudgeActivity.mUsername).get(roundPosition).get(key);
        }
        String u = ts + "";
        mScores.setText(u);
        Log.e("Hey", set.size() + "");
        mAdapter = new AnotherAdapter(this, mParameters);
        mList.setAdapter(mAdapter);
    }
}
