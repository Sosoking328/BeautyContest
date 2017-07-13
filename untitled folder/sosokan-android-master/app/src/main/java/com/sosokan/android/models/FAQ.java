package com.sosokan.android.models;

import java.io.Serializable;

/**
 * Created by phuong.tran on 8/31/2016.
 */

public class FAQ implements Serializable {
    String titleFAQ;
    String answerFAQ;

    public FAQ(String titleFAQ, String answerFAQ) {
        this.titleFAQ = titleFAQ;
        this.answerFAQ = answerFAQ;
    }

    public String getAnswerFAQ() {
        return answerFAQ;
    }

    public void setAnswerFAQ(String answerFAQ) {
        this.answerFAQ = answerFAQ;
    }

    public String getTitleFAQ() {
        return titleFAQ;
    }

    public void setTitleFAQ(String titleFAQ) {
        this.titleFAQ = titleFAQ;
    }
}
