package com.sosokan.android.models;

/**
 * Created by gwh on 1/19/2017.
 * List of ads, downloaded from http://sosokan-staging.herokuapp.com/api/ads
 */

public class AdListApi {



    public AdListApi(){
    }

    public AdListApi(int count, String next, String previous) {
        this.count = count;
        this.next = next;
        this.previous = previous;
    }

    private int count;
    private String next;
    private String previous;
    private AdvertiseApi advertiseApi[];

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

    public AdvertiseApi[] getAdvertiseApi() {
        return advertiseApi;
    }

    public void setAdvertiseApi(AdvertiseApi[] advertiseApi) {
        this.advertiseApi = advertiseApi;
    }

}
