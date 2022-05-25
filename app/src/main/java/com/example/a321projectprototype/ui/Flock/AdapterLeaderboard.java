package com.example.a321projectprototype.ui.Flock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;
import com.example.a321projectprototype.User.FlockScoreModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterLeaderboard  extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapterLeaderboard.MyViewHolder>
{
    protected List<FlockScoreModel> flockList;
    protected List<Integer> flockScoreList;
    protected int position;

    //Leaderboard adapter, nothing special happening here, just pushing data to the user interface

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView memberNameTextview, scoreTextview, positionTextview;

        MyViewHolder(View itemView)
        {
            super(itemView);

            memberNameTextview = itemView.findViewById(R.id.leaderboard_flock_name);
            scoreTextview = itemView.findViewById(R.id.leaderboard_flock_points);
            positionTextview = itemView.findViewById(R.id.leaderboardPosition);
        }
    }

    AdapterLeaderboard(List<FlockScoreModel> flockList , List<Integer> flockScoreList)
    {
        this.flockList = flockList;
        this.flockScoreList = flockScoreList;

    }

    @NonNull
    @Override
    public com.example.a321projectprototype.ui.Flock.AdapterLeaderboard.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_content, parent, false);
        return new AdapterLeaderboard.MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull com.example.a321projectprototype.ui.Flock.AdapterLeaderboard.MyViewHolder holder, int position)
    {
        holder.memberNameTextview.setText(flockList.get(position).getFlockname());
        holder.scoreTextview.setText(flockScoreList.get(position).toString());
        int pos = position + 1;
        holder.positionTextview.setText("#"+pos);

    }

    @Override
    public int getItemCount() {
        return flockList.size();
    }

}