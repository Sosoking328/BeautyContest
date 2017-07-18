package com.sosokan.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sosokan.android.R;
import com.sosokan.android.control.multi.level.menu.DataProviderCategoryApi;
import com.sosokan.android.control.multi.level.menu.ItemInfo;
import com.sosokan.android.control.multi.level.menu.LevelBeamView;
import com.sosokan.android.control.multi.level.menu.MultiLevelListAdapter;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macintosh on 2/21/17.
 */

public class ListCategoryApiAdapter extends MultiLevelListAdapter {
    Context context;
    boolean isChineseApp;

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
        if (context != null) {

            String languageToLoad = LocaleHelper.getLanguage(context);
            isChineseApp = languageToLoad.toString().equals("zh");
        }
    }

    @Override
    public List<?> getSubObjects(Object object) {
        CategoryNew category = (CategoryNew) object;
        if (category != null)
            return DataProviderCategoryApi.getSubCategoryItems(category.getLegacy_id());
        return new ArrayList<>();
    }

    @Override
    public boolean isExpandable(Object object) {
        CategoryNew category = (CategoryNew) object;
        return DataProviderCategoryApi.isExpandableCategory(category);
    }

    @Override
    public View getViewForObject(Object object, View convertView, final ItemInfo itemInfo, String categorySelected) {
        final ViewHolder viewHolder;
        final CategoryNew category = ((CategoryNew) object);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.data_item, null);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
            viewHolder.iconCategory = (ImageView) convertView.findViewById(R.id.iconCategory);
            viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
            viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
            viewHolder.rbCategory = (ImageView) convertView.findViewById(R.id.rbCategory);
            viewHolder.rlCategory = (RelativeLayout) convertView.findViewById(R.id.rlCategory);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (category != null && !isChineseApp) {
            viewHolder.nameView.setText(category.getName());
        } else {
            if (category != null) {
                if (category.getNameChinese() != null && !category.getNameChinese().isEmpty()) {
                    viewHolder.nameView.setText(category.getNameChinese());
                } else {
                    viewHolder.nameView.setText(category.getName());
                }
            }

        }
        if(category!= null && category.getLegacy_id()!=null)
        {
            if(category.getLegacy_id().toString().equals(categorySelected))
            {
                if (category != null && category.getChildren() != null && category.getChildren().length > 0) {
                    int id = context.getResources().getIdentifier("ic_arrow_drop_down_orange_24dp", "drawable", context.getPackageName());
                    viewHolder.rbCategory.setImageResource(id);
                } else {
                    viewHolder.rbCategory.setVisibility(View.GONE);
                }
            }else{

                if (category != null && category.getChildren() != null && category.getChildren().length > 0) {
                    int id = context.getResources().getIdentifier("ic_arrow_drop_down_black_24dp", "drawable", context.getPackageName());
                    viewHolder.rbCategory.setImageResource(id);
                } else {
                    viewHolder.rbCategory.setVisibility(View.GONE);
                }
            }
        }else{
            viewHolder.rbCategory.setVisibility(View.GONE);
        }
        /*if (itemInfo != null) {
            if (itemInfo.isSelected()) {
                if (category != null && category.getChildren() != null && category.getChildren().length > 0) {
                    int id = context.getResources().getIdentifier("ic_arrow_drop_down_black_24dp", "drawable", context.getPackageName());
                    viewHolder.rbCategory.setImageResource(id);
                } else {
                    //int id = context.getResources().getIdentifier("ic_arrow_back_black_30dp", "drawable", context.getPackageName());
                    viewHolder.rbCategory.setVisibility(View.GONE);
                }
            }else{
                if (category != null && category.getChildren() != null && category.getChildren().length > 0) {
                    int id = context.getResources().getIdentifier("ic_arrow_drop_down_orange_24dp", "drawable", context.getPackageName());
                    viewHolder.rbCategory.setImageResource(id);
                } else {
                    //int id = context.getResources().getIdentifier("ic_arrow_back_black_30dp", "drawable", context.getPackageName());
                    viewHolder.rbCategory.setVisibility(View.GONE);
                }
            }
        }*/


        int countAds = 0;
        if (category != null)
            countAds += isChineseApp ? category.getCount_zh_hans() : category.getCount_en();
        viewHolder.infoView.setText(String.format(context.getString(R.string.class_field), countAds));
        if (category != null && context != null) {

            String iconUrl;
            if (isChineseApp) {
                iconUrl = category.getIconChinese();

            } else {
                iconUrl = category.getIconEnglish();

            }
            if (iconUrl != null && !iconUrl.isEmpty()) {
                Glide.with(viewHolder.iconCategory.getContext()).load(iconUrl).thumbnail(0.2f).skipMemoryCache(true).into(viewHolder.iconCategory);

            }
            //  Log.e("ListCate",category.getName());
        } else {
            if (context != null) {
                int idAll = context.getResources().getIdentifier("ic_bg_white", "drawable", context.getPackageName());
                viewHolder.iconCategory.setImageResource(idAll);
            }

        }


        viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

        return convertView;
    }
}
