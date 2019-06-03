package com.bappe.term19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    /*Wizets*/
    private Button signUp, signIn;
    private EditText idText, pwText;

    // id and password
    public String id, pw;

    // SharedPreference for user auto login
    public SharedPreferences sh_UserInfo;
    public SharedPreferences.Editor toEdit;
    public boolean isOK = false; // User is legitimate?



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Message2", String.valueOf(FirebasePost.isActive));

        // if neck tracking service is active?
        if(FirebasePost.isActive){
            // destroy service for protecting duplicate service
            Intent intent = new Intent(this, MyService.class);
            stopService(intent);
            FirebasePost.isActive = false;
            ServiceThread.stopForever();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebasePost.isActive = false;  // set inactive service

        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);
        idText = findViewById(R.id.id);
        pwText = findViewById(R.id.pw);

        // Database initialize
        FirebaseApp.initializeApp(this);
        // get user data from firebase realtime database
        FirebasePost.getUserData();

        // get sharedPreference data (if there is...)
        applySharedPreference();
        // sign in buttion
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = idText.getText().toString();
                pw = pwText.getText().toString();

                // Check user id & password
                if (FirebasePost.users.containsKey(id)) {
                    if (FirebasePost.users.get(id).equals(pw)) {
                        // If user is legitimate user,
                        sharedPreference(); // store in sharedPreference
                        Intent intent2 = new Intent(getApplicationContext(), MainHomeActivity.class);
                        intent2.putExtra("ID", id);
                        startActivity(intent2);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "PW is incorrect !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID is incorrect !", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Sign up button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Sign up intent
                Intent intent = new Intent(getApplicationContext(), signUpActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get sharedPreference data (if there is...)
        FirebasePost.getUserData();
    }

    public void sharedPreference() {
        // store id & pw info in sharedPreference
        sh_UserInfo = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        toEdit = sh_UserInfo.edit();
        toEdit.putString("UserId", id);
        toEdit.putString("UserPw", pw);
        toEdit.commit();
    }

    public void applySharedPreference() {
        // get data in sharedPrference
        sh_UserInfo = getSharedPreferences("Login Credentials", MODE_PRIVATE);

        // if there is....
        if (sh_UserInfo != null && sh_UserInfo.contains("UserId")) {
            String id2 = sh_UserInfo.getString("UserId", "Default");
            idText.setText(id2);
            String pw2 = sh_UserInfo.getString("UserPw", "Default");
            pwText.setText(pw2);
        }
    }
}
