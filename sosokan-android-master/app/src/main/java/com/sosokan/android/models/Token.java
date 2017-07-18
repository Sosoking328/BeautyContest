package com.sosokan.android.models;

/**
 * Created by macintosh on 3/17/17.
 */

public class Token {
    private String key;
    private String created;
    private int user;

    public Token() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
