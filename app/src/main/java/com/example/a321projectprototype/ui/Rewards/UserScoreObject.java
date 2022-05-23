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
import java.util.Date;
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

        Date date = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy");

        updatedAt = timeFormat.format(date);

        this.scoreThisMonth = scoreThisMonth;
        this.scoreThisWeek = scoreThisWeek;
        this.scoreThisYear = scoreThisYear;
        this.totalScore = totalScore;
        this.user_id = auth.getUid();
    }

    public void getCreatedAt(String userID, onCallBack callback) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.document("/userScore/"+userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().contains("created_at")) {
                            createdAt = task.getResult().get("created_at").toString();
                        } else {
                            Date date = new Date();
                            SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy");

                            createdAt = timeFormat.format(date);
                        }
                        callback.callBack();
                    }
                });
    }

    public void toMap(onCallBackMap callback) {
        getCreatedAt(user_id, new onCallBack() {
            @Override
            public void callBack() {
                HashMap<String, Object> result = new HashMap<>();
                result.put("scoreThisMonth", scoreThisMonth);
                result.put("scoreThisWeek", scoreThisWeek);
                result.put("scoreThisYear", scoreThisYear);
                result.put("totalScore", totalScore);
                result.put("user_id", user_id);
                result.put("created_at", createdAt);
                result.put("updated_at", updatedAt);

                callback.callBack(result);
            }
        });
    }

    public interface onCallBack {
        void callBack();
    }

    public interface onCallBackMap {
        void callBack(Map<String, Object> map);
    }
}
