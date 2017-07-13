package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class CommentNew {
    private int id;
    private String object_pk;
    private String user_name;
    private String user_email;
    private String user_url;
    private String comment;
    private String ip_address;
    private boolean is_public;
    private boolean is_removed;
    private String submit_date;
    private String firebase_user_id;
    private String parent_id;
    private int rating_likes;
    private String avatar;
    private boolean user_voted;

    public CommentNew() {
    }

    public String getObject_pk() {
        return object_pk;
    }

    public void setObject_pk(String object_pk) {
        this.object_pk = object_pk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUser_voted() {
        return user_voted;
    }

    public void setUser_voted(boolean user_voted) {
        this.user_voted = user_voted;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIp_address() {
        return ip_address;
    }

    public int getRating_likes() {
        return rating_likes;
    }

    public void setRating_likes(int rating_likes) {
        this.rating_likes = rating_likes;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String isIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public boolean is_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public boolean is_removed() {
        return is_removed;
    }

    public void setIs_removed(boolean is_removed) {
        this.is_removed = is_removed;
    }

    public String getSubmit_date() {
        return submit_date;
    }

    public void setSubmit_date(String submit_date) {
        this.submit_date = submit_date;
    }


    public String getFirebase_user_id() {
        return firebase_user_id;
    }

    public void setFirebase_user_id(String firebase_user_id) {
        this.firebase_user_id = firebase_user_id;
    }


}
