package com.sih2020.sih.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.sih2020.sih.Activities.MainActivity;
import com.sih2020.sih.Adapters.JudgeAdapter;
import com.sih2020.sih.Constants.AppConstants;
import com.sih2020.sih.Helpers.Encryptor;
import com.sih2020.sih.Models.Judge;
import com.sih2020.sih.R;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.sih2020.sih.Helpers.Encryptor.getSHA;

public class JudgesFragment extends Fragment {

    private EditText mName, mUserName, mPassword;
    private Button mSame, mAuto, mAdd;
    private TextView mAddTv;
    private ProgressBar mProgress, mProgress2;
    private ListView mListView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Judge> mJudges;
    private HashMap<Judge, String> mHashMap;
    private JudgeAdapter mJudgeAdapter;
    private String enc;

    public JudgesFragment() {
        // Required empty public constructor
    }

    public static JudgesFragment newInstance() {
        JudgesFragment fragment = new JudgesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        mName = view.findViewById(R.id.name);
        mUserName = view.findViewById(R.id.username);
        mPassword = view.findViewById(R.id.password);
        mSame = view.findViewById(R.id.use_same);
        mAuto = view.findViewById(R.id.auto);
        mAdd = view.findViewById(R.id.add);
        mAddTv = view.findViewById(R.id.add_tv);
        mProgress = view.findViewById(R.id.progress);
        mListView = view.findViewById(R.id.list_view);
        mProgress2 = view.findViewById(R.id.progress2);
        mJudges = new ArrayList<>();
        mJudgeAdapter = new JudgeAdapter(getContext(), mJudges, JudgesFragment.this);
        mListView.setAdapter(mJudgeAdapter);
        mHashMap = new HashMap<>();

        mDatabase = AppConstants.getmDatabase();
        mDatabaseReference = AppConstants.getmDatabaseReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_judges, container, false);
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
        mDatabaseReference.child("Judges").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Judge judge = dataSnapshot.getValue(Judge.class);
                mJudges.add(judge);
                mHashMap.put(judge, dataSnapshot.getKey());
                mJudgeAdapter.notifyDataSetChanged();
                mListView.setVisibility(View.VISIBLE);
                mProgress2.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Judge judge = dataSnapshot.getValue(Judge.class);
                mJudges.remove(judge);
                mJudgeAdapter.notifyDataSetChanged();
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
        mSame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().trim().length() == 0)
                    Toast.makeText(getActivity().getApplicationContext(), "Name is empty", Toast.LENGTH_LONG).show();
                else
                    mUserName.setText(mName.getText());
            }
        });

        mAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword.setText(generatePassword());
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Name is empty", Toast.LENGTH_LONG).show();
                    return;
                } else if (mPassword.getText().toString().length() < 6) {
                    Toast.makeText(getActivity().getApplicationContext(), "Password must be at least 6 characters long", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mUserName.getText().toString().trim().length() == 0)
                    mUserName.setText(mName.getText());
                mAdd.setClickable(false);
                mAddTv.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                try {
                    if (MainActivity.mJudgeData.containsKey(mUserName.getText().toString())) {
                        Toast.makeText(getContext(), "Username Already Exists", Toast.LENGTH_LONG).show();
                        mAdd.setClickable(true);
                        mAddTv.setVisibility(View.VISIBLE);
                        mProgress.setVisibility(View.GONE);
                        return;
                    }
                    enc = Encryptor.toHexString(getSHA(mPassword.getText().toString()));
                    Log.e("Test", "Hey " + enc);
                    mDatabaseReference.child("Judges").push().setValue(new Judge(mName.getText().toString(), mUserName.getText().toString(), enc)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Judge Added", Toast.LENGTH_LONG).show();
                                    mName.setText("");
                                    mPassword.setText("");
                                    mUserName.setText("");
                                } else {
                                    task.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                            mAdd.setClickable(true);
                            mAddTv.setVisibility(View.VISIBLE);
                            mProgress.setVisibility(View.GONE);
                        }
                    });
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Hey", parent.getItemIdAtPosition(position) + " " + id + " " + R.id.bin);
            }
        });
    }

    private String generatePassword() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 48; i <= 57; i++)
            values.add(i);
        for (int i = 65; i <= 90; i++)
            values.add(i);
        for (int i = 97; i <= 122; i++)
            values.add(i);
        String password = "";
        while (password.length() < 6) {
            int x = values.get((int) (Math.random() * values.size()));
            char ch = (char) x;
            password = password + Character.toString(ch);
        }
        return password;
    }

    public void updateArrayList(final ArrayList<Judge> arrayList, Judge judge) {
        mListView.setVisibility(View.GONE);
        mProgress2.setVisibility(View.VISIBLE);
        String key = mHashMap.get(judge);
        mDatabaseReference.child("Judges").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mListView.setVisibility(View.VISIBLE);
                    mProgress2.setVisibility(View.GONE);
                    mJudges = arrayList;
                    mJudgeAdapter.notifyDataSetChanged();
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
