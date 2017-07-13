package com.sosokan.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sosokan.android.R;
import com.sosokan.android.models.Country;


public class CountryAdapter extends ArrayAdapter<Country> {

    private LayoutInflater mLayoutInflater;
    private boolean isSpinner;

    public CountryAdapter(Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setIsSpinner(boolean isSpinner) {
        this.isSpinner = isSpinner;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_country_drop, parent, false);
            holder = new ViewHolder();
            holder.mNameView = (TextView) convertView.findViewById(R.id.country_name);
            holder.mCodeView = (TextView) convertView.findViewById(R.id.country_code);
            holder.imvFlag = (ImageView) convertView.findViewById(R.id.imvFlag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Country country = getItem(position);
        if (country != null) {
            holder.mNameView.setText(country.getName());
            holder.mCodeView.setText(country.getCountryCodeStr());
            holder.imvFlag.setBackgroundResource(country.getResId());
        }
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_country_drop, parent, false);
            holder = new ViewHolder();
            holder.mNameView = (TextView) convertView.findViewById(R.id.country_name);
            holder.mCodeView = (TextView) convertView.findViewById(R.id.country_code);
            holder.imvFlag = (ImageView) convertView.findViewById(R.id.imvFlag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Country country = getItem(position);
        if (country != null) {
            if (isSpinner) {
                holder.mNameView.setVisibility(View.GONE);
            } else {
                holder.mNameView.setText(country.getName());
            }
            holder.mCodeView.setText(country.getCountryCodeStr());
            holder.imvFlag.setBackgroundResource(country.getResId());
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView mNameView;
        public TextView mCodeView;
        public ImageView imvFlag;
    }
}
