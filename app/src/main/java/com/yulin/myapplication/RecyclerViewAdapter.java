package com.yulin.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;
    private List<DramaBean> mData;

    public RecyclerViewAdapter(Context context, List<DramaBean> data) {
        this.mContext = context;
        this.mData = data;
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
        DramaBean rowData = mData.get(position);
        Picasso.with(mContext)
                .load(rowData.getThumb())
                .fit()
                .centerInside()
                .into(rowView.thumb);
        rowView.name.setText(rowData.getName());
        rowView.rating.setText(String.format("%s", rowData.getRating()));
        rowView.createdAt.setText(Utils.parserDateFormat(rowData.getCreated_at()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

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
}
