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

import com.sih2020.sih.Fragments.TeamFragment;
import com.sih2020.sih.Models.Team;
import com.sih2020.sih.R;

import java.util.ArrayList;
import java.util.List;

public class JudgeActivityAdapter extends ArrayAdapter<Team> {
    private Context mContext;
    private List<Team> mTeams;

    public JudgeActivityAdapter(@NonNull Context context, @NonNull List<Team> objects) {
        super(context, 0, objects);
        mContext = context;
        mTeams = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.generic_item, parent, false);
        TextView name = convertView.findViewById(R.id.name);
        TextView username = convertView.findViewById(R.id.username);
        ImageView im = convertView.findViewById(R.id.bin);

        name.setText(mTeams.get(position).getmTeamName());
        username.setText(mTeams.get(position).getmTeamLead());
        im.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));
        return convertView;
    }
}

