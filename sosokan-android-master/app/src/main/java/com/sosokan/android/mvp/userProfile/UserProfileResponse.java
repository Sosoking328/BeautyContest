package com.sosokan.android.mvp.userProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sosokan.android.models.UserProfile;
import com.sosokan.android.models.UserProfileApi;
import com.sosokan.android.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macintosh on 3/16/17.
 */

public class UserProfileResponse {

    @SerializedName("results")
    @Expose
    private List<UserProfileApi> results = new ArrayList<UserProfileApi>();

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("previous")
    @Expose
    private String previous;

    public List<UserProfileApi> getResults() {
        return results;
    }

    public void setResults(List<UserProfileApi> results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
