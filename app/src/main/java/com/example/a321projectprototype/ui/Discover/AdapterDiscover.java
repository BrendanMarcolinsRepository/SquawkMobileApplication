package com.example.a321projectprototype.ui.Discover;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsCardviewAdpator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AdapterDiscover  extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder> {

    List<ItemDataModel> FullList;
    List<ItemDataModel> dataSet;
    private HomePage homePage;
    private NavController navigation;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView bird;



        MyViewHolder(View itemView)
        {
            super(itemView);

            bird = itemView.findViewById(R.id.birds);
            navigation = homePage.getNav();
            itemView.setOnClickListener(choiceMethod);
        }

        private final View.OnClickListener choiceMethod = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String birdName = bird.getText().toString();



                System.out.println(birdName);

                Bundle bundle = new Bundle();
                bundle.putString("birdName",birdName);
                navigation.navigate(R.id.action_nav_discover_to_bird,bundle);
            }
        };

    }

    AdapterDiscover(List<ItemDataModel> listItem, HomePage homePage)
    {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);
        this.homePage = homePage;
    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_discover_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder holder, int position)
    {
        ItemDataModel currentItem = dataSet.get(position);
        holder.bird.setText(currentItem.getTxtname());

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


