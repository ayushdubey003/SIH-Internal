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
import com.sih2020.sih.R;

import java.util.List;

public class ScoreAdapter extends ArrayAdapter<Parameter> {
    private Context mContext;
    private int mCaller;
    private List<Parameter> parameters;
    public View view;

    public ScoreAdapter(@NonNull Context context, @NonNull List<Parameter> objects, int mCaller) {
        super(context, 0, objects);
        mContext = context;
        this.mCaller = mCaller;
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

        if (mCaller == 0)
            points.setVisibility(View.GONE);
        else
            editText.setVisibility(View.GONE);

        parameter.setText(parameters.get(position).getmParameterName());
        view = convertView;
        return convertView;
    }
}
