package com.sosokan.android.mvp.advertise;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sosokan.android.models.AdvertiseApi;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macintosh on 3/17/17.
 */

public class AdvertiseResponse {
    @SerializedName("results")
    @Expose
    private List<AdvertiseApi> results = new ArrayList<AdvertiseApi>();

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("previous")
    @Expose
    private String previous;

    public List<AdvertiseApi> getResults() {
        return results;
    }

    public void setResults(List<AdvertiseApi> results) {
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
