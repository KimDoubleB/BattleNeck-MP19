package com.example.term19;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DowonYoon on 2017-07-11.
 */

@IgnoreExtraProperties
public class FirebasePost {
    public String id;
    public String pw;
    public int age;
    public String gender;
    public int score;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String id, String pw, int age, String gender, int score) {
        this.id = id;
        this.pw = pw;
        this.age = age;
        this.gender = gender;
        this.score = score;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pw", pw);
        result.put("age", age);
        result.put("gender", gender);
        result.put("score", score);
        return result;
    }
}
