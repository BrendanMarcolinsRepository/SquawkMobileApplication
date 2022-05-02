package com.example.a321projectprototype.ui.Record;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.a321projectprototype.Database.RecordingPathFileDatabase;
import com.example.a321projectprototype.HomePage;
import com.example.a321projectprototype.R;
import com.example.a321projectprototype.User.Files;
import com.example.a321projectprototype.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static androidx.activity.result.contract.ActivityResultContracts.*;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.getExternalCacheDirs;

public class RecordFragment<Switch> extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback
{


    private View root;
    private RecordViewModel recordViewModel;
    private ImageView recordImage;
    private MediaRecorder mediaRecorder;
    private HomePage homePage;
    private boolean isRecording  = false, cloudStorage = false;
    private Context context;
    private NavController navController;
    private AnimatorSet mAnimationSet;
    private TextView recordingInformationTexview;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private AppBarConfiguration appBarConfiguration;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private String userID, filePath, desciption = "bird recording", fileName = "birdRecording", dateString, dateStringFile, filePathFirebase;
    private Date date;
    private SwitchCompat recordSwitch;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_record, container, false);
        recordImage = root.findViewById(R.id.recordIcon);
        recordingInformationTexview = root.findViewById(R.id.recordInformationTextview);
        progressBar = root.findViewById(R.id.recordProgressBar);
        recordSwitch =  root.findViewById(R.id.recordSwitch);
        progressBar.setVisibility(View.INVISIBLE);


        recordImage.setOnClickListener(record);
        recordSwitch.setOnCheckedChangeListener(switched);

        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        homePage = (HomePage)getActivity();
        navController = homePage.getNav();

        DrawerLayout drawerLayout = homePage.getDrawer();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        context = homePage.getApplicationContext();



        return root;
    }

    private final CompoundButton.OnCheckedChangeListener switched = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            cloudStorage = isChecked;

            if(cloudStorage){
                Toast.makeText(homePage,"Storing on the cloud", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(homePage,"Storing on your phone", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private final View.OnClickListener record = new View.OnClickListener() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
           if(!isRecording)
           {


               if(ContextCompat.checkSelfPermission(homePage,Manifest.permission.RECORD_AUDIO)
                       != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},1);
                   return;
               }

               if(ContextCompat.checkSelfPermission(homePage,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                       != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                   return;
               }

               if(ContextCompat.checkSelfPermission(homePage,Manifest.permission.INTERNET)
                       != PackageManager.PERMISSION_GRANTED) {
                   requestPermissions(new String[]{Manifest.permission.INTERNET},1);
                   return;
               }

               isRecording = true;
               startRecord();
               startAlphaAnimation();
               System.out.println("1");


           } else {
               isRecording = false;
               stopRecording();
           }
        }
    };




    public void startRecord() {

        disableMenuItems();
        if(haveNetwork()) {

        } else {
            System.out.println("ready to be saved");
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        filePath = getRecordingFilePath();
        mediaRecorder.setOutputFile(filePath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            recordingInformationTexview.setText("Now Recording Your Chirps....");


            System.out.println("Recording Started...");
        } catch (IOException e) {
            System.out.println("Didnt work" + e);
        }

    }

    private void stopRecording() {
        System.out.println("it stopped");
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        stopAlphaAnimation();

        response();
    }



    private boolean haveNetwork() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    private String getRecordingFilePath() {
        date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy hh:mm:ss");
        SimpleDateFormat ft2 = new SimpleDateFormat ("dd-MM-yyyy-hh:mm:ss");
        dateString = ft.format(date);
        dateStringFile = ft2.format(date);

        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        filePathFirebase = homePage.getExternalCacheDir().getAbsolutePath();
        filePathFirebase +=  "/" + UUID.randomUUID()+"_bird_recording"+".3gp";


        return filePathFirebase;
    }


    private void startAlphaAnimation() {
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

    private void stopAlphaAnimation() {
        mAnimationSet.removeAllListeners();
        mAnimationSet.end();
        System.out.println("stopping");
    }

    private void response() {

        recordImage.setVisibility(View.INVISIBLE);
        recordSwitch.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        recordingInformationTexview.setText("Identifing Your Chirps....");

         CountDownTimer countDownTimer = new CountDownTimer(6000,5) {
            private boolean warned = false;
            @Override
            public void onTick(long millisUntilFinished_) {
                if(millisUntilFinished_ == 3000) {
                    recordingInformationTexview.setText("Almost Chirping There....");
                }
            }


            @Override
            public void onFinish() {
                Random randomObject = new Random();
                int randomInteger = randomObject.nextInt(2);


                if(randomInteger == 0) {
                    if(cloudStorage){
                        storeRecordingFilePath();

                    }else {
                        noSqlRecordingPath();
                    }
                } else {

                    errorOccured();
                }

            }

         }.start();
    }

    public void errorOccured() {


        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Error Occured");
        alertDialog.setMessage("We had a problem trying to identify your recording, please try again");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();

        recordImage.setVisibility(View.VISIBLE);
        recordSwitch.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        recordingInformationTexview.setText("Record Your Chirps....");
        enableMenuItems();
    }


    private void noSqlRecordingPath() {
        RecordingPathFileDatabase recordingPathFileDatabase = new RecordingPathFileDatabase(homePage);
        Files files = new Files(dateString.substring(0,10),desciption,fileName,filePath,dateString);
        recordingPathFileDatabase.addFile(files);
        enableMenuItems();
        navController.navigate(R.id.action_nav_record_data);


    }

    private void storeRecordingFilePath() {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("BirdRecordings").child(filePath);

        userID = auth.getCurrentUser().getUid();

        Uri uri = Uri.fromFile(new File(filePath));
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                System.out.println("--------------------------- worked");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("--------------------------- failured  " + e);
            }
        });


        DocumentReference documentReference = firebaseFirestore.collection("files").document();

        HashMap<String,Object> userMap = new HashMap<>();

        String time = dateString.substring(0,10);

        userMap.put("created_at",time);
        userMap.put("description",desciption);
        userMap.put("filename",fileName);
        userMap.put("path", storageReference.getPath());
        userMap.put("updated_at", dateString);
        userMap.put("uploadedBy", userID);


        documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                enableMenuItems();
                navController.navigate(R.id.action_nav_record_data);
            }
        });



    }

    public void disableMenuItems() {
        homePage.disableMenuItems();
    }
    public void enableMenuItems() {
        homePage.enableMenuItems();
    }

}






