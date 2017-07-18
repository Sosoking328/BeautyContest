package com.sosokan.android.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sosokan.android.R;
import com.sosokan.android.models.MenuItem;

public class MenuAdapter extends ArrayAdapter<MenuItem> {

    private Context mContext;

    public MenuAdapter(Context paramContext) {
        super(paramContext, 0);
        this.mContext = paramContext;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;
        if (row == null) {
            viewHolder = new ViewHolder();
            row = ((Activity) mContext).getLayoutInflater().inflate(R.layout.menu_item, parent, false);
            viewHolder.mTextViewTitle = (TextView) row.findViewById(R.id.tvTitleMenu);
            viewHolder.mTextViewMessage = (TextView) row.findViewById(R.id.mTextViewMessage);
            viewHolder.mImageViewIcon = (ImageView) row.findViewById(R.id.ivIconMenu);
            row.setTag(viewHolder);
        } else viewHolder = (ViewHolder) row.getTag();
        MenuItem item = getItem(position);
        viewHolder.mImageViewIcon.setImageResource(item.getIcon());
        viewHolder.mTextViewTitle.setText(item.getTitle());
        if (item != null && item.getId() == MenuItem.MESSAGE) {
//            int inbox = RVSSettingDB.getInstance(mContext).getInboxNumber();
            int inbox = 3;  //gán cứng giá trị
            if (inbox > 0) {
                viewHolder.mTextViewMessage.setVisibility(View.VISIBLE);
                viewHolder.mTextViewMessage.setText(String.valueOf(inbox));
            } else {
                viewHolder.mTextViewMessage.setVisibility(View.GONE);
                viewHolder.mTextViewMessage.setText(String.valueOf(inbox));
            }
        } else {
            viewHolder.mTextViewMessage.setVisibility(View.GONE);
        }
        return row;
    }

    public int getPositionById(int id) {
        for (int i = 0; i < getCount(); i++) {
            MenuItem menu = getItem(i);
            if (menu.getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public static class ViewHolder {
        private ImageView mImageViewIcon;
        private TextView mTextViewTitle, mTextViewMessage;
    }
}
