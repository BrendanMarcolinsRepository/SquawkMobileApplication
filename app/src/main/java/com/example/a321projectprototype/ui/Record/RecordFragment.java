package com.example.a321projectprototype.ui.Record;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.home.HomeFragment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static androidx.activity.result.contract.ActivityResultContracts.*;

public class RecordFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback
{



    private RecordViewModel recordViewModel;
    private ImageView recordImage;
    private MediaRecorder mediaRecorder;
    private HomePage homePage;
    private boolean isRecording  = false;
    private Context context;
    private NavController navController;
    private AnimatorSet mAnimationSet;
    private TextView recordingInformationTexview;
    private ProgressBar progressBar;



    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new RequestPermission(), isGranted ->
            {
                if(isGranted)
                {
                    System.out.println("5");

                }
                else
                {

                    System.out.println("6");


                }

            });


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_record, container, false);
        recordImage = root.findViewById(R.id.recordIcon);
        recordingInformationTexview = root.findViewById(R.id.recordInformationTextview);
        progressBar = root.findViewById(R.id.recordProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        recordImage.setOnClickListener(record);

        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        homePage = (HomePage)getActivity();
        navController = homePage.getNav();

        context = homePage.getApplicationContext();



        return root;
    }

    private final View.OnClickListener record = new View.OnClickListener()
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v)
        {
           if(!isRecording)
           {

               if (ContextCompat.checkSelfPermission(
                       getContext(), Manifest.permission.RECORD_AUDIO) ==
                       PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                       getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_GRANTED)

               {
                   // You can use the API that requires the permission.
                   isRecording = true;
                   startRecord();
                   startAlphaAnimation();
                   System.out.println("1");
               }

               else
               {
                   System.out.println("2");
                   requestPermission();
               }
           }
           else
           {
               isRecording = false;
               stopRecording();
           }
        }
    };



    public void startRecord()
    {


        if(haveNetwork())
        {

        }
        else
        {
            System.out.println("ready to be saved");
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        System.out.println("Location: " + getRecordingFilePath());
        mediaRecorder.setOutputFile(getRecordingFilePath());

        try
        {
            mediaRecorder.prepare();
            mediaRecorder.start();
            recordingInformationTexview.setText("Now Recording Your Chirps....");


            System.out.println("Recording Started...");
        }
        catch (IOException e)
        {
            System.out.println("Didnt work" + e);
        }

    }

    private void stopRecording()
    {
        System.out.println("it stopped");
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        stopAlphaAnimation();

        response();
    }



    private boolean haveNetwork()
    {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void requestPermission()
    {
        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)
                && shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) )
        {
            System.out.println("3");

            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Recording Permission");
            alertDialog.setMessage("Do you Accept recording permission?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            requestPermissionLauncher.launch(
                                    Manifest.permission.RECORD_AUDIO);

                            requestPermissionLauncher.launch(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);


                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            alertDialog.dismiss();

                        }
                    });
            alertDialog.show();

        }
        else
        {
            System.out.println("4");
            requestPermissionLauncher.launch(
                    Manifest.permission.RECORD_AUDIO);
            requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


    }

    private String getRecordingFilePath()
    {
        Date date = new Date();
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory,"birdRecordingFile-" + date.getDate() +".mp3");
        return file.getPath();
    }


    private void startAlphaAnimation()
    {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(recordImage, "alpha",  1f, .3f);
        fadeOut.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(recordImage, "alpha", .3f, 1f);
        fadeIn.setDuration(2000);

        mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }

    private void stopAlphaAnimation()
    {
        mAnimationSet.removeAllListeners();
        mAnimationSet.end();
        System.out.println("stopping");
    }

    private void response()
    {

        recordImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        recordingInformationTexview.setText("Identifing Your Chirps....");

         CountDownTimer countDownTimer = new CountDownTimer(6000,5) {
            private boolean warned = false;
            @Override
            public void onTick(long millisUntilFinished_)
            {
                if(millisUntilFinished_ == 3000)
                {
                    recordingInformationTexview.setText("Almost Chirping There....");
                }
            }


            @Override
            public void onFinish()
            {
                Random randomObject = new Random();
                int randomInteger = randomObject.nextInt(2);

                if(randomInteger == 0)
                {
                    navController.navigate(R.id.action_nav_record_data);
                }
                else
                {
                    errorOccured();
                }

            }
        }.start();



    }

    public void errorOccured()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Error Occured");
        alertDialog.setMessage("We had a problem trying to identify your recording, please try again");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();

        recordImage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        recordingInformationTexview.setText("Record Your Chirps....");
    }

}






