package com.example.a321projectprototype.ui.Rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.UserScore;

import java.util.ArrayList;

public class RewardRecyclerAdaptor extends RecyclerView.Adapter<RewardRecyclerAdaptor.ViewHolder>
{
    private ArrayList<UserScore> userRewardItems;
    private String dropDownItem;

    public RewardRecyclerAdaptor(ArrayList<UserScore> userRewardItems, String dropDownItem) {
        this.userRewardItems = userRewardItems;
        this.dropDownItem = dropDownItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rank_bar,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int rank = position + 1;
        holder.txtView.setText("#"+rank+" "+userRewardItems.get(position).getUsername());
        if(dropDownItem.equalsIgnoreCase("Total")){
            holder.textView.setText(" "+userRewardItems.get(position).getTotalScore());
        }
        if(dropDownItem.equalsIgnoreCase("This Week")){
            holder.textView.setText(" "+userRewardItems.get(position).getScoreThisWeek());
        }
        if(dropDownItem.equalsIgnoreCase("This Month")){
            holder.textView.setText(" "+userRewardItems.get(position).getScoreThisMonth());
        }
        if(dropDownItem.equalsIgnoreCase("This Year")){
            holder.textView.setText(" "+userRewardItems.get(position).getScoreThisYear());
        }
    }

    @Override
    public int getItemCount() {
        return userRewardItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtView;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.reward_userName);
            textView = itemView.findViewById(R.id.reward_userScore);
        }
    }
}
