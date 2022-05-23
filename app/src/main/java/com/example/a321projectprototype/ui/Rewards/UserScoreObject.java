package com.example.a321projectprototype.ui.Rewards;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserScoreObject {
    public String createdAt;
    public String updatedAt;
    public long scoreThisMonth;
    public long scoreThisWeek;
    public long scoreThisYear;
    public long totalScore;
    public String user_id;

    public UserScoreObject(long scoreThisMonth, long scoreThisWeek, long scoreThisYear, long totalScore) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        //if created at field doesn't exist
        if(!firebaseFirestore.document("/userScore/"+user_id).get().getResult().contains("created_at")) {
            createdAt = timeFormat.format(cal);
        }

        updatedAt = timeFormat.format(cal);

        this.scoreThisMonth = scoreThisMonth;
        this.scoreThisWeek = scoreThisWeek;
        this.scoreThisYear = scoreThisYear;
        this.totalScore = totalScore;
        this.user_id = auth.getUid();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("scoreThisMonth", scoreThisMonth);
        result.put("scoreThisWeek", scoreThisWeek);
        result.put("scoreThisYear", scoreThisYear);
        result.put("totalScore", totalScore);
        result.put("user_id", user_id);
        result.put("created_at", createdAt);
        result.put("updated_at", updatedAt);

        return result;
    }
}
