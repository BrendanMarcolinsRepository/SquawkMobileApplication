package com.example.a321projectprototype.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

public class IdentifiedBirdModel {
    private String recordedBy;
    private Date date;
    private String birdStatus;

    private Calendar cal = Calendar.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public IdentifiedBirdModel() {

    }

    public double getData(String userID) {
        Task<QuerySnapshot> mColRef = db.collection("identified_bird")
                .whereEqualTo("recorded_by", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getData());
                            }
                        } else {
                            Log.d("IdentifiedBird", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return 5;
    }
}
