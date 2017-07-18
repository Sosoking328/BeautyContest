package com.sosokan.android.events;

public class ToolBarNavigationEvent {
    public static final int UP = 0;
    public static final int DOWN = 1;
    private int action;

    public ToolBarNavigationEvent(int action) {

        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
