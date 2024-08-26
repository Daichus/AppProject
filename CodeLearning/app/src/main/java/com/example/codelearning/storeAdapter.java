package com.example.codelearning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class storeAdapter extends ArrayAdapter<Note> {

    public storeAdapter(@NonNull Context context, ArrayList<Note> notes) {
        super(context, 0,notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sample_view, parent, false);
        }

        Note notes = getItem(position);

        TextView tv_category = convertView.findViewById(R.id.tv_category);
        TextView tv_name = convertView.findViewById(R.id.tv_name);
        TextView tv_content = convertView.findViewById(R.id.tv_content);

        tv_category.setText(notes.getCategory());
        tv_name.setText(notes.getName());
        tv_content.setText(notes.getContent());
        return convertView;
    }
}

