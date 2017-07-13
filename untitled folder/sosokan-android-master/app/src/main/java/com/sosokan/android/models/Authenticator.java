package com.sosokan.android.models;

/**
 * Created by macintosh on 1/18/17.
 */

public class Authenticator {
    private String accessToken;

    private String csrfToken;

    public Authenticator(String accessToken, String csrfToken) {
        this.accessToken = accessToken;
        this.csrfToken = csrfToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }
}
