package com.example.a321projectprototype.ui.Flock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

import java.util.ArrayList;
import java.util.List;

public class AdapterFlock extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder>
{
    List<FlockModelData> FullList;
    List<FlockModelData> dataSet;

    private HomePage homePage;
    private NavController navigation;
    private Button joinButton, infoButton;
    private int picked;
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView groupName, groupCountNumber;



        MyViewHolder(View itemView)
        {
            super(itemView);
            navigation = homePage.getNav();
            groupName = itemView.findViewById(R.id.flock_namesearch_textview);
            groupCountNumber = itemView.findViewById(R.id.flock_amount_textview);
            joinButton = itemView.findViewById(R.id.flock_join_button);
            infoButton = itemView.findViewById(R.id.flock_info_button);
            infoButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    picked = getLayoutPosition();
                    System.out.println("worked");
                    Bundle bundle = new Bundle();
                    bundle.putInt("Position",picked );
                    navigation.navigate(R.id.info_fragment_nav,bundle);
                }
            });

        }
    }

    AdapterFlock(List<FlockModelData> listItem, HomePage homePage)
    {
        this.dataSet = listItem;
        this.homePage = homePage;
        FullList = new ArrayList<>(listItem);
    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_flock_items, parent, false);
        return new AdapterFlock.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Flock.AdapterFlock.MyViewHolder holder, int position)
    {
        FlockModelData currentItem = dataSet.get(position);
        holder.groupName.setText(currentItem.getName());
        holder.groupCountNumber.setText(currentItem.getGroupNumber() + "/200");


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
