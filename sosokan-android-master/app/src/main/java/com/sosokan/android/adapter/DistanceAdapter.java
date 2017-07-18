package com.sosokan.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sosokan.android.R;
import com.sosokan.android.utils.DateUtils;

import java.util.List;

/**
 * Created by macintosh on 2/23/17.
 */

public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.MyViewHolder> {

    private Context mContext;
    String[] mObjects;
    Listener listener;
    int widthBase;

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    public interface Listener {
        void onItemClick(int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvValue;

        public MyViewHolder(View view) {
            super(view);

            tvValue = (TextView) view.findViewById(R.id.tvValue);

        }


    }

    @Override
    public DistanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_spinner_custom, parent, false);
        //    Log.e("onCreateViewHolder", Integer.toString(mObjects.length));
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        widthBase = windowManager.getDefaultDisplay().getWidth();
        DistanceAdapter.MyViewHolder holder = new DistanceAdapter.MyViewHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(DistanceAdapter.MyViewHolder holder, final int position) {

        if (mObjects != null && mObjects.length > 0 && position < mObjects.length) {
            final String name = mObjects[position];
            holder.tvValue.setText(name);

            holder.tvValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener == null) {

                    } else {
                        listener.onItemClick(position);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mObjects.length;
    }

    public DistanceAdapter(Context context, String[] objects) {


        this.mContext = context;
        this.mObjects = objects;
    }
}