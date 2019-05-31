package com.bappe.term19;

import android.support.annotation.NonNull;
import android.util.Log;

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

//        readDataFirebase();
    }

    public static void readDataFirebase(){

        // 중복제거를하려고 이렇게 했는데 안된다....
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("id_list/");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All data in Firebase DB
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Extract ID and PW
                    String dataID = ((HashMap<String, Object>)snapshot.getValue()).get("id").toString();
                    idList.add(dataID);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public boolean addDataFirebase(){
        Log.d("Message2", idList.toString());
        Log.d("Message2", id);

        if(idList.contains(id)){
            return false; // duplicated ID
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("id_list/" + this.id);
        Log.d("Message2", idList.toString());
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
