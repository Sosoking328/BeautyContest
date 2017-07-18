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
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Image;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnhZin on 11/16/2016.
 */

public class ListCategoryFireBaseAdapter extends MultiLevelListAdapterFirebase {
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

    public ListCategoryFireBaseAdapter(Context context) {
        this.context = context;
        String languageToLoad = LocaleHelper.getLanguage(context);
        isChineseApp = languageToLoad.toString().equals("zh");
    }

    public ListCategoryFireBaseAdapter(Context context, String categoryIdSelected) {
        this.context = context;
        String languageToLoad = LocaleHelper.getLanguage(context);
        isChineseApp = languageToLoad.toString().equals("zh");
        this.categoryIdSelected = categoryIdSelected;
    }

    @Override
    public List<?> getSubObjects(Object object) {
        Category category = (Category) object;
        //    Log.e("getSubObjects",category.getName());
        if (category == null) return new ArrayList<>();
        return DataProviderCategory.getSubCategoryItems(category.getId());
    }

    @Override
    public boolean isExpandable(Object object) {
        Category category = (Category) object;
        return DataProviderCategory.isExpandableCategory(category);
    }

    @Override
    public View getViewForObject(Object object, View convertView, final ItemInfo itemInfo, String categoryIdSelected) {
        ListCategoryFireBaseAdapter.ViewHolder viewHolder;

        viewHolder = new ListCategoryFireBaseAdapter.ViewHolder();
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
        Category category = ((Category) object);
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

            if (category != null && category.getId() != null) {
                if (this.categoryIdSelected != null) {
                    if (category.getId().toString().equals(categoryIdSelected)) {
                        // Log.e("List category",categoryIdSelected);
                        int id = context.getResources().getIdentifier("ic_checked", "drawable", context.getPackageName());


                        viewHolder.rbCategory.setImageResource(id);
                    } else {
                        int id = context.getResources().getIdentifier("ic_check", "drawable", context.getPackageName());
                        viewHolder.rbCategory.setImageResource(id);
                    }
                } else {
                    if (category.getId().toString().equals(categoryIdSelected)) {
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

            int  countAds = 0;
            if(category.getAdvertisesChinese()!= null && isChineseApp)
            {
                countAds += category.getAdvertisesChinese().size();

            }
            if(category.getAdvertisesEnglish()!= null && !isChineseApp)
            {
                countAds += category.getAdvertisesEnglish().size();
            }
            viewHolder.infoView.setText(String.format(context.getString(R.string.class_field),countAds));
            if (category != null && category.getIcons() != null && context != null) {
                if (isChineseApp && category.getIcons().getIconChinese() != null) {
                    Image icon = category.getIcons().getIconChinese();
                    if (icon.getImageUrl() != null && !icon.getImageUrl().isEmpty()) {
                 /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)viewHolder.iconCategory.getContext()).isDestroyed()) {

                    }*/
                        loadImageForItemview(viewHolder, icon);
                    }


                } else {
                    Image icon = category.getIcons().getIconEnglish();
                    if (icon.getImageUrl() != null && !icon.getImageUrl().isEmpty() && context != null) {
                        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)context).isDestroyed()) {
                        loadImageForItemviewWithLanguageEnglish(viewHolder, icon);
                        //}
                    }
                }
                //  Log.e("ListCate",category.getName());
            } else {
                if (context != null) {
                    int idAll = context.getResources().getIdentifier("ic_bg_white", "drawable", context.getPackageName());
                    viewHolder.iconCategory.setImageResource(idAll);
                }

            }


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

    private void loadImageForItemview(final ViewHolder viewHolder, Image icon) {
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
            Log.e("...", "viewHolder.iconCategory.setBackground(drawable); " + ex.toString());
        }
    }


}

