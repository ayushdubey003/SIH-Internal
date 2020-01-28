package com.sih2020.sih.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sih2020.sih.Models.Parameter;
import com.sih2020.sih.Models.Score;
import com.sih2020.sih.R;

import java.util.List;

public class AnotherAdapter extends ArrayAdapter<Score> {
    private Context mContext;
    private List<Score> parameters;
    public View view;

    public AnotherAdapter(@NonNull Context context, @NonNull List<Score> objects) {
        super(context, 0, objects);
        mContext = context;
        parameters = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.score_item, parent, false);

        TextView parameter = convertView.findViewById(R.id.parameter);
        TextView points = convertView.findViewById(R.id.points);
        EditText editText = convertView.findViewById(R.id.editBox);

        editText.setVisibility(View.GONE);

        parameter.setText(parameters.get(position).getmParameter());
        String score = parameters.get(position).getmScore() + "";
        points.setText(score);
        view = convertView;
        return convertView;
    }
}
