package com.example.a321projectprototype.ui.Flock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Discover.AdapterDiscover;
import com.example.a321projectprototype.ui.Discover.ItemDataModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterFlock extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder> {
    List<FlockModelData> FullList;
    List<FlockModelData> dataSet;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.flock_namesearch_textview);
        }
    }

    AdapterFlock(List<FlockModelData> listItem) {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);
    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_flock_items, parent, false);
        return new AdapterFlock.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder holder, int position) {
        FlockModelData currentItem = dataSet.get(position);
        holder.tvName.setText(currentItem.getName());
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
            ArrayList<FlockModelData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(FullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FlockModelData item : FullList) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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
