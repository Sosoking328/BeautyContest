package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
import com.sosokan.android.models.Advertise;
import com.sosokan.android.models.Category;
import com.sosokan.android.models.Favorite;
import com.sosokan.android.models.Image;
import com.sosokan.android.models.User;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.ui.activity.MenuActivity;
import com.sosokan.android.ui.activity.PrefManager;
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
public class AdvertiseFollowingAdapter extends RecyclerView.Adapter<AdvertiseFollowingAdapter.MyViewHolder> implements Filterable {
    private DynamicHeight dynamicHeight;
    private Context mContext;
    private List<Advertise> mObjects;
    private List<Advertise> originalData;
    private List<Advertise> advertiseList;
    private Listener listener;
    private List<Category> categories;
   // User user;
    UserProfileApi user;
    Favorite favorite;
    List<Favorite> favorites;
    // private DatabaseReference mDatabaseFavorite, mDatabaseAdvertise, mDatabaseUser;
    Map<String, Object> mapFavorite;
    boolean isChineseApp;
    boolean showType;
    int widthBase;
    Map<String, String> imagesUser;
    public static Boolean isScrolling = true;
    private AdvertiseFollowingAdapter.ItemFilter mFilter = new AdvertiseFollowingAdapter.ItemFilter();

    public OnChangeFavorite getChangeFavorite() {
        return changeFavorite;
    }

    public void setChangeFavorite(OnChangeFavorite changeFavorite) {
        this.changeFavorite = changeFavorite;
    }

    private AdvertiseFollowingAdapter.OnChangeFavorite changeFavorite;

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
        public ImageView thumbnail;
        public VideoView videoView;
        public ImageButton ibFavoritePostInList;
        public ImageButton ibSharePostInList;
        public TextView tvNumberFavoriteInList;
        public ImageView imvAvatarUser;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.titleCardHomeView);
            description = (TextView) view.findViewById(R.id.description);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            createdDate = (TextView) view.findViewById(R.id.tvCreatedDate);
            sale = (TextView) view.findViewById(R.id.sale);
            videoView = (VideoView) view.findViewById(R.id.videoPreviewListAdvertise);
            ibFavoritePostInList = (ImageButton) view.findViewById(R.id.ibFavoritePostInList);
            ibSharePostInList = (ImageButton) view.findViewById(R.id.ibSharePostInList);
            tvNumberFavoriteInList = (TextView) view.findViewById(R.id.tvNumberFavoriteInList);
            imvAvatarUser = (ImageView) view.findViewById(R.id.imvAvatarUser);
        }


    }

    public AdvertiseFollowingAdapter(Context mContext, List<Advertise> advertiseList, List<Category> categories, UserProfileApi user,
                                     List<Favorite> favorites, boolean showType, Map<String, String> imagesUser) {
        this.mContext = mContext;
        this.advertiseList = advertiseList;
        this.categories = categories;
        this.user = user;
        this.favorites = favorites;
//        mDatabaseAdvertise = FirebaseDatabase.getInstance()
//                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES);
//        mDatabaseUser = FirebaseDatabase.getInstance()
//                .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS);
        //TODO GET USER FAVORITE
        /*if (user != null) {
            mapFavorite = user.getFavorites();
        }*/
        this.originalData = advertiseList;
        this.mObjects = advertiseList;
        this.showType = showType;
        if (mContext != null) {
            String languageToLoad = LocaleHelper.getLanguage(mContext);
            isChineseApp = languageToLoad.toString().equals("zh");

        }
        this.imagesUser = imagesUser;
    }

    public void updateList(List<Advertise> data) {
        mObjects = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.card_view_following, parent, false);
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
            final Advertise advertise = mObjects.get(position);
            // Log.e("onBindViewHolder", Integer.toString(mObjects.size()));
            if (advertise != null) {
                if (advertise != null && advertise.getName() != null && !advertise.getName().isEmpty() && advertise.getName().contains("-")) {
                    String languageToLoad = LocaleHelper.getLanguage(mContext);
                    String[] separated = advertise.getName().toString().split("-");
                    String stChinese = separated != null && separated.length > 0 ? separated[0] : "";
                    String stEnglish = separated != null && separated.length > 1 ? separated[1] : "";
                    if (!languageToLoad.toString().equals("en")) {
                        holder.title.setText(stChinese);
                    } else {
                        holder.title.setText(stEnglish);
                    }
                } else if (advertise.getName() != null) {
                    holder.title.setText(advertise.getName());
                }
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
                }
*/
                if (imagesUser != null && imagesUser.size() > 0 && imagesUser.get(advertise.getUserId()) != null) {
                    if (holder.imvAvatarUser.getContext() != null) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity)holder.imvAvatarUser.getContext()).isDestroyed())
//                        {

                        try {
                            Glide.with(holder.imvAvatarUser.getContext()).load(imagesUser.get(advertise.getUserId())).centerCrop()
                                    .transform(new CircleTransform(holder.imvAvatarUser.getContext())).override(40, 40).into(holder.imvAvatarUser);
                        } catch (Exception ex) {

                        }
