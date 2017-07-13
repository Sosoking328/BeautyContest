package com.sosokan.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sosokan.android.R;
import com.sosokan.android.models.FAQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FAQAdapter extends ArrayAdapter<FAQ> implements Filterable {
    private final Object mLock = new Object();
    private int resource;
    private Context mContext;
    private List<FAQ> mObjects;
    private List<FAQ> originalData;
    private ArrayList<FAQ> mOriginalValues;
    private List<FAQ> filteredData = null;
    boolean isShowAnswer = true;
    private ItemFilter mFilter = new ItemFilter();

    public FAQAdapter(Context context, int resource, List<FAQ> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.mContext = context;
        this.mObjects = objects;
        this.originalData = objects;
        filteredData = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FAQAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new FAQAdapter.ViewHolder();
            convertView = ((Activity) mContext).getLayoutInflater().inflate(resource, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitleFAQ);
            viewHolder.tvAnswer = (TextView) convertView.findViewById(R.id.tvAnswerFAQ);
           // viewHolder.imvDown = (ImageView) convertView.findViewById(R.id.imvDown);
            viewHolder.view = convertView.findViewById(R.id.view);
            viewHolder.rlQuestionFAQ  = (RelativeLayout) convertView.findViewById(R.id.rlQuestionFAQ);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FAQAdapter.ViewHolder) convertView.getTag();
        }

        FAQ inbox = getItem(position);
        if (inbox != null) {
            viewHolder.tvTitle.setText(inbox.getTitleFAQ());
            viewHolder.tvAnswer.setText(inbox.getAnswerFAQ());
        }
        viewHolder.rlQuestionFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowAnswer) {
                    viewHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.orange));
                    viewHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_arrow_drop_up_orange_24dp, 0);
                    viewHolder.view.setVisibility(View.VISIBLE);
                    viewHolder.tvAnswer.setVisibility(View.VISIBLE);
                 //   viewHolder.imvDown.setBackground(mContext.getResources().getDrawable(R.drawable.ic_up_orange));
                } else {
                    viewHolder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.black));
                    viewHolder.tvTitle.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
                    viewHolder.tvAnswer.setVisibility(View.GONE);
                    viewHolder.view.setVisibility(View.GONE);
                   // viewHolder.imvDown.setBackground(mContext.getResources().getDrawable(R.drawable.down_arrow));
                }
                isShowAnswer = !isShowAnswer;
            }
        });
        return convertView;
    }

    @Override
    public FAQ getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getPosition(FAQ item) {
        return mObjects.indexOf(item);
    }

    @Override
    public void addAll(Collection<? extends FAQ> collection) {
        synchronized (mLock) {
            mObjects.addAll(collection);
        }
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    private class ViewHolder {
        TextView tvTitle, tvAnswer;
     //  ImageView imvDown;
        RelativeLayout rlQuestionFAQ;
        View view;
    }
    public Filter getFilter() {
        return mFilter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<FAQ> list = originalData;

            int count = list.size();
            final ArrayList<FAQ> nlist = new ArrayList<FAQ>(count);

            for(FAQ temp:originalData){
                if(temp.getTitleFAQ().toLowerCase().contains(filterString)||temp.getAnswerFAQ().toLowerCase().contains(filterString)){ //returns true if name matchs with Persons name
                    nlist.add(temp);
                }
            }
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mObjects = (ArrayList<FAQ>) results.values;
            notifyDataSetChanged();
        }

    }
}
