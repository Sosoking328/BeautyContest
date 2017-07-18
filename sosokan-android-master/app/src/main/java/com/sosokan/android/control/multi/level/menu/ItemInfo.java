package com.sosokan.android.control.multi.level.menu;

/**
 * Created by AnhZin on 10/3/2016.
 */

/**
 * Interface used to get information about list item and its location in MultiLevelListView.
 */
public interface ItemInfo {

    /**
     * Gets item level. Levels starts from 0.
     *
     * @return Item level.
     */
    int getLevel();

    /**
     * Gets number of items with item level at the same hierarchy.
     *
     * @return Total number of items belonging to item's level.
     */
    int getLevelSize();

    /**
     * Gets item index within level.
     *
     * @return Item index.
     */
    int getIdxInLevel();

    /**
     * Gets info if item is extended.
     *
     * @return true if item is extended, false otherwise.
     */
    boolean isExpanded();

    /**
     * Gets info if item is expandable.
     *
     * @return true if item is expandable, false otherwise.
     */
    boolean isExpandable();

    boolean isSelected();
}