//                        }
                    }
                }

                if (advertise != null && advertise.getImageHeader() != null
                        && advertise.getImageHeader().getImageUrl() != null && !advertise.getImageHeader().getImageUrl().isEmpty()) {
                    if (mContext != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
                            Glide
                                    .with(mContext)
                                    .load(advertise.getImageHeader().getImageUrl()).thumbnail(0.2f).skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.thumbnail);
                        }
                    }

                } else if (advertise != null && advertise.getVideo() != null && advertise.getVideo().getVideoImage() != null
                        && !advertise.getVideo().getVideoImage().getImageUrl().isEmpty()) {
                    if (mContext != null) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
//
//                        }
                        try {
                            Glide
                                    .with(mContext)
                                    .load(advertise.getVideo().getVideoImage().getImageUrl()).thumbnail(0.2f).skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(holder.thumbnail);
                        } catch (Exception ex) {

                        }
                    }


                } else {

                    if (mContext != null && categories != null) {
                        for (Category category : categories) {
                            if (category.getId().equals(advertise.getCategoryId()) && category.getIcons() != null) {
                                if (isChineseApp) {
                                    Image icon = category.getIcons().getIconChinese();
                                    if (icon.getImageUrl() != null && !icon.getImageUrl().isEmpty()) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) holder.imvAvatarUser.getContext()).isDestroyed()) {
                                            Glide.with(holder.imvAvatarUser.getContext()).load(icon.getImageUrl()).thumbnail(0.2f).skipMemoryCache(true).into(holder.thumbnail);
                                        }

                                    }

                                } else {
                                    Image icon = category.getIcons().getIconEnglish();
                                    if (icon.getImageUrl() != null && !icon.getImageUrl().isEmpty()) {
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) holder.thumbnail.getContext()).isDestroyed()) {
                                        try {
                                            Glide.with(holder.thumbnail.getContext()).load(icon.getImageUrl()).thumbnail(0.2f).skipMemoryCache(true).into(holder.thumbnail);
                                        } catch (Exception ex) {

                                        }
//                                        }

                                    }
                                }

                                break;
                            }

                        }
                    }
                }

                boolean isFavorite = false;
                if (user != null) {
                    //TODO GET USER FAVORITE
                    //  mapFavorite = user.getFavorites();
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
                            holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                        } else {
                            holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
                        }

                    } else {
                        if (isFavorite) {
                            holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                        } else {
                            holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
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
                                        holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                                    } else {
                                        holder.ibFavoritePostInList.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
                                    }

                                } else {
                                    if (isFavorite) {
                                        holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                                    } else {
                                        holder.ibFavoritePostInList.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !((Activity) mContext).isDestroyed()) {
            try {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext);
                dlgAlert.setMessage(message);
                dlgAlert.setTitle(title);
                dlgAlert.setPositiveButton(mContext.getResources().getString(R.string.ok), null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } catch (Exception ex) {

            }
        }
    }

    public void broadcastIntent(String advertiseId) {
        Intent intent = new Intent(MenuActivity.ACTION_UPDATE_FAVORITE);
        intent.putExtra("advertiseId", advertiseId);
        mContext.sendBroadcast(intent);
    }

    public boolean processFavorite(final Advertise advertise) {
        boolean isFavorite = false;
        if (user != null && advertise != null && user.getLegacy_id() != advertise.getUserId()) {

            if (mapFavorite == null) {
                isFavorite = true;
                mapFavorite = new HashMap<>();
                mapFavorite.put(advertise.getId(), isFavorite);
                int numOfFavorite = advertise.getFavoriteCount();
                advertise.setFavoriteCount(numOfFavorite + 1);
                FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertise.getId()).setValue(advertise);
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
                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertise.getId()).setValue(advertise);
                } else {
                    isFavorite = true;
                    mapFavorite.put(advertise.getId(), isFavorite);
                    int numOfFavorite = advertise.getFavoriteCount();
                    advertise.setFavoriteCount(numOfFavorite + 1);
                    FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.ADVERTISES).child(advertise.getId()).setValue(advertise);
                }
            }


            // broadcastIntent();
            //TODO GET USER FAVORITE
            // user.setFavorites(mapFavorite);
            final boolean finalIsFavorite = isFavorite;
            if (user != null) {
                PrefManager prefManager = new PrefManager(mContext);
                //TODO GET USER FAVORITE
                //prefManager.setUser(user);
                FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(Config.FIREBASE_URL).child(Constant.USERS).child(user.getLegacy_id()).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            // showMessageError(getResources().getString(R.string.error), getResources().getString(R.string.error_your_post_was_not_successful));
                        } else {
                            if (!finalIsFavorite) {
                                broadcastIntent(advertise.getId());
                            }
                        }
                    }
                });
            }

        }

        return isFavorite;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Advertise> list = originalData;

            int count = list.size();
            final ArrayList<Advertise> nlist = new ArrayList<Advertise>(count);

            for (Advertise temp : originalData) {
                if (temp.getName().toLowerCase().contains(filterString) || temp.getDescription().toLowerCase().contains(filterString)) { //returns true if name matchs with Persons name
                    nlist.add(temp);
                }
            }
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (ArrayList<Advertise>) results.values;
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
    public void insert(int position, Advertise data) {
        mObjects.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Advertise data) {
        int position = mObjects.indexOf(data);
        mObjects.remove(position);
        notifyItemRemoved(position);
    }

    public interface DynamicHeight {
        void HeightChange(int position, int height);
    }
}
