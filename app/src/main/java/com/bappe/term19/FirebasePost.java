package com.bappe.term19;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@IgnoreExtraProperties
public class FirebasePost {
    public String id;
    public String pw;
    public int age;
    public String gender;
    public int score;

    // useful information for application
    public static HashMap<String, String> users = new HashMap<>();
    public static HashMap<String, Integer> userScores = new HashMap<>();
    public static boolean isActive;

    public FirebasePost() {
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    // constructor and get data from user
    public FirebasePost(String id, String pw, int age, String gender, int score) {
        this.id = id;
        this.pw = pw;
        this.age = age;
        this.gender = gender;
        this.score = score;

    }

    // get User data
    public static void getUserData() {
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("id_list/");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // All data in Firebase DB
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Extract ID and PW
                    String dataID = ((HashMap<String, Object>) snapshot.getValue()).get("id").toString();
                    String dataPW = ((HashMap<String, Object>) snapshot.getValue()).get("pw").toString();
                    String dataScore = ((HashMap<String, Object>) snapshot.getValue()).get("score").toString();

                    // get the data from firebase Realtime DB
                    // and store in FirebasePost class variable
                    users.put(dataID, dataPW);
                    userScores.put(dataID, Integer.parseInt(dataScore));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // add data to firebase dadtabase
    public boolean addDataFirebase() {
        FirebasePost.getUserData();

        if (users.containsKey(id)) {
            return false; // duplicated ID
        } else {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("id_list/" + this.id);
            myRef.setValue(this.toMap());
            return true;
        }
    }

    // Mapping the information
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

    // update the data in Firebase realtime DB
    public static void writeNewPost(String userId, int score) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("id_list/" + userId + "/score/");
        mDatabase.setValue(score);
    }
}
