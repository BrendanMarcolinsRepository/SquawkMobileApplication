package com.example.a321projectprototype.ui.Past_Recordings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.CommentModel;
import com.example.a321projectprototype.User.Files;
import com.chibde.visualizer.LineBarVisualizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class PastRecordingsCardviewAdpator  extends RecyclerView.Adapter<PastRecordingsCardviewAdpator.MyViewHolder>
{

    List<Files> files;
    HomePage homePage;



    private MediaPlayer mediaPlayer;
    private String filePath, fileDescription, fileName,time;
    private Files fileObjects;
    private String date;

    PastRecordingsCardviewAdpator(HomePage homePage, List<Files> files)
    {
        this.homePage = homePage;
        this.files = files;

    }
    @NonNull
    @Override
    public PastRecordingsCardviewAdpator.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastrecordings_cardview,parent,false);
        return new PastRecordingsCardviewAdpator.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PastRecordingsCardviewAdpator.MyViewHolder holder,  int position) {
        fileDescription = files.get(position).getDescription();
        fileName = files.get(position).getFilename();
        time = files.get(position).getCreated_at();

        holder.name.setText(fileName);
        holder.time.setText(time.substring(10,16));

        holder.iconPlayer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String path =  files.get(position).getPath();

                System.out.println(path);

                mediaPlayer = MediaPlayer.create(homePage,Uri.parse(path));

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        System.out.println("worked");
                        mp.start();
                        holder.pause.setVisibility(View.VISIBLE);
                        holder.iconPlayer.setVisibility(View.INVISIBLE);
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        mediaPlayer.stop();
                        mediaPlayer.release();
                        holder.pause.setVisibility(View.INVISIBLE);
                        holder.iconPlayer.setVisibility(View.VISIBLE);
                        System.out.println("worked stopped");

                    }
                });




            }
        });

        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    holder.pause.setVisibility(View.INVISIBLE);
                    holder.iconPlayer.setVisibility(View.VISIBLE);
                    System.out.println("worked stopped");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,time, description;
        ImageView iconPlayer, pause;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.past_recording_day_textview);
            time = itemView.findViewById(R.id.past_recording_day_time);
            iconPlayer = itemView.findViewById(R.id.pastrecordingRecycleViewPlayer);
            pause = itemView.findViewById(R.id.pastrecordingPauseRecycleViewPlayer);
            pause.setVisibility(View.INVISIBLE);


        }

    }

}
