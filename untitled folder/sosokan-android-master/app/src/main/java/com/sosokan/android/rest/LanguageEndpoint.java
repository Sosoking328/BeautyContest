package com.sosokan.android.rest;

import com.sosokan.android.utils.Constant;

/**
 * Created by macintosh on 2/20/17.
 */

public class LanguageEndpoint {
    public boolean isChinse;

    public boolean isChinse() {
        return isChinse;
    }

    public void setChinse(boolean chinse) {
        isChinse = chinse;
    }

    public LanguageEndpoint(boolean isChinse) {
        this.isChinse = isChinse;
    }

    public LanguageEndpoint() {

    }

    public String getLanguage(Boolean isChinese)
    {
        return isChinese? Constant.LANG_CN:Constant.LANG_EN;
    }
}
