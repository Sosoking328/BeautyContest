package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sosokan.android.R;
import com.sosokan.android.control.RoundedImageView;
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.AdvertiseApi;

import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.models.Favorite;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.ui.activity.AdvertiseDetailActivity;
import com.sosokan.android.ui.activity.AdvertiseDetailApiActivity;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.view.SquareImageView;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Config;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;
import com.sosokan.android.utils.DetectHtml;
import com.sosokan.android.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sosokan.android.utils.ColorHelper.getColorWithAlpha;

/**
 * Created by AnhZin on 9/22/2016.
 */
public class AdvertiseNewAdapter extends RecyclerView.Adapter<AdvertiseNewAdapter.MyViewHolder> implements Filterable {
    private DynamicHeight dynamicHeight;
    private Context mContext;
    private List<AdvertiseApi> mObjects;
    private List<AdvertiseApi> originalData;
    private List<AdvertiseApi> advertiseList;
    private Listener listener;
    private List<CategoryNew> categories;
    //  User user;
    UserProfileApi user;
    List<Favorite> favorites;
    Map<String, Object> mapFavorite;
    boolean isChineseApp;
    boolean showType;
    int widthBase;
    CategoryNew categoryAll;
    List<String> categoriesId;
    private DatabaseReference mDataRef;
    private AdvertiseNewAdapter.ItemFilter mFilter = new AdvertiseNewAdapter.ItemFilter();


