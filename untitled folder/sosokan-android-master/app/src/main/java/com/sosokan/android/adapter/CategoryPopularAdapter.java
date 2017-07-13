package com.sosokan.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sosokan.android.R;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.CategoryPopularCustomize;
import com.sosokan.android.ui.view.SquareImageView;
import com.sosokan.android.utils.LocaleHelper;

import java.util.List;

public class CategoryPopularAdapter extends RecyclerView.Adapter<CategoryPopularAdapter.MyViewHolder> {

    private Context mContext;
    public List<CategoryPopularCustomize> categoryList;
    private static RadioButton lastChecked = null;
    private static int lastCheckedPos = 0;
    private boolean isChineseApp = false;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleCategoryPopular;
        public SquareImageView iconCategoryPopular;
        public RadioButton rbCategoryPopular;

        public MyViewHolder(View view) {
            super(view);
            titleCategoryPopular = (TextView) view.findViewById(R.id.titleCategoryPopular);
            iconCategoryPopular = (SquareImageView) view.findViewById(R.id.iconCategoryPopular);
            rbCategoryPopular = (RadioButton) view.findViewById(R.id.rbCategoryPopular);
        }
    }


    public CategoryPopularAdapter(Context mContext, List<CategoryPopularCustomize> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        String languageToLoad = LocaleHelper.getLanguage(mContext);
        isChineseApp = languageToLoad.toString().equals("zh");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_category_popular, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryPopularCustomize categoryPopularCustomize = categoryList.get(position);
        if(categoryPopularCustomize!=null)
        {

            CategoryNew category = categoryPopularCustomize.getCategory();
            if (category != null) {
                holder.titleCategoryPopular.setText(category.getName());
                holder.rbCategoryPopular.setChecked(categoryPopularCustomize.isSelected());
                String urlIcon = isChineseApp ? category.getIconChinese() : category.getIconEnglish();
                if(urlIcon!= null && !urlIcon.isEmpty())
                {
                    Glide.with(holder.iconCategoryPopular.getContext()).load(urlIcon).asBitmap().into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                holder.iconCategoryPopular.setBackground(drawable);
                            }
                        }
                    });
                }

                holder.rbCategoryPopular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isSelected = categoryPopularCustomize.isSelected();
                        RadioButton cb = (RadioButton) v;
                        cb.setChecked(!isSelected);
                        categoryPopularCustomize.setSelected(!isSelected);

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    private int selectedItem;

    public void setSelectedItem(int position) {
        selectedItem = position;
    }

}
