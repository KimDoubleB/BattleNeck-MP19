package com.example.term19;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.HashSet;
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
    public static HashSet<String> idList = new HashSet<>();


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
    public boolean addDataFirebase(){
        if(idList.contains(id)){
            return false; // duplicated ID
        }

        idList.add(id);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("id_list/" + this.id);
        myRef.setValue(this.toMap());
        return true;
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
