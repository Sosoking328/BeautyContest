package com.sosokan.android.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.sosokan.android.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

    private Activity context;
    private String[] deer;
    private int resource;

    public CustomSpinnerAdapter(Activity context, int resource, String[] deer) {
        super(context, resource, deer);
        this.context = context;
        this.deer = deer;
        this.resource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            holder = new ViewHolder();
            row = context.getLayoutInflater().inflate(resource, parent, false);
            holder.name = (TextView) row.findViewById(R.id.tvValue);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        String current = deer[position];
        holder.name.setText(current);
        return row;
    }

    public class ViewHolder {
        TextView name;
    }

}
