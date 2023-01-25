package com.example.a321projectprototype.ui.Flock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.FlockModelData;

import java.util.ArrayList;
import java.util.List;

public class AdapterLeaderboard  extends RecyclerView.Adapter<com.example.a321projectprototype.ui.Flock.AdapterLeaderboard.MyViewHolder>
{
    protected List<FlockModelData> FullList;
    protected List<FlockModelData> dataSet;
    protected int position;

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

    AdapterLeaderboard(List<FlockModelData> listItem)
    {
        this.dataSet = listItem;
        FullList = new ArrayList<>(listItem);

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
        FlockModelData memberFlock = dataSet.get(position);
        holder.memberNameTextview.setText(memberFlock.getName());
        holder.scoreTextview.setText(Integer.toString(0));
        holder.positionTextview.setText(Integer.toString(1));


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}