    private AdvertiseNewAdapter.OnChangeFavorite changeFavorite;


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public TextView createdDate;
        public TextView sale;
        public RoundedImageView thumbnail;
        public VideoView videoView;
        public ImageButton ibFavoritePostInList;
        public ImageButton ibSharePostInList;
        public TextView tvNumberFavoriteInList;
        public TextView tvDisplayCreatedType;
        public TextView tvViewPostInList;
        public ImageButton ibViewPostInList;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleCardHomeView);
            description = (TextView) view.findViewById(R.id.description);
            thumbnail = (RoundedImageView) view.findViewById(R.id.thumbnail);
            createdDate = (TextView) view.findViewById(R.id.tvCreatedDate);
            sale = (TextView) view.findViewById(R.id.sale);
            videoView = (VideoView) view.findViewById(R.id.videoPreviewListAdvertise);
            ibFavoritePostInList = (ImageButton) view.findViewById(R.id.ibFavoritePostInList);
            ibSharePostInList = (ImageButton) view.findViewById(R.id.ibSharePostInList);
            tvNumberFavoriteInList = (TextView) view.findViewById(R.id.tvNumberFavoriteInList);
            tvDisplayCreatedType = (TextView) view.findViewById(R.id.tvDisplayCreatedType);
            tvViewPostInList = (TextView) view.findViewById(R.id.tvViewPostInList);
            ibViewPostInList = (ImageButton) view.findViewById(R.id.ibViewPostInList);
        }


    }

    public AdvertiseNewAdapter(Context mContext, List<AdvertiseApi> advertiseList, List<CategoryNew> categories, UserProfileApi user, List<Favorite> favorites, boolean showType) {
        this.mContext = mContext;
        this.advertiseList = advertiseList;
        this.categories = categories;
        this.user = user;
        this.favorites = favorites;
        //TODO GET FAVORITE
       /* if (user != null) {
            mapFavorite = user.getFavorites();
        }*/
        this.originalData = advertiseList;
        this.mObjects = advertiseList;
        if (mContext != null) {
            String languageToLoad = LocaleHelper.getLanguage(mContext);
            isChineseApp = languageToLoad.toString().equals("zh");

        }
        this.showType = showType;
        categoriesId = new ArrayList<>();
        if (categories != null) {
            for (CategoryNew category : categories) {
                categoriesId.add(category.getLegacy_id());
                if (category.getLegacy_id().equals(Constant.sosokanCategoryAll)) {
                    categoryAll = category;
                }
            }

        }
        mDataRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(Config.FIREBASE_URL);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.card_view_home, parent, false);
        //    Log.e("onCreateViewHolder", Integer.toString(mObjects.size()));
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        widthBase = windowManager.getDefaultDisplay().getWidth();
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        ApplicationUtils.closeMessage();
        if (mObjects != null && mObjects.size() > 0 && position < mObjects.size()) {
            final AdvertiseApi advertise = mObjects.get(position);
            // Log.e("onBindViewHolder", Integer.toString(mObjects.size()));
            if (advertise != null) {
                if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                    String languageToLoad = LocaleHelper.getLanguage(mContext);
                    String[] separated = advertise.getName().toString().split("-");
                    String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                    String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                    if (!languageToLoad.toString().equals("en")) {
                        holder.title.setText(stChinese);
                        //  Log.e(" stChinese",stChinese);
                    } else {
                        holder.title.setText(stEnglish);
                        //Log.e("stEnglish ", stEnglish);
                    }
                } else if (advertise.getName() != null) {
                    holder.title.setText(advertise.getName());
                }

                holder.tvViewPostInList.setText(String.valueOf(advertise.getViews()));
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callDetailPage(advertise.getId(), advertise.getLegacy_id(), advertise.getCategoryId());
                    }
                });

                if (advertise.getDescription() != null && !advertise.getDescription().isEmpty()
                        && DetectHtml.isHtml(advertise.getDescription())) {
                    try {
                        holder.description.setText(Html.fromHtml(Html.fromHtml(advertise.getDescription()).toString()));
                    } catch (Exception ex) {
                        Log.e("html decode", ex.toString());
                        holder.description.setText("");
                    }
                } else {
                    holder.description.setText(advertise.getDescription());
                }

                holder.createdDate.setText(DateUtils.getTimeAgo(advertise.getCreated_on(), mContext));
                holder.createdDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("createdDate", "advertise.getCreated_on() " + advertise.getCreated_on());
                        //  DateUtils.getDayForAdvertise(advertise.getCreated_on());
                        if (holder.tvDisplayCreatedType.getText().equals("0")) {
                            holder.createdDate.setText(DateUtils.getDayForAdvertise(advertise.getCreated_on()));
                            holder.tvDisplayCreatedType.setText("1");
                        } else {
                            holder.createdDate.setText(DateUtils.getTimeAgo(advertise.getCreated_on(), mContext));
                            holder.tvDisplayCreatedType.setText("0");
                        }

                    }
                });
               /* int sale = advertise.getSaleOff();
                if (sale > 0) {
                    holder.sale.setVisibility(View.VISIBLE);
                    holder.sale.setText(advertise.getSaleOff() + "% " + mContext.getString(R.string.off_percent));
                    holder.sale.setBackgroundColor(getColorWithAlpha(Color.DKGRAY, 0.6f));
                }*/

                if (advertise != null && advertise.getImageHeader() != null && !advertise.getImageHeader().getUrl().isEmpty()) {
                    if (mContext != null) {
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {

                        } else {
                            Log.e("isDestroyed", "isDestroyed");
                        }*/

                        Glide
                                .with(mContext)
                                .load(advertise.getImageHeader().getUrl()).asBitmap().skipMemoryCache(true)
                                /// .override(width, height)
                                .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                                .into(holder.thumbnail);
                    }

                } else {

                    if (mContext != null) {
                        if (categoriesId.contains(advertise.getCategoryId())) {
                            for (CategoryNew category : categories) {
                                if (category.getLegacy_id().equals(advertise.getCategoryId())) {
                                    String iconUrl;
                                    if (isChineseApp) {
                                        iconUrl = category.getIconChinese();

                                    } else {
                                        iconUrl = category.getIconEnglish();
                                    }
                                    if (iconUrl != null && !iconUrl.isEmpty()) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
                                        } else {
                                            Log.e("isDestroyed", "isDestroyed");
                                        }
                                        Glide.with(mContext).load(iconUrl).skipMemoryCache(true).dontAnimate().into(holder.thumbnail);

                                    }
                                    break;
                                }

                            }
                        } else {
                            Image icon;
                            if (categoryAll != null) {
                                String iconUrl;
                                if (isChineseApp) {
                                    iconUrl = categoryAll.getIconChinese();

                                } else {
                                    iconUrl = categoryAll.getIconEnglish();
                                }
                                if (iconUrl != null && !iconUrl.isEmpty()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
                                    } else {
                                        Log.e("isDestroyed", "isDestroyed");
                                    }
                                    Glide.with(mContext).load(iconUrl).skipMemoryCache(true).dontAnimate().into(holder.thumbnail);

                                }
                            }


                        }

                    }
                }

                boolean isFavorite = false;
                if (user != null) {
                    //TODO GET FAVORITE
                    //    mapFavorite = user.getFavorites();
                    if (mapFavorite != null) {
                        Object obj = mapFavorite.get(advertise.getId());
                        if (obj != null) {
                            isFavorite = true;
                        }
                    }/*else{
                        isFavorite = true;
                    }*/
                    final int sdk = Build.VERSION.SDK_INT;
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                        if (isFavorite) {
                            holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_share_white_30dp));
                        } else {
                            holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_white_30dp));
                        }

                    } else {
                        if (isFavorite) {
                            holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_share_white_30dp));
                        } else {
                            holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_white_30dp));
                        }

                    }
                }
                holder.tvNumberFavoriteInList.setText(Integer.toString(advertise.getFavoriteCount()));
                holder.ibFavoritePostInList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String alert;
                        String message;
                        if (user != null) {
                            if (user != null) {
                                boolean isFavorite = processFavorite(advertise);
                                final int sdk = Build.VERSION.SDK_INT;
                                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                                    if (isFavorite) {
                                        holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_share_white_30dp));
                                    } else {
                                        holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_white_30dp));
                                    }

                                } else {
                                    if (isFavorite) {
                                        holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_share_white_30dp));
                                    } else {
                                        holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_white_30dp));
                                    }

                                }
                                holder.tvNumberFavoriteInList.setText(Integer.toString(advertise.getFavoriteCount()));
                            } else {
                                alert = mContext.getResources().getString(R.string.we_are_sorry);
                                message = mContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                                showMessageError(alert, message);
                            }
                        } else {
                            alert = mContext.getResources().getString(R.string.we_are_sorry);
                            message = mContext.getResources().getString(R.string.you_need_to_login_your_sosokan);
                            showMessageError(alert, message);
                        }

                    }
                });
                holder.ibSharePostInList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
                        String nameAds = advertise.getName() == null ? "" : advertise.getName();
                        shareIntent.putExtra(Intent.EXTRA_TEXT, nameAds + "\n" + advertise.getDescription());

                        v.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));

                    }
                });
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
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

    }

    public void showMessageError(String title, String message) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext);
                if (dlgAlert != null)
                    dlgAlert.create().dismiss();
                dlgAlert.setMessage(message);
                dlgAlert.setTitle(title);
                dlgAlert.setPositiveButton(mContext.getResources().getString(R.string.ok), null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        } catch (Exception ex) {

        }
    }

    public void broadcastIntent(String advertiseId) {
        Intent intent = new Intent(MenuActivity.ACTION_UPDATE_FAVORITE);
        intent.putExtra(Constant.ADVERTISEID, advertiseId);
        mContext.sendBroadcast(intent);
    }

    public boolean processFavorite(final AdvertiseApi advertise) {
        boolean isFavorite = false;
        //TODO: PROCESS FAVORITE
        /*if (user != null && advertise!= null && user.getId() != advertise.getUserId()) {

            if (mapFavorite == null) {
                isFavorite = true;
                mapFavorite = new HashMap<>();
                mapFavorite.put(Integer.toString(advertise.getId()), isFavorite);
                int numOfFavorite = advertise.getFavoriteCount();
                advertise.setFavoriteCount(numOfFavorite + 1);
                mDataRef.child(Constant.ADVERTISES).child(Integer.toString(advertise.getId())).setValue(advertise);
            } else {
                Object isFavor = mapFavorite.get(advertise.getId());
                if (isFavor != null) {
                    isFavorite = (boolean) isFavor;
                    isFavorite = !isFavorite;
                    //  mapFavorite.put(advertise.getId(),isFavorite);
                    mapFavorite.remove(advertise.getId());
                    int numOfFavorite = advertise.getFavoriteCount();
                    if (!isFavorite) {
                        if (numOfFavorite > 0) {
                            advertise.setFavoriteCount(numOfFavorite - 1);
                        } else {
                            advertise.setFavoriteCount(0);
                        }
                    } else {
                        advertise.setFavoriteCount(numOfFavorite + 1);
                    }
                    mDataRef.child(Constant.ADVERTISES).child(Integer.toString(advertise.getId())).setValue(advertise);
                } else {
                    isFavorite = true;
                    mapFavorite.put(Integer.toString(advertise.getId()), isFavorite);
                    int numOfFavorite = advertise.getFavoriteCount();
                    advertise.setFavoriteCount(numOfFavorite + 1);
                    mDataRef.child(Constant.ADVERTISES).child(Integer.toString(advertise.getId())).setValue(advertise);
                }
            }


            // broadcastIntent();
            user.setFavorites(mapFavorite);
            final boolean finalIsFavorite = isFavorite;
            if (user != null) {
                mDataRef.child(Constant.USERS).child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {
                            if (!finalIsFavorite) {
                                broadcastIntent(Integer.toString(advertise.getId()));
                            }
                        }
                    }
                });
            }
        }*/

        return isFavorite;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            FilterResults results = new FilterResults();
            if (constraint != null) {
                String filterString = constraint.toString().toLowerCase();

                final List<AdvertiseApi> list = originalData;

                int count = list.size();
                final ArrayList<AdvertiseApi> nlist = new ArrayList<AdvertiseApi>(count);

                for (AdvertiseApi temp : originalData) {
                    if (temp.getName().toLowerCase().contains(filterString) || temp.getDescription().toLowerCase().contains(filterString)) { //returns true if name matchs with Persons name
                        nlist.add(temp);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (ArrayList<AdvertiseApi>) results.values;
            notifyDataSetChanged();
        }

    }

    public interface OnChangeFavorite {
        void changeFavorite();
    }

    @Override
    public int getItemCount() {
        //  Log.e("getItemCount", Integer.toString(mObjects.size()));
        return mObjects == null ? 0 : mObjects.size();
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, AdvertiseApi data) {
        mObjects.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(AdvertiseApi data) {
        int position = mObjects.indexOf(data);
        mObjects.remove(position);
        notifyItemRemoved(position);
    }

    public interface DynamicHeight {
        void HeightChange(int position, int height);
    }

    public void callDetailPage(int advertiseId, String legacyId, String categoryId) {
        if (categoryId != null && legacyId != null && !legacyId.isEmpty()) {
            Intent intent = new Intent(mContext, AdvertiseDetailApiActivity.class);
            //intent.putExtra(Constant.POSSITION, position);
            intent.putExtra(Constant.ADVERTISEID, advertiseId);
            intent.putExtra(Constant.CATEGORYID, categoryId);
            intent.putExtra(Constant.LEGACY_ID, legacyId);

            mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }
}
