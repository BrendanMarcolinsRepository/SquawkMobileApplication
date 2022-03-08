package com.example.a321projectprototype.ui.Record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsCardviewAdpator;

import java.util.List;

public class RecordDataCardViewAdapter extends RecyclerView.Adapter<RecordDataCardViewAdapter.MyViewHolder>
{
    List<String> listItem;
    HomePage homePage;
    int position;

    RecordDataCardViewAdapter(List<String> listItem, HomePage homePage)
    {
        this.homePage = homePage;
        this.listItem = listItem;
    }
    @NonNull
    @Override
    public RecordDataCardViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_data_retrival_cardview,parent,false);

        return new RecordDataCardViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordDataCardViewAdapter.MyViewHolder holder, int position)
    {
        this.position = position;
        holder.identifiedBirdTexview.setText("Identified: " + listItem.get(position));

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView identifiedBirdTexview;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            identifiedBirdTexview = itemView.findViewById(R.id.record_retrival_data_identified_texview);


            /*
            NavController navController = homePage.getNav();
            Bundle bundle = new Bundle();
            bundle.putString("birdName", listItem.get(position));
            navController.navigate(R.id.action_nav_discover_to_bird,bundle);
            */

        }
    }
}
