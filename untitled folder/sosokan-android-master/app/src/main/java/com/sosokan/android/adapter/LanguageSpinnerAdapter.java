package com.sosokan.android.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sosokan.android.R;
import com.sosokan.android.utils.LocaleHelper;

import java.util.Locale;

/**
 * Created by AnhZin on 9/18/2016.
 */
public class LanguageSpinnerAdapter extends BaseAdapter {

    private Context  context;
    int flags[];
    String[] countryNames;
    private int resource;
    LayoutInflater inflater;

    public LanguageSpinnerAdapter(Context applicationContext,  int resource, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        inflater = (LayoutInflater.from(applicationContext));
        this.resource = resource;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(resource, null);
        if(convertView!=null)
        {
            TextView textView = (TextView) convertView.findViewById(R.id.txtViewCountryName);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imgViewFlag);
            textView.setText(countryNames[position]);
            imageView.setImageResource(flags[position]);
          /*  LinearLayout llSpinnerLanguage = (LinearLayout)convertView.findViewById(R.id.llSpinnerLanguage);
            llSpinnerLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLanguageByPosition(position);
                }
            });*/
        }
        return convertView;
    }

    public class ViewHolder {
        TextView name;
        ImageButton image;
    }
    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(resource, null);
         if(convertView!=null)
         {
             TextView textView = (TextView) convertView.findViewById(R.id.txtViewCountryName);
             ImageView imageView = (ImageView) convertView.findViewById(R.id.imgViewFlag);
             textView.setText(countryNames[position]);
             imageView.setImageResource(flags[position]);
           /*  LinearLayout llSpinnerLanguage = (LinearLayout)convertView.findViewById(R.id.llSpinnerLanguage);
             llSpinnerLanguage.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     setLanguageByPosition(position);
                 }
             });*/
         }
        return convertView;
    }

    public void setLanguageByPosition(int position)
    {

        switch (position)
        {
            case 0:
                if(context!= null)
                {
                    setLocale(context,"en");
                    LocaleHelper.onCreate(context,"en");
                }

                break;
            case 1:
                if(context!= null)
                {

                    setLocale(context,"zh");
                    LocaleHelper.onCreate(context,"zh");
                }
                break;
        }
    }

    public void setLocale(Context context, String language){
        if(context!=null)
        {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getApplicationContext().getResources().updateConfiguration(config, null);
        }
    }
}
