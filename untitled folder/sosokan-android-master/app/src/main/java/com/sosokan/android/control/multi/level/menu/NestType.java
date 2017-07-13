package com.sosokan.android.control.multi.level.menu;

/**
 * Created by AnhZin on 10/3/2016.
 */


/**
 * MultiLevelListView nest types.
 */
public enum NestType {

    /**
     * SINGLE nest type. Only one group item is expanded at the same time.
     */
    SINGLE(0),
    /**
     * MULTIPLE nest type. Any group items are expandnded at the same time.
     */
    MULTIPLE(1);

    private int mValue;

    /**
     * Constructor.
     *
     * @param value nest type value.
     */
    NestType(int value) {
        mValue = value;
    }

    /**
     * Gets nest type value.
     *
     * @return Nest type value.
     */
    public int getValue() {
        return mValue;
    }

    /**
     * Converts integer to nest type.
     *
     * @param value nest type as integer.
     * @return Nest type value.
     */
    public static NestType fromValue(int value) {
        switch (value) {
            case 0:
                return SINGLE;
            case 1:
            default:
                return MULTIPLE;
        }
    }
}
