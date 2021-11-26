package com.example.a321projectprototype.ui.Record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
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

import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.ui.home.HomeFragment;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static androidx.activity.result.contract.ActivityResultContracts.*;

public class RecordFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {



    private RecordViewModel recordViewModel;
    private Button recordButton;
    private MediaRecorder mediaRecorder;
    private File file;
    private HomePage homePage;
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private boolean recording = false;
    private FragmentActivity fragmentActivity;
    private String fileName;
    private boolean isRecording  = false;



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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_record, container, false);
        recordButton = root.findViewById(R.id.recordFragmentButton);

        recordButton.setOnClickListener(record);

        recordViewModel =
                new ViewModelProvider(this).get(RecordViewModel.class);

        homePage = (HomePage)getActivity();




        final TextView textView = root.findViewById(R.id.text_gallery);




        recordViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {

            }
        });



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
                       PackageManager.PERMISSION_GRANTED)

               {
                   // You can use the API that requires the permission.
                   isRecording = true;
                   startRecord();
                   recordButton.setText("Recording Now...Press to Stop");
                   recordButton.setBackgroundColor(getResources().getColor(R.color.red));
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
        System.out.println("Recording Started...");
        //recordButton.setText("Recording...");

        

        try
        {
            System.out.println("it work");
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();


        }
        catch (IOException e)
        {
            System.out.println("Didnt work");
        }

    }

    private void stopRecording()
    {
        System.out.println("it stopped");
        mediaRecorder.stop();
        mediaRecorder.release();
        recordButton.setText("Press To Record");
        recordButton.setBackgroundColor(getResources().getColor(R.color.darkerGreen));
    }

    private void requestPermission()
    {
        if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO))
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

        // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.

        }


    }

    private String getRecordingFilePath()
    {
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory,"birdRecordingFile" + ".mp3");
        return file.getPath();
    }

}






