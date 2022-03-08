package com.example.a321projectprototype.ui.Past_Recordings;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.chibde.visualizer.LineBarVisualizer;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;

import java.util.List;

public class PastRecordingsCardviewAdpator  extends RecyclerView.Adapter<PastRecordingsCardviewAdpator.MyViewHolder>
{
    List<String> listItem;
    HomePage homePage;
    int position;
    private LineBarVisualizer lineBarVisualizer;
    private ImageView iconPlayer;
    private MediaPlayer mediaPlayer;

    PastRecordingsCardviewAdpator(List<String> listItem, HomePage homePage)
    {
        this.homePage = homePage;
        this.listItem = listItem;
    }
    @NonNull
    @Override
    public PastRecordingsCardviewAdpator.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastrecordings_cardview,parent,false);



        lineBarVisualizer = view.findViewById(R.id.visualizerLineBar);
        iconPlayer = view.findViewById(R.id.pastrecordingRecycleViewPlayer);

        //iconPlayer.setOnClickListener(playerRecording);

        return new PastRecordingsCardviewAdpator.MyViewHolder(view);
    }



    public View.OnClickListener playerRecording = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
           // mediaPlayer = MediaPlayer.create( this,  );

            mediaPlayer.start();


            lineBarVisualizer.setVisibility(View.VISIBLE);

            lineBarVisualizer.setColor(R.color.purple_200);

            // define the custom number of bars we want in the visualizer between (10 - 256).
            lineBarVisualizer.setDensity(60);
        }

    };



    @Override
    public void onBindViewHolder(@NonNull PastRecordingsCardviewAdpator.MyViewHolder holder, int position)
    {
        this.position = position;
        holder.tvItem.setText(listItem.get(position));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvItem;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvItem = itemView.findViewById(R.id.past_recording_day_textview);

        }
    }



}
