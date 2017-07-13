package com.sosokan.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.sosokan.android.R;
import com.sosokan.android.models.City;

import java.util.List;

/**
 * Created by macintosh on 2/5/17.
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> {

    private Context mContext;
    private List<City> cities;
    private int resource;
    private Listener listener;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    int selected_position = -1;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView rbFlag;
        RelativeLayout rlItemFlag;
        public MyViewHolder(View view) {
            super(view);
            rlItemFlag = (RelativeLayout) view.findViewById(R.id.rlItemFlag);
            name = (TextView) view.findViewById(R.id.nameFlagChoice);
            rbFlag = (ImageView) view.findViewById(R.id.rbFlag);
            // Make this view clickable
            name.setClickable(true);
        }
    }


    public CitiesAdapter(Context mContext, List<City> cities) {
        this.mContext = mContext;
        this.cities = cities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flag, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        City city = cities.get(position);
        holder.name.setText(city.getCity());
        if (selected_position == position) {
            int id = mContext.getResources().getIdentifier("ic_checked", "drawable", mContext.getPackageName());
            holder.rbFlag.setImageResource(id);

        } else {
            int id = mContext.getResources().getIdentifier("ic_check", "drawable", mContext.getPackageName());
            holder.rbFlag.setImageResource(id);
        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {

                } else {
                    listener.onItemClick(position);
                    if (selected_position >= 0) {

                        notifyItemChanged(selected_position);
                    }
                    selected_position = position;
                    notifyItemChanged(selected_position);
                }
            }
        });

        holder.rlItemFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null) {

                } else {
                    listener.onItemClick(position);
                    if (selected_position >= 0) {

                        notifyItemChanged(selected_position);
                    }
                    selected_position = position;
                    notifyItemChanged(selected_position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    private int selectedItem;

    public void setSelectedItem(int position) {
        selectedItem = position;
    }

}
