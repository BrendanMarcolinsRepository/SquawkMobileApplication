package com.example.a321projectprototype.ui.Flock;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;



public class AdapaterMemberFlock extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder>
{
    protected List<FlockMembersModel> FullList;
    protected List<FlockMembersModel> dataSet;


    private int picked;

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView memberName, score;

        MyViewHolder(View itemView)
        {
            super(itemView);

            memberName = itemView.findViewById(R.id.memberFockName);
            score = itemView.findViewById(R.id.memberFockScore);
        }
    }

    AdapaterMemberFlock(List<FlockMembersModel> listItem)
    {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);

    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flock_group_table, parent, false);
        return new AdapaterMemberFlock.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Flock.AdapaterMemberFlock.MyViewHolder holder, int position)
    {
        FlockMembersModel memberFlock = dataSet.get(position);
        holder.memberName.setText(memberFlock.getUsername());
        holder.score.setText(Integer.toString(memberFlock.getScore()));


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
