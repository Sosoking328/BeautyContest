package com.sosokan.android.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.google.firebase.database.Query;
import com.sosokan.android.R;
import com.sosokan.android.models.Category;

import java.util.ArrayList;

/**
 * Created by AnhZin on 8/28/2016.
 */
public class CategoryFireBaseAdapter extends FirebaseRecyclerAdapter<CategoryFireBaseAdapter.ViewHolder, Category> {
    private Context mContext;
    private ArrayList<Category> categoryList;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    ArrayList<String> listCategoryId;
    private int resource;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, classField;
        public ImageView iconCategory;
        public RadioButton rbCategory;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleCategory);
            classField = (TextView) view.findViewById(R.id.classFieldCategory);
            iconCategory = (ImageView) view.findViewById(R.id.iconCategory);
            rbCategory = (RadioButton) view.findViewById(R.id.rbCategory);
        }
    }

    public CategoryFireBaseAdapter(Query query, Class<Category> itemClass, @Nullable ArrayList<Category> items,
                                   @Nullable ArrayList<String> keys) {
        super(query, itemClass, items, keys);
    }
    public CategoryFireBaseAdapter(Context mContext, Query query, Class<Category> itemClass, @Nullable ArrayList<Category> items,
                                    @Nullable ArrayList<String> keys, @Nullable ArrayList<String> listAdvertiseId, int resource) {
        super(query, itemClass, items, keys);
        categoryList = items;
        this.mContext = mContext;
        this.resource = resource;
        this.listCategoryId = listAdvertiseId;
    }

    @Override
    public CategoryFireBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryFireBaseAdapter.ViewHolder holder, final int position) {
        if(categoryList.size()>0 && categoryList.size()>position)
        {
            Category category = categoryList.get(position);
            holder.title.setText(category.getName());
            holder.classField.setText(String.format(mContext.getString(R.string.class_field), category.getAdvertiseCount()));

          /*  int id = mContext.getResources().getIdentifier(category.getIcon().getName(), "drawable", mContext.getPackageName());
            holder.iconCategory.setImageResource(id);*/

            if (position == selectedItem) {
                holder.rbCategory.setChecked(true);
            }

            holder.rbCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton cb = (RadioButton) v;
                    int clickedPos = position; //((Integer)cb.getTag()).intValue();

                    if (cb.isChecked()) {
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                            //  categoryList.get(lastCheckedPos).setSelected(false);
                        }

                        lastChecked = cb;
                        lastCheckedPos = clickedPos;
                    } else
                        lastChecked = null;

                    // categoryList.get(clickedPos).setSelected(cb.isChecked());
                }
            });
        }
    }

    @Override
    protected void itemAdded(Category item, String key, int position) {
       // Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override
    protected void itemChanged(Category oldItem, Category newItem, String key, int position) {
       // Log.d("MyAdapter", "Changed an item.");
    }

    @Override
    protected void itemRemoved(Category item, String key, int position) {
      //  Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override
    protected void itemMoved(Category item, String key, int oldPosition, int newPosition) {
      //  Log.d("MyAdapter", "Moved an item.");
    }

    private int selectedItem;

    public void setSelectedItem(int position) {
        selectedItem = position;
    }
    public interface Listener {
        void onItemClick(int position);
    }

}
