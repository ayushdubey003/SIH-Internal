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

public class TeamAdapter extends ArrayAdapter<Team> {
    private Context mContext;
    private List<Team> mTeams;
    private TeamFragment TeamsFragment;

    public TeamAdapter(@NonNull Context context, @NonNull List<Team> objects, TeamFragment TeamsFragment) {
        super(context, 0, objects);
        mContext = context;
        mTeams = objects;
        this.TeamsFragment = TeamsFragment;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.generic_item, parent, false);
        TextView name = convertView.findViewById(R.id.name);
        TextView username = convertView.findViewById(R.id.username);
        ImageView del = convertView.findViewById(R.id.bin);

        name.setText(mTeams.get(position).getmTeamName());
        username.setText(mTeams.get(position).getmTeamLead());
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
                .setMessage("Do you want to remove Team?")
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Team Team = mTeams.get(position);
                        mTeams.remove(position);
                        TeamsFragment.updateArrayList((ArrayList) mTeams, Team);
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
