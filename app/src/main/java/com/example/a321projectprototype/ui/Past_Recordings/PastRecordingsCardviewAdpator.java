package com.example.a321projectprototype.ui.Past_Recordings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class PastRecordingsCardviewAdpator  extends RecyclerView.Adapter<PastRecordingsCardviewAdpator.MyViewHolder>
{

    List<Files> files;
    HomePage homePage;
    int position;

    private ImageView iconPlayer;
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
    public void onBindViewHolder(@NonNull PastRecordingsCardviewAdpator.MyViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        this.position = position;

        if(files.size() > 0)
        {
            fileObjects = files.get(position);
            filePath = fileObjects.getPath();
            fileDescription = fileObjects.getDescription();
            fileName = fileObjects.getFilename();
            time = fileObjects.getCreated_at();

            holder.name.setText(fileName);
            holder.time.setText(time.substring(10,16));
        }

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,time, description;
        private ImageView iconPlayer, pause;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.past_recording_day_textview);
            time = itemView.findViewById(R.id.past_recording_day_time);




            iconPlayer = itemView.findViewById(R.id.pastrecordingRecycleViewPlayer);
            pause = itemView.findViewById(R.id.pastrecordingPauseRecycleViewPlayer);

            iconPlayer.setOnClickListener(playerRecording);
            pause.setOnClickListener(pauseRecording);
            pause.setVisibility(View.INVISIBLE);



        }



        public View.OnClickListener playerRecording = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.out.println("=>>>>>>>>>>>>>Staring: " + filePath);


                mediaPlayer = MediaPlayer.create(homePage, Uri.parse(filePath));


                if(mediaPlayer == null)
                {
                    Toast.makeText(homePage.getBaseContext(), "Could No Find File, Please Try Again",Toast.LENGTH_LONG).show();
                    return;
                }

                int audioSessionId = mediaPlayer.getAudioSessionId();



                if(audioSessionId != -1)
                {

                    if(ContextCompat.checkSelfPermission(homePage,Manifest.permission.ACCESS_MEDIA_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(homePage,new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION},1);

                    }

                    mediaPlayer.start();
                    iconPlayer.setVisibility(View.INVISIBLE);
                    pause.setVisibility(View.VISIBLE);


                }
            }

        };


        public View.OnClickListener pauseRecording = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    pause.setVisibility(View.INVISIBLE);
                    iconPlayer.setVisibility(View.VISIBLE);
                }
            }

        };
    }
}
