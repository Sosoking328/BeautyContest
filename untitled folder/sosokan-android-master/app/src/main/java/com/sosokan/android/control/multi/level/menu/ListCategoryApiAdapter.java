package com.sosokan.android.control.multi.level.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sosokan.android.R;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.Image;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnhZin on 11/16/2016.
 */

public class ListCategoryApiAdapter extends MultiLevelListAdapter {
    Context context;
    boolean isChineseApp;
    public String categoryIdSelected;

    private class ViewHolder {
        TextView nameView;
        TextView infoView;
        ImageView iconCategory;
        LevelBeamView levelBeamView;
        ImageView rbCategory;
        RelativeLayout rlCategory;
    }

    public ListCategoryApiAdapter(Context context) {
        this.context = context;
        String languageToLoad = LocaleHelper.getLanguage(context);
        isChineseApp = languageToLoad.toString().equals("zh");
    }

    public ListCategoryApiAdapter(Context context, String categoryIdSelected) {
        this.context = context;
        String languageToLoad = LocaleHelper.getLanguage(context);
        isChineseApp = languageToLoad.toString().equals("zh");
        this.categoryIdSelected = categoryIdSelected;
    }

    @Override
    public List<?> getSubObjects(Object object) {
        CategoryNew category = (CategoryNew) object;
      //  Log.e("getSubObjects", category.getName());
        if (category == null) return new ArrayList<>();
        return DataProviderCategoryApi.getSubCategoryItems(category.getLegacy_id());
    }

    @Override
    public boolean isExpandable(Object object) {
        CategoryNew category = (CategoryNew) object;
        return DataProviderCategoryApi.isExpandableCategory(category);
    }

    @Override
    public View getViewForObject(Object object, View convertView, final ItemInfo itemInfo, String categoryIdSelected) {
        ListCategoryApiAdapter.ViewHolder viewHolder;

        viewHolder = new ListCategoryApiAdapter.ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.data_item, null);
        viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
        viewHolder.iconCategory = (ImageView) convertView.findViewById(R.id.iconCategory);
        viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
        viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
        viewHolder.rbCategory = (ImageView) convertView.findViewById(R.id.rbCategory);
        viewHolder.rlCategory = (RelativeLayout) convertView.findViewById(R.id.rlCategory);
        convertView.setTag(viewHolder);
        /*convertView.measure(View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));*/
        //  Log.e("","getViewForObject categoryIdSelected " + categoryIdSelected);
        CategoryNew category = ((CategoryNew) object);
        if (category != null) {

            if (category != null && !isChineseApp) {
                viewHolder.nameView.setText(category.getName());
            } else {
                if (category.getNameChinese() != null && !category.getNameChinese().isEmpty()) {
                    viewHolder.nameView.setText(category.getNameChinese());
                } else {
                    viewHolder.nameView.setText(category.getName());
                }

            }
            if (category != null && category.getChildren() != null && category.getChildren().length > 0) {
                int id = context.getResources().getIdentifier("ic_arrow_drop_down_black_24dp", "drawable", context.getPackageName());
                viewHolder.rbCategory.setImageResource(id);
            } else {
                //int id = context.getResources().getIdentifier("ic_arrow_back_black_30dp", "drawable", context.getPackageName());
                viewHolder.rbCategory.setVisibility(View.GONE);
            }
            if(isChineseApp)
            {
                loadImageForItemview(viewHolder,category.getIconChinese());
            }else{
                loadImageForItemview(viewHolder,category.getIconEnglish());
            }
/*
            if (category != null && category.getLegacy_id() != null) {
                if (this.categoryIdSelected != null) {
                    if (category.getLegacy_id().toString().equals(categoryIdSelected)) {
                        // Log.e("List category",categoryIdSelected);
                        int id = context.getResources().getIdentifier("ic_checked", "drawable", context.getPackageName());


                        viewHolder.rbCategory.setImageResource(id);
                    } else {
                        int id = context.getResources().getIdentifier("ic_check", "drawable", context.getPackageName());
                        viewHolder.rbCategory.setImageResource(id);
                    }
                } else {
                    if (category.getLegacy_id().toString().equals(categoryIdSelected)) {
                        // Log.e("List category",categoryIdSelected);
                        int id = context.getResources().getIdentifier("ic_checked", "drawable", context.getPackageName());
                        viewHolder.rbCategory.setImageResource(id);
                    } else {
                        int id = context.getResources().getIdentifier("ic_check", "drawable", context.getPackageName());
                        viewHolder.rbCategory.setImageResource(id);
                    }
                }

            } else {
                int id = context.getResources().getIdentifier("ic_check", "drawable", context.getPackageName());
                viewHolder.rbCategory.setImageResource(id);
            }
*/
            int countAds = 0;

            viewHolder.infoView.setText(String.format(context.getString(R.string.class_field), countAds));

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

        }
        return convertView;
    }

    private void loadImageForItemviewWithLanguageEnglish(final ViewHolder viewHolder, Image icon) {
        try {
            Glide.with(viewHolder.iconCategory.getContext()).load(icon.getImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewHolder.iconCategory.setBackground(drawable);
                    }
                }
            });
        } catch (Exception ex) {

        }
    }

    private void loadImageForItemview(final ViewHolder viewHolder, String icon) {
        try {
            Glide.with(viewHolder.iconCategory.getContext()).load(icon).asBitmap().into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewHolder.iconCategory.setBackground(drawable);
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("...", "viewHolder.iconCategory.setBackground(drawable); " + ex.toString());
        }
    }


}

