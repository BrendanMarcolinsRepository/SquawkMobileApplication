package com.example.a321projectprototype.ui.Past_Recordings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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


import com.example.a321projectprototype.Database.RecordingPathFileDatabase;
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

public class PastRecordingsCardviewAdpator  extends RecyclerView.Adapter<PastRecordingsCardviewAdpator.MyViewHolder> {

    List<Files> files;
    HomePage homePage;
    private MediaPlayer mediaPlayer;
    private Handler progressBarHandler, timeHandler;
    private Runnable runnable;
    private boolean cloud;


    PastRecordingsCardviewAdpator(HomePage homePage, List<Files> files, boolean cloud) {
        this.homePage = homePage;
        this.files = files;
        this.cloud = cloud;

    }
    @NonNull
    @Override
    public PastRecordingsCardviewAdpator.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastrecordings_cardview,parent,false);
        return new PastRecordingsCardviewAdpator.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PastRecordingsCardviewAdpator.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String fileName = files.get(position).getFilename();
        String time = files.get(position).getTime();

        holder.name.setText(fileName);
        holder.time.setText(time);

        holder.iconPlayer.setOnClickListener(v -> {

            System.out.println("worked1");

            if(mediaPlayer != null){
                if(mediaPlayer.isPlaying()){
                    stopPlayer(mediaPlayer,holder);
                    System.out.println("worked2");
                }
                else {
                    System.out.println("worked3");
                    setUpPlayer(holder,position);
                }
            }else{
                System.out.println("worked4");
                setUpPlayer(holder,position);
            }
        });
    }

    public void setUpPlayer(MyViewHolder holder, int position){
        if(cloud) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.seekBar.setVisibility(View.INVISIBLE);
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

            firebaseStorage.getReference().child(files.get(position)
                    .getPath())
                    .getDownloadUrl()
                    .addOnSuccessListener(uri -> {

                        System.out.println("URI:" + uri.toString());
                        holder.iconPlayer.setImageResource(R.drawable.pause);
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.seekBar.setVisibility(View.VISIBLE);
                        mediaPlayer = MediaPlayer.create(homePage, uri);
                        setUpSeekBar(holder);
                    });
        } else {
            String path = files.get(position).getPath();
            mediaPlayer = MediaPlayer.create(homePage, Uri.parse(path));
            setUpSeekBar(holder);
        }

    }

    public void setUpSeekBar(MyViewHolder holder){


        progressBarHandler = new Handler();
        timeHandler = new Handler();
        System.out.println("worked5");
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mediaPlayer.setOnPreparedListener(mp -> {
            startPlayer(mp,holder);
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayer( mp,holder);
            }
        });

    }

    public void startPlayer(MediaPlayer mp,MyViewHolder holder){
        holder.seekBar.setMax(mp.getDuration());
        mp.start();
        holder.end.setText(holder.convertTimer(mp.getDuration()));
        holder.iconPlayer.setImageResource(R.drawable.pause);
        holder.upDateSeekBar();
        holder.updateTime();

    }


    public void stopPlayer(MediaPlayer mp,MyViewHolder holder){
        mp.stop();
        mp.reset();
        holder.seekBar.setProgress(0);
        holder.progressBar.setVisibility(View.INVISIBLE);
        holder.start.setText(R.string.timer);
        holder.iconPlayer.setImageResource(R.drawable.play);
    }




    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,time, start,end;
        ImageView iconPlayer;
        SeekBar seekBar;
        private ProgressBar progressBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.past_recording_day_textview);
            time = itemView.findViewById(R.id.past_recording_day_time);
            seekBar = itemView.findViewById(R.id.pastRecordingSeekBar);
            iconPlayer = itemView.findViewById(R.id.pastrecordingRecycleViewPlayer);
            start = itemView.findViewById(R.id.pastReocordingStartTimer);
            end = itemView.findViewById(R.id.pastReocordingEndTimer);
            progressBar = itemView.findViewById(R.id.pastRecordingCardViewProgressBar);
            progressBar.setVisibility(View.INVISIBLE);


        }

        public void updateTime(){

            int position;
            position = mediaPlayer.getCurrentPosition();


            runnable = new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer == null){
                        return;
                    }

                    if(mediaPlayer.isPlaying()){
                        start.setText(convertTimer(position));
                        updateTime();
                    }
                }
            };

            timeHandler.postDelayed(runnable,0);
        }

        public void upDateSeekBar(){

            int position;
            position = mediaPlayer.getCurrentPosition();


            runnable = new Runnable() {
                @Override
                public void run() {

                    if(mediaPlayer == null){
                        return;
                    }

                    if(mediaPlayer.isPlaying()){
                        seekBar.setProgress(position);
                        upDateSeekBar();
                    }
                }
            };

            progressBar.postDelayed(runnable,1000);
        }




        public String convertTimer(long convert) {
            String audioTime;
            int minutes = (int) (convert % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((convert % (1000 * 60 * 60)) % (1000 * 60) / 1000);
            audioTime = String.format("%02d:%02d", minutes, seconds);
            return audioTime;
        }


    }

    public void setCloudBoolean(boolean cloudBoolean){
        this.cloud = cloudBoolean;
    }

}
