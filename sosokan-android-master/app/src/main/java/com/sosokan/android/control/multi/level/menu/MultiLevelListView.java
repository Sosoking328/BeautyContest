package com.sosokan.android.control.multi.level.menu;

/**
 * Created by AnhZin on 10/3/2016.
 */


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.sosokan.android.R;
import com.sosokan.android.models.CategoryNew;
import com.sosokan.android.utils.Constant;

/**
 * MultiLevelListView.
 */
public class MultiLevelListView extends FrameLayout {

    private ListView mListView;

    private boolean mAlwaysExpanded;
    private NestType mNestType;

    public MultiLevelListAdapter getmAdapter() {
        return mAdapter;
    }

    private MultiLevelListAdapter mAdapter;
    private OnItemCategoryClickListener mOnItemCategoryClickListener;
    public String categorySelectedId = Constant.sosokanCategoryAll;
    /**
     * View constructor.
     */
    public MultiLevelListView(Context context) {
        super(context);

        initView(null);
    }

    /**
     * View constructor.
     */
    public MultiLevelListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(attrs);
    }

    /**
     * View constructor.
     */
    public MultiLevelListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initView(attrs);
    }

    /**
     * Indicates if view is always expanded.
     *
     * @return true if view is always expanded, false otherwise.
     */
    public boolean isAlwaysExpanded() {
        return mAlwaysExpanded;
    }

    /**
     * Sets whether view should be always expanded or not.
     *
     * @param alwaysExpanded desired always expanded value.
     */
    public void setAlwaysExpanded(boolean alwaysExpanded) {
        if (mAlwaysExpanded == alwaysExpanded) {
            return;
        }
        mAlwaysExpanded = alwaysExpanded;
        if (mAdapter != null) {
            mAdapter.reloadData();
        }
    }

    /**
     * Sets view nesting type.
     *
     * @param nestType desired nest type.
     */
    public void setNestType(NestType nestType) {
        if (mNestType == nestType) {
            return;
        }
        mNestType = nestType;
        notifyDataSetChanged();
    }

    /**
     * Gets view nest type.
     *
     * @return nest type.
     */
    public NestType getNestType() {
        return mNestType;
    }

    /**
     * Initializes view
     *
     * @param attrs used attribute set
     */
    private void initView(AttributeSet attrs) {
        confWithAttributes(attrs);

        addView(mListView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mListView.setOnItemClickListener(new OnProxyItemClickListener());
    }

    /**
     * Sets Android ListView layout id or creates new when 0 is passed.
     *
     * @param listLayoutId Android ListView layout id, 0 is possible.
     */
    private void setList(int listLayoutId) {
        if (listLayoutId == 0) {
            mListView = new ListView(getContext());
        } else {
            mListView = (ListView) LayoutInflater.from(getContext()).inflate(listLayoutId, null);
        }
    }

    /**
     * Configurates view.
     *
     * @param attrs used attribute set.
     */
    private void confWithAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiLevelListView, 0, 0);
        try {
            setAlwaysExpanded(typedArray.getBoolean(R.styleable.MultiLevelListView_alwaysExtended, false));
            setNestType(NestType.fromValue(typedArray.getInt(R.styleable.MultiLevelListView_nestType, NestType.SINGLE.getValue())));
            setList(typedArray.getResourceId(R.styleable.MultiLevelListView_list, 0));
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Sets list adapter.
     *
     * @param adapter Used adapter.
     */
    public void setAdapter(MultiLevelListAdapter adapter) {
        if (mAdapter != null) {
            try {
                mAdapter.unregisterView(this);
            }catch (Exception ex)
            {
                System.err.println("setAdapter: " +ex.toString());
            }
        }

        mAdapter = adapter;

        if (adapter == null) {
            return;
        }

        try {
            adapter.registerView(this);
        }catch ( Exception ex)
        {
            System.err.println("setAdapter: " +ex.toString());
        }
    }

    /**
     * Sets list item click callback listener.
     *
     * @param listener Callback listener.
     */
    public void setOnItemClickListener(OnItemCategoryClickListener listener) {
        mOnItemCategoryClickListener = listener;
    }

    /**
     * Notifies adapter that data set changed.
     */
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Gets wrapped Android ListView instance.
     *
     * @return Wrapped Android ListView instance.
     */
    ListView getListView() {
        return mListView;
    }

    /**
     * Helper class used to display created flat list of item's using Android's ListView.
     */
    class OnProxyItemClickListener implements AdapterView.OnItemClickListener {

        /**
         * Notifies that certain node was clicked.
         *
         * @param view Clicked view (provided by the adapter).
         * @param node Clicked node.
         */
        private void notifyItemClicked(View view, Node node) {
            if (mOnItemCategoryClickListener != null) {
                mOnItemCategoryClickListener.onItemClicked(MultiLevelListView.this, view, node.getObject(), node.getItemInfo());
            }
        }

        /**
         * Notifies that certain group node was clicked.
         *
         * @param view Clicked view (provided by the adapter).
         * @param node Clicked group node.
         */
        private void notifyGroupItemClicked(View view, Node node) {
            if (mOnItemCategoryClickListener != null) {
                mOnItemCategoryClickListener.onGroupItemClicked(MultiLevelListView.this, view, node.getObject(), node.getItemInfo());
            }
        }

        /**
         * Handles certain node click event.
         *
         * @param view Clicked view (provided by the adapter).
         * @param node Clicked node
         */
        private void onItemClicked(View view, Node node) {
            Object object = node.getObject();
            CategoryNew category = (CategoryNew) object;
            categorySelectedId = category.getLegacy_id();
            MultiLevelListAdapter.setCategorySelectedId(categorySelectedId);
            Log.e("onGroupItemClicked",categorySelectedId + "  ");
            notifyItemClicked(view, node);
        }

        /**
         * Scrolls to click event if necessary.
         *
         * @param itemIndex Clicked item index.
         */
        private void scrollToItemIfNeeded(int itemIndex) {
            int first = mListView.getFirstVisiblePosition();
            int last = mListView.getLastVisiblePosition();

            if ((itemIndex < first) || (itemIndex > last)) {
                mListView.smoothScrollToPosition(itemIndex);
            }
        }

        /**
         * Notifies certain group node click event.
         *
         * @param view Clicked view (provided by the adapter).
         * @param node Clicked group node.
         */
        private void onGroupItemClicked(View view, Node node) {
            boolean isExpanded = node.isExpanded();
            Object object = node.getObject();
            CategoryNew category = (CategoryNew) object;
            categorySelectedId = category.getLegacy_id();
            MultiLevelListAdapter.setCategorySelectedId(categorySelectedId);
            Log.e("onGroupItemClicked",categorySelectedId + "  ");
            if (!isAlwaysExpanded()) {
                if (isExpanded) {
                    mAdapter.collapseNode(node);
                } else {
                    mAdapter.extendNode(node, mNestType);
                }
            }

            if (mNestType == NestType.SINGLE) {
                scrollToItemIfNeeded(mAdapter.getFlatItems().indexOf(node));
            }

            notifyGroupItemClicked(view, node);
        }

        /**
         * Handles wrapped Android ListView item click event.
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Node node = mAdapter.getFlatItems().get(position);
            Object object = node.getObject();
            CategoryNew category = (CategoryNew) object;

            if(category!= null)
            {
                categorySelectedId = category.getLegacy_id();
                MultiLevelListAdapter.setCategorySelectedId(categorySelectedId);
                Log.e("onItemClick",categorySelectedId + " ----------------- position:  "+position);
                if (node.isExpandable()) {
                    Log.e("onItemClick",categorySelectedId + " ----------------- node.isExpandable():  "+node.isExpandable());
                    onGroupItemClicked(view, node);
                } else {
                    Log.e("onItemClick",categorySelectedId + " ----------------- NOT node.isExpandable():  ");
                    onItemClicked(view, node);
                }
            }

        }
    }
}
