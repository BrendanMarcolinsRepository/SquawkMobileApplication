package com.example.a321projectprototype.ui.Rewards;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserScoreObject {
    public long scoreThisMonth;
    public long scoreThisWeek;
    public long scoreThisYear;
    public long totalScore;
    public String user_id;

    public UserScoreObject(long scoreThisMonth, long scoreThisWeek, long scoreThisYear, long totalScore) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

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

        return result;
    }
}
