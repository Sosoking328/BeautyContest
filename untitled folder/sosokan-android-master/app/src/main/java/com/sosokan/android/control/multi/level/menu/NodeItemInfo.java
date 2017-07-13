package com.sosokan.android.control.multi.level.menu;

/**
 * Created by AnhZin on 10/3/2016.
 */


/**
 * Class used to get information about list item and its location in MultiLevelListView.
 *
 * ItemInfo interface implementation.
 */
class NodeItemInfo implements ItemInfo {

    private Node mNode;

    public NodeItemInfo(Node node) {
        mNode = node;
    }

    /**
     * Gets node level. Levels starts from 0.
     *
     * @return Item level.
     */
    @Override
    public int getLevel() {
        return mNode.getLevel();
    }

    /**
     * Gets number of nodes with node level at the same hierarchy.
     *
     * @return Total number of items belonging to item's level.
     */
    @Override
    public int getIdxInLevel() {
        return mNode.getIdxInLevel();
    }

    /**
     * Gets node index within level.
     *
     * @return Node index.
     */
    @Override
    public int getLevelSize() {
        return mNode.getLevelSize();
    }

    /**
     * Gets info if node is expanded.
     *
     * @return true if node is expanded, false otherwise.
     */
    @Override
    public boolean isExpanded() {
        return mNode.isExpanded();
    }

    /**
     * Gets info if node is expandable.
     *
     * @return true if node is expandable, false otherwise.
     */
    @Override
    public boolean isExpandable() {
        return mNode.isExpandable();
    }

    @Override
    public boolean isSelected() {
        return mNode.isSelected();
    }

}
