package com.sosokan.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sosokan.android.R;
import com.sosokan.android.models.CommentNew;
import com.sosokan.android.models.User;
import com.sosokan.android.rest.UrlEndpoint;
import com.sosokan.android.ui.activity.EditProfileActivity;
import com.sosokan.android.ui.activity.PrefManager;
import com.sosokan.android.utils.ApplicationUtils;
import com.sosokan.android.utils.Constant;
import com.sosokan.android.utils.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macintosh on 2/5/17.
 */

public class CommentsApiAdapter extends RecyclerView.Adapter<CommentsApiAdapter.MyViewHolder> {
    private static final String TAG = "CommentsApiAdapter";
    private Context mContext;
    private List<CommentNew> comments;
    private int resource;
    private Listener listener;
    private RequestQueue mQueue;

    int selected_position = 0;
    int widthBase;
    PrefManager prefManager;
    User user;
    private static final String urlComment = new UrlEndpoint().getUrlApi(Constant.COMMENTS);
    private String token;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public interface Listener {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvDate, tvContent, tvNumberFavoriteComment;
        public ImageView imvAvatar, ivReportComment;
        public ImageButton ibFavoriteComment;


        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            imvAvatar = (ImageView) view.findViewById(R.id.imvAvatarComment);
            tvNumberFavoriteComment = (TextView) view.findViewById(R.id.tvNumberFavoriteComment);
            ibFavoriteComment = (ImageButton) view.findViewById(R.id.ibFavoriteComment);
            ivReportComment = (ImageView) view.findViewById(R.id.ivReportComment);
        }
    }


    public CommentsApiAdapter(Context mContext, List<CommentNew> comments) {
        this.mContext = mContext;
        this.comments = comments;
        token = new UrlEndpoint().getUserToken(mContext);
        mQueue = Volley.newRequestQueue(this.mContext);
        prefManager = new PrefManager(this.mContext);
        user = prefManager.getUser();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (comments != null && comments.size() > 0 && position < comments.size()) {
            final CommentNew comment = comments.get(position);
            if (comment != null) {
                holder.tvName.setText(comment.getUser_name());
                holder.tvContent.setText(comment.getComment());
                holder.tvDate.setText(DateUtils.getTimeAgo(comment.getSubmit_date(), mContext));
                holder.tvNumberFavoriteComment.setText(String.valueOf(comment.getRating_likes()));
                if (comment.getAvatar() != null && !comment.getAvatar().isEmpty()) {
                    Glide.with(holder.imvAvatar.getContext()).load(comment.getAvatar()).centerCrop()
                            .transform(new CircleTransform(holder.imvAvatar.getContext())).override(40, 40).into(holder.imvAvatar);

                }

                final int sdk = Build.VERSION.SDK_INT;
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    if (comment.isUser_voted()) {
                        holder.ibFavoriteComment.setBackgroundDrawable(holder.ibFavoriteComment.getContext().getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                    } else {
                        holder.ibFavoriteComment.setBackgroundDrawable(holder.ibFavoriteComment.getContext().getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
                    }

                } else {
                    if (comment.isUser_voted()) {
                        holder.ibFavoriteComment.setBackground(holder.ibFavoriteComment.getContext().getResources().getDrawable(R.drawable.ic_favorite_orange_30dp));
                    } else {
                        holder.ibFavoriteComment.setBackground(holder.ibFavoriteComment.getContext().getResources().getDrawable(R.drawable.ic_favorite_border_grey_24dp));
                    }

                }
                holder.imvAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ((comment.getFirebase_user_id() != null && user != null && comment.getFirebase_user_id().equals(user.getId())) ||
                                comment.getUser_email() != null && user != null && comment.getUser_email().equals(user.getEmail())) {
                            gotoEditProfile();
                        }else{

                        }

                    }
                });

                holder.ibFavoriteComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!comment.isUser_voted()) {
                             likeComment(comment.getId(),position,comment);
                        }else if (comment.isUser_voted()) {
                            unlikeComment(comment.getId(),position,comment);
                        }
                    }
                });

                holder.ivReportComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });

            }


        }


    }


    private void gotoEditProfile() {
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void likeComment(int commentId, final int position, final CommentNew commentNew) {
        int rating = commentNew.getRating_likes();
        final int originRating  = commentNew.getRating_likes();
        rating++;
        commentNew.setRating_likes(rating);
        commentNew.setUser_voted(true);
        comments.set(position,commentNew);
        notifyItemChanged(position);
        String url = urlComment + commentId + "/rate/1";
        Log.e(TAG, "likeComment url  " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            Log.e(TAG, "likeComment response  " + response);
                            if (response.toString().equals("1") || response.toString().equals("456")) {

                            }else{

                                commentNew.setRating_likes(originRating);
                                commentNew.setUser_voted(true);
                                comments.set(position,commentNew);
                                notifyItemChanged(position);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        ApplicationUtils.closeMessage();
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                return handleParseNetworkResponse(response);
            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader();
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        mQueue.add(postRequest);


    }

    @NonNull
    private Response<String> handleParseNetworkResponse(NetworkResponse response) {
        String utf8String;
        try {
            utf8String = new String(response.data, "UTF-8");
            return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    public void unlikeComment(int commentId, final int position, final CommentNew commentNew) {
        int rating = commentNew.getRating_likes();
        final int originRating  = commentNew.getRating_likes();
        if(rating>0)
        {
            rating--;
        }
        commentNew.setRating_likes(rating);
        commentNew.setUser_voted(false);
        comments.set(position,commentNew);
        notifyItemChanged(position);
        String url = urlComment + commentId + "/rate/-1";
        Log.e(TAG, "unlikeComment url  " + url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null) {
                            Log.e(TAG, "unlikeComment response  " + response);
                            if (response.toString().equals("0") || response.toString().equals("455")) {

                            }else{

                                commentNew.setRating_likes(originRating);
                                commentNew.setUser_voted(true);
                                comments.set(position,commentNew);
                                notifyItemChanged(position);
                            }
                        }
                    }
                },
                volleyErrListener
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                return handleParseNetworkResponse(response);
            }


            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader();
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                1f));
        mQueue.add(postRequest);



    }

    private Response.ErrorListener volleyErrListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // TODO: handle error
            Log.e(TAG, "Error: " + error.getMessage());
            ApplicationUtils.closeMessage();
        }
    };
    private Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Authorization", token);
        return headers;
    }
}
