package com.sosokan.android.events;

import java.io.Serializable;

public class FragmentEvent implements Serializable {
    public static final String EVENT = "event";
    public static final int ACTION_TO_HOME = 1;
    public static final int ACTION_TO_INBOX = 2;
    public static final int ACTION_TO_PICK_UP = 3;
    public static final int ACTION_TO_MY_TRIP = 4;
    public static final int ACTION_TO_PICK_UP_WITH_METER = 6;
    public static final int ACTION_TO_HAILING_WITH_METER = 5;
    public static final int ACTION_TO_RECEIPT_WITH_METER = 7;
    public static final int ACTION_TO_HARDWARE_METER = 8;
    private int action;
    private Object model;

    public FragmentEvent(int action) {
        this.action = action;
    }

    public FragmentEvent(int action, Object model) {
        this.action = action;
        this.model = model;
    }

    public int getAction() {
        return action;
    }

    public Object getModel() {
        return model;
    }
}
