package com.sosokan.android.models;

/**
 * Created by macintosh on 3/8/17.
 */

public class WechatError {
    public int errcode;
    public String errmsg;

    public WechatError() {
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
