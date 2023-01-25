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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class PastRecordingsCardviewAdpator  extends RecyclerView.Adapter<PastRecordingsCardviewAdpator.MyViewHolder>
{

    List<Files> files;
    HomePage homePage;



    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private String filePath, fileDescription, fileName,time;
    private Files fileObjects;
    private String date;
    private boolean cloud;
    private ProgressBar progressBar;

    PastRecordingsCardviewAdpator(HomePage homePage, List<Files> files, boolean cloud)
    {
        this.homePage = homePage;
        this.files = files;
        this.cloud = cloud;

    }
    @NonNull
    @Override
    public PastRecordingsCardviewAdpator.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastrecordings_cardview,parent,false);
        return new PastRecordingsCardviewAdpator.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PastRecordingsCardviewAdpator.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        fileDescription = files.get(position).getDescription();
        fileName = files.get(position).getFilename();
        time = files.get(position).getCreated_at();

        holder.name.setText(fileName);

        if (time.length() > 10) {
            holder.time.setText(time.substring(10, 16));
        } else {
            holder.time.setText(time);
        }

        holder.iconPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cloud) {
                    progressBar.setVisibility(View.VISIBLE);
                    holder.seekBar.setVisibility(View.INVISIBLE);
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                    firebaseStorage.getReference().child(files.get(position).getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressBar.setVisibility(View.INVISIBLE);
                            holder.seekBar.setVisibility(View.VISIBLE);
                            mediaPlayer = MediaPlayer.create(homePage, uri);
                            setUpMediaPlayer(holder);
                        }
                    });


                } else {
                    String path = files.get(position).getPath();
                    mediaPlayer = MediaPlayer.create(homePage, Uri.parse(path));
                    setUpMediaPlayer(holder);
                }


            }
        });


        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    holder.seekBar.setProgress(0);
                    holder.pause.setVisibility(View.INVISIBLE);
                    holder.iconPlayer.setVisibility(View.VISIBLE);
                    System.out.println("worked stopped");
                }
            }
        });



    }

    public void setUpMediaPlayer(MyViewHolder holder){
        handler = new Handler();

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                System.out.println("worked");
                holder.seekBar.setMax(mp.getDuration());
                mp.start();
                holder.upDateSeekBar();
                holder.pause.setVisibility(View.VISIBLE);
                holder.iconPlayer.setVisibility(View.INVISIBLE);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mediaPlayer.stop();
                mediaPlayer.reset();
                holder.seekBar.setProgress(0);
                holder.pause.setVisibility(View.INVISIBLE);
                holder.iconPlayer.setVisibility(View.VISIBLE);
                System.out.println("worked stopped");

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
        SeekBar seekBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.past_recording_day_textview);
            time = itemView.findViewById(R.id.past_recording_day_time);
            seekBar = itemView.findViewById(R.id.pastRecordingSeekBar);
            iconPlayer = itemView.findViewById(R.id.pastrecordingRecycleViewPlayer);
            pause = itemView.findViewById(R.id.pastrecordingPauseRecycleViewPlayer);
            pause.setVisibility(View.INVISIBLE);
            progressBar = itemView.findViewById(R.id.pastRecordingCardViewProgressBar);
            progressBar.setVisibility(View.INVISIBLE);


        }

        public void upDateSeekBar(){

            int position;
            position = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(position);

            runnable = new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer == null){
                        return;
                    }

                    if(mediaPlayer.isPlaying()){
                        upDateSeekBar();
                    }
                }
            };

            handler.postDelayed(runnable,1000);
        }

    }

}
