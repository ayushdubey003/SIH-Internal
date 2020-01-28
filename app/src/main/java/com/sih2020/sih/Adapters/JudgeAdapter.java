package com.sih2020.sih.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.sih2020.sih.Fragments.JudgesFragment;
import com.sih2020.sih.Models.Judge;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.List;

public class JudgeAdapter extends ArrayAdapter<Judge> {
    private Context mContext;
    private List<Judge> mJudges;
    private JudgesFragment judgesFragment;

    public JudgeAdapter(@NonNull Context context, @NonNull List<Judge> objects, JudgesFragment judgesFragment) {
        super(context, 0, objects);
        mContext = context;
        mJudges = objects;
        this.judgesFragment = judgesFragment;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.generic_item, parent, false);
        TextView name = convertView.findViewById(R.id.name);
        TextView username = convertView.findViewById(R.id.username);
        ImageView del = convertView.findViewById(R.id.bin);

        name.setText(mJudges.get(position).getmName());
        username.setText(mJudges.get(position).getmUsername());
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption(position);
                diaBox.show();
            }
        });
        return convertView;
    }


    private AlertDialog AskOption(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("Delete")
                .setMessage("Do you want to remove Judge?")
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Judge judge = mJudges.get(position);
                        mJudges.remove(position);
                        judgesFragment.updateArrayList((ArrayList) mJudges, judge);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return alertDialog;
    }
}
