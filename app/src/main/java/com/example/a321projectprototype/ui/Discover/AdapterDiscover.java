package com.example.a321projectprototype.ui.Discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsCardviewAdpator;

import java.util.ArrayList;
import java.util.List;


public class AdapterDiscover  extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder> {
    ArrayList<ItemDataModel> FullList;
    ArrayList<ItemDataModel> dataSet;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.past_recording_dates_textview);
        }
    }

    AdapterDiscover(ArrayList<ItemDataModel> listItem) {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);
    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_discover_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder holder, int position) {
        ItemDataModel currentItem = dataSet.get(position);
        holder.tvName.setText(currentItem.getTxtname());
    }

    public Filter getFilter() {
        return Searched_Filter;
    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ItemDataModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(FullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemDataModel item : FullList) {
                    if (item.getTxtname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet.clear();
            dataSet.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

    };
}


