package com.sosokan.android.models;

/**
 * Created by macintosh on 1/17/17.
 */

public class FlagChoice {
    private String id;
    private String reason;

    public FlagChoice(String id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
