package com.example.term19;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RankingActivity extends AppCompatActivity {

    public TextView rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        rank = findViewById(R.id.ranking);
        setRanking();
    }

    public void setRanking() {
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("id_list/");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All data in Firebase DB
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getValue();
                    /*
                    //    this.id = id;
                        this.pw = pw;
                        this.age = age;
                        this.gender = gender;
                    //    this.score = score;
                    */

                    String dataID = ((HashMap<String, Object>) snapshot.getValue()).get("id").toString();
                    String dataScore = ((HashMap<String, Object>) snapshot.getValue()).get("score").toString();
                    Log.d("Message", dataID + " " + dataScore);

                    rank.setText(rank.getText() + dataID + " : " + dataScore + '\n');


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void onBackPressed() {
        finish();
    }

}
