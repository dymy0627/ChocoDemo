package com.yulin.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowViewHolder> implements Filterable {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<DramaBean> mData;
    private List<DramaBean> mFilteredData;
    private ItemFilter mFilter = new ItemFilter();

    public RecyclerViewAdapter(Context context, List<DramaBean> data) {
        this.mContext = context;
        this.mData = data;
        this.mFilteredData = data;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.row_drama, parent, false);
        return new RowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder rowView, int position) {
        Log.d(TAG, "onBindViewHolder: position=" + position);
        DramaBean rowData = mFilteredData.get(position);
        Picasso.with(mContext)
                .load(rowData.getThumb())
                .fit()
                .centerInside()
                .into(rowView.thumb);
        rowView.name.setText(rowData.getName());
        rowView.rating.setText(String.format("%s", rowData.getRating()));
        rowView.createdAt.setText(Utils.parserDateFormat(rowData.getCreated_at()));
    }

    public DramaBean getItem(int position) {
        return mFilteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mFilteredData.size();
    }

    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    class RowViewHolder extends RecyclerView.ViewHolder {

        ImageView thumb;
        TextView name;
        TextView rating;
        TextView createdAt;

        RowViewHolder(View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.drama_thumb);
            name = itemView.findViewById(R.id.drama_name);
            rating = itemView.findViewById(R.id.drama_rating_value);
            createdAt = itemView.findViewById(R.id.drama_createdAt_value);

            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            int allDataSize = mData.size();
            final List<DramaBean> filteredList = new ArrayList<>(allDataSize);
            for (int i = 0; i < allDataSize; i++) {
                DramaBean dramaBean = mData.get(i);
                if (dramaBean.getName().toLowerCase().contains(filterString)) {
                    filteredList.add(dramaBean);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredData = (ArrayList<DramaBean>) results.values;
            notifyDataSetChanged();
        }
    }
}
