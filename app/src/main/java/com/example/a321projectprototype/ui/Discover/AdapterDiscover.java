package com.example.a321projectprototype.ui.Discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.BirdModel;

import java.util.ArrayList;
import java.util.List;


public class AdapterDiscover  extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Discover.AdapterDiscover.MyViewHolder> implements Filterable {

    List<BirdModel> FullList;
    List<BirdModel> dataSet;
    private HomePage homePage;
    private NavController navigation;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView bird;
        String latitude, logatude, sciName, visit, date,code;



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
                bundle.putString("latitude",latitude);
                bundle.putString("logatude",logatude);
                bundle.putString("sciName",sciName);
                bundle.putString("visit",visit);
                bundle.putString("date",date);
                bundle.putString("code",code);
                navigation.navigate(R.id.action_nav_discover_to_bird,bundle);
            }
        };

    }

    AdapterDiscover(List<BirdModel> listItem, HomePage homePage)
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
        BirdModel currentItem = dataSet.get(position);
        holder.bird.setText(currentItem.getComName());
        holder.latitude = dataSet.get(position).getLat();
        holder.logatude = dataSet.get(position).getLng();
        holder.sciName = dataSet.get(position).getSciName();
        holder.visit = dataSet.get(position).getLocName();
        holder.date = dataSet.get(position).getObsDt();
        holder.code = dataSet.get(position).getSpeciesCode();

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
            ArrayList<BirdModel> filteredList = new ArrayList<>();
            System.out.println("worked 1");
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(FullList);
                System.out.println("worked 2");
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                System.out.println("worked 3");
                for (BirdModel item : FullList) {
                    if (item.getComName().toLowerCase().contains(filterPattern)) {
                        System.out.println("worked 4");
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


