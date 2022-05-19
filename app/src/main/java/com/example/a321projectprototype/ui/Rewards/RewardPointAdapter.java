package com.example.a321projectprototype.ui.Rewards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.RewardPointModel;

import java.util.ArrayList;

public class RewardPointAdapter extends RecyclerView.Adapter<RewardPointAdapter.RewardRecViewHolder>
{
    private ArrayList<RewardPointModel> rewardPointList;

    public RewardPointAdapter(ArrayList<RewardPointModel> rewardPointList) {
        this.rewardPointList = rewardPointList;
    }

    public ArrayList<RewardPointModel> getRewardPointList() {
        return rewardPointList;
    }

    public void setRewardPointList(ArrayList<RewardPointModel> rewardPointList) {
        this.rewardPointList = rewardPointList;
    }

    @NonNull
    @Override
    public RewardRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_bar,parent,false);
        return new RewardRecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardRecViewHolder holder, int position) {
        int birdCountNum = rewardPointList.get(position).getBirdCount();
        String birdStatusMessage = rewardPointList.get(position).getBirdStatus();
        holder.birdStatusCount.setText(birdCountNum + " x "+birdStatusMessage+" Birds");
        holder.birdName.setText(rewardPointList.get(position).getBirdName());
        holder.birdTotalScore.setText("+"+rewardPointList.get(position).getTotalRewardPoints());
    }

    @Override
    public int getItemCount() {
        return rewardPointList.size();
    }

    public class RewardRecViewHolder extends RecyclerView.ViewHolder{
        private TextView birdStatusCount;
        private TextView birdName;
        private TextView birdTotalScore;
        public RewardRecViewHolder(@NonNull View itemView) {
            super(itemView);
            birdStatusCount = itemView.findViewById(R.id.birdStatusItem);
            birdName = itemView.findViewById(R.id.birdNameItem);
            birdTotalScore = itemView.findViewById(R.id.birdTotalScore);
        }
    }
}
