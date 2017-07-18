package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sosokan.android.R;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

import static com.sosokan.android.utils.ColorHelper.getColorWithAlpha;

/**
 * Created by AnhZin on 8/28/2016.
 */
public class AdvertiseFirebaseAdapter extends FirebaseRecyclerAdapter<AdvertiseFirebaseAdapter.ViewHolder, Advertise> {
    private Context mContext;
    private List<Advertise> advertiseList;
    private StorageReference mStorage;
    ArrayList<String> listAdvertiseId;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView createdDate;
        public TextView sale;
        public ImageView thumbnail;
        public VideoView videoView;
        public ImageButton ibFavoritePostInList;
        public ImageButton ibSharePostInList;
        public TextView tvNumberFavoriteInList;

        //    public WebView webViewDescription;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            createdDate = (TextView) view.findViewById(R.id.tvCreatedDate);
            sale = (TextView) view.findViewById(R.id.sale);
            videoView = (VideoView) view.findViewById(R.id.videoPreviewListAdvertise);
            ibFavoritePostInList = (ImageButton) view.findViewById(R.id.ibFavoritePostInList);
            ibSharePostInList = (ImageButton) view.findViewById(R.id.ibSharePostInList);
            tvNumberFavoriteInList = (TextView) view.findViewById(R.id.tvNumberFavoriteInList);
            //  webViewDescription = (WebView) view.findViewById(R.id.webViewDescription);

        }
    }

    public AdvertiseFirebaseAdapter(Context mContext, Query query, Class<Advertise> itemClass, @Nullable ArrayList<Advertise> items,
                                    @Nullable ArrayList<String> keys, @Nullable ArrayList<String> listAdvertiseId) {
        super(query, itemClass, items, keys);
        advertiseList = items;
        mStorage = FirebaseStorage.getInstance().getReference();
        this.mContext = mContext;
        this.listAdvertiseId = listAdvertiseId;
    }

    @Override
    public AdvertiseFirebaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_home, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final AdvertiseFirebaseAdapter.ViewHolder holder, final int position) {
        if (advertiseList != null && advertiseList.size() > 0) {
            final Advertise advertise = advertiseList.get(position);
            if (advertise != null) {
                if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                    String[] separated = advertise.getName().toString().split("-");
                    String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                    String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                    if (mContext != null) {
                        String languageToLoad = LocaleHelper.getLanguage(mContext);

                        if (!languageToLoad.toString().equals("en")) {
                            holder.title.setText(stChinese);
                        } else {
                            holder.title.setText(stEnglish);
                        }
                    } else {
                        holder.title.setText(stEnglish);
                    }

                } else if (advertise.getName() != null) {
                    holder.title.setText(advertise.getName());
                }


                //holder.title.setText(advertise.getName());
                holder.description.setText(advertise.getDescription());
                if (Math.abs(advertise.createdAt) >= Math.abs(advertise.getUpdatedAt())) {
                    holder.createdDate.setText(DateUtils.toDuration(advertise.getCreatedAt(), mContext));
                } else {
                    holder.createdDate.setText(DateUtils.toDuration(advertise.getUpdatedAt(), mContext));
                }

                /*int sale = advertise.getSaleOff();
                if (sale > 0) {
                    holder.sale.setVisibility(View.VISIBLE);
                    holder.sale.setText(advertise.getSaleOff() + "% " + mContext.getString(R.string.off_percent));
                    holder.sale.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.6f));
                }*/
                if (advertise != null && advertise.getImageHeader() != null && !advertise.getImageHeader().getImageUrl().isEmpty()) {
                    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)mContext).isDestroyed()) {
                    try {
                        Glide.with(mContext).load(advertise.getImageHeader().getImageUrl()).into(holder.thumbnail);
                    } catch (Exception ex) {

                    }
//                    }
                } else {
                   /* if (advertise!= null && advertise.getCategory()!=null&& advertise.getCategory().getIcon()!=null && !advertise.getCategory().getIcon().getImageUrl().isEmpty()) {
                        Glide.with(mContext).load(advertise.getCategory().getIcon().getImageUrl()).into(holder.thumbnail);
                    }*/
                }

                if (listAdvertiseId != null && advertise != null && listAdvertiseId.contains(advertise.getId())) {
                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                    } else {
                        holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                    }
                }
                holder.tvNumberFavoriteInList.setText(Integer.toString(advertise.getFavoriteCount()));
                holder.ibFavoritePostInList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                holder.ibSharePostInList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
                        String nameAds = advertise.getName() == null ? "" : advertise.getName();
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, nameAds + "\n" + advertise.getDescription());

                        v.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));

                    }
                });
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(position);
                    }
                });

              /*  holder.webViewDescription.getSettings().setJavaScriptEnabled(true);
                holder.webViewDescription.loadDataWithBaseURL("", advertise.getHtmlDescription(), "text/html", "UTF-8", "");*/
            }

        }
    }

    @Override
    protected void itemAdded(Advertise item, String key, int position) {
        // System.out.println("itemAdded: " + item);
    }

    @Override
    protected void itemChanged(Advertise oldItem, Advertise newItem, String key, int position) {
       /* System.out.println("itemChanged: oldItem " + oldItem);
        System.out.println("itemChanged: newItem " + newItem);*/
    }

    @Override
    protected void itemRemoved(Advertise item, String key, int position) {

    }

    @Override
    protected void itemMoved(Advertise item, String key, int oldPosition, int newPosition) {

    }

    public interface Listener {
        void onItemClick(int position);
    }


}

