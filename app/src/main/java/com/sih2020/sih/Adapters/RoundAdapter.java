package com.sih2020.sih.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sih2020.sih.R;

import java.util.List;

public class RoundAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> objects;

    public RoundAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
        mContext = context;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.generic_item, parent, false);
        TextView name = convertView.findViewById(R.id.name);
        TextView username = convertView.findViewById(R.id.username);
        ImageView bin = convertView.findViewById(R.id.bin);
        bin.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_keyboard_arrow_right_black_24dp));
        username.setVisibility(View.GONE);
        name.setText(objects.get(position));
        return convertView;
    }
}
