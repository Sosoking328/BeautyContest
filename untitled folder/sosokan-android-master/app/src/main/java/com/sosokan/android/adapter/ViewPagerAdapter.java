package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sosokan.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AnhZin on 12/19/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    // private int[] mResources;
    private ArrayList<String> images;

    public ViewPagerAdapter(Context mContext, ArrayList<String> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);


      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
            try {
                Glide.with(mContext)
                        .load(images.get(position))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int imageWidth = bitmap.getWidth();
                                int imageHeight = bitmap.getHeight();
                                WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                                int newWidth = windowManager.getDefaultDisplay().getWidth(); //this method should return the width of device screen.
                                float scaleFactor = (float) newWidth / (float) imageWidth;
                                int newHeight = (int) (imageHeight * scaleFactor);

                                bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

                                ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
                                imageView.setImageBitmap(bitmap);
                                container.addView(itemView);

                            }
                        });
            } catch (Exception ex) {
                Log.e("ViewPagerAdapter ex", String.valueOf(ex));
            }

//        }


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}