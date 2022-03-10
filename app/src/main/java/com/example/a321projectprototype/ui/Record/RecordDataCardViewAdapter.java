package com.example.a321projectprototype.ui.Record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.ItemDataModel;
import com.example.a321projectprototype.ui.Past_Recordings.PastRecordingsCardviewAdpator;

import java.util.ArrayList;
import java.util.List;

public class RecordDataCardViewAdapter extends RecyclerView.Adapter<RecordDataCardViewAdapter.MyViewHolder>
{
    List<ItemDataModel> listItem;
    HomePage homePage;
    int position;

    RecordDataCardViewAdapter(List<ItemDataModel> listItem, HomePage homePage)
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

        this.position = holder.getAbsoluteAdapterPosition();
        holder.identifiedBirdTexview.setText("Identified: " + listItem.get(position).getTxtname());

        int res =  listItem.get(position).getDrawable();
        holder.imageViewBirds.setImageResource(res);



        holder.pointsTextView.setText("Congradulation You Have Earned: 10 Points");

        holder.url = listItem.get(position).getUrl();
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView identifiedBirdTexview, pointsTextView;
        ImageView imageViewBirds;
        Button moreInfo;
        String url;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            identifiedBirdTexview = itemView.findViewById(R.id.record_retrival_data_identified_texview);
            imageViewBirds = itemView.findViewById(R.id.record_retrival_data_imageview);
            pointsTextView = itemView.findViewById(R.id.recordCardViewtextView1);
            moreInfo = itemView.findViewById(R.id.moreInfoButtonRecordCardview);

            moreInfo.setOnClickListener(moreInfoClick);



        }


        private final View.OnClickListener moreInfoClick = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBrowseClick(v);
            }
        };

        public void onBrowseClick(View v) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // Note the Chooser below. If no applications match,
            // Android displays a system message.So here there is no need for try-catch.
            homePage.startActivity(Intent.createChooser(intent, "Browse with"));

        }
    }
